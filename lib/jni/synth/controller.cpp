// controller.cpp
// Author: Allen Porter <allen@thebends.org>

#include "synth/controller.h"

#include <math.h>
#include <assert.h>
#include "synth/envelope.h"
#include "synth/filter.h"
#include "synth/modulation.h"
#include "synth/oscillator.h"

namespace synth {
  
Controller::Controller()
    : key_frequency_(0.0f),
      arpeggio_enabled_(false),
      arpeggio_(&key_stack_),
      key_lag_processor_(&arpeggio_),
      combined_osc_(&osc1_, &osc2_, &key_lag_processor_),
      osc_sync_(false),
      modulation_source_(LFO_SRC_SQUARE),
      modulation_destination_(LFO_DEST_WAVE),
      modulation_frequency_(0.0f),
      modulation_amount_(0.0f) {        
  modulation_osc_.set_frequency(&modulation_frequency_);
  modulation_.set_oscillator(&modulation_osc_);
  modulation_.set_level(&modulation_amount_);

  lowpass_filter_.set_cutoff(&filter_cutoff_);
  resonant_filter_.set_cutoff(&filter_cutoff_);

  reset_routing();
}

void Controller::set_volume(float volume) {
  volume_.set_level(volume);
}

void Controller::set_sample_rate(float sample_rate) {
  osc1_.set_sample_rate(sample_rate);
  osc2_.set_sample_rate(sample_rate);
}

void Controller::NoteOn(int note) {
  assert(note >= 1);
  assert(note <= 88);
  key_stack_.NoteOn(note);
  if (key_stack_.size() == 1) {
    // This is the first note played, so start attacking
    key_lag_processor_.reset();
    arpeggio_.reset();
    volume_envelope()->NoteOn();
    filter_envelope()->NoteOn();
  }
  float frequency = KeyToFrequency(key_stack_.GetCurrentNote());
  key_frequency_.set_value(frequency);
}

void Controller::NoteOnFrequency(float frequency) {
  key_frequency_.set_value(frequency);
  volume_envelope()->NoteOn();
  filter_envelope()->NoteOn();
}

void Controller::NoteOff(int note) {
  key_stack_.NoteOff(note);
  if (key_stack_.size() == 0) {
    // All notes were release, so start the release phase of the envelope
    NoteOff();
  } else {
    // There are still notes on key stack -- switch!
    float frequency = KeyToFrequency(key_stack_.GetCurrentNote());
    key_frequency_.set_value(frequency);
  }
}

void Controller::NoteOff() {
  key_stack_.clear();
  volume_envelope()->NoteOff();
  filter_envelope()->NoteOff();
}

void Controller::set_osc1_level(float level) {
  combined_osc_.set_osc1_level(level);
}

void Controller::set_osc1_wave_type(Oscillator::WaveType wave_type) {
  osc1_.set_wave_type(wave_type);
}

void Controller::set_osc1_octave(OctaveShift octave) {
  combined_osc_.set_osc1_octave((int)octave);
}

void Controller::set_osc2_level(float level) {
  combined_osc_.set_osc2_level(level);
}

void Controller::set_osc2_wave_type(Oscillator::WaveType wave_type) {
  osc2_.set_wave_type(wave_type);
}

void Controller::set_osc2_octave(OctaveShift octave) {
  combined_osc_.set_osc2_octave((int)octave);
}
  
void Controller::set_osc2_shift(int cents) {
  combined_osc_.set_osc2_shift(cents);
}

void Controller::set_osc_sync(bool sync) {
  combined_osc_.set_osc_sync(sync);
}
  
void Controller::set_glide_samples(long samples) {
  key_lag_processor_.set_samples(samples);
}

void Controller::set_arpeggio_enabled(bool enabled) {
  arpeggio_enabled_ = enabled;
  reset_routing();
}
  
void Controller::set_arpeggio_samples(long samples) {
  arpeggio_.set_samples_per_note(samples);
}

void Controller::set_arpeggio_octaves(int octaves) {
  arpeggio_.set_octaves(octaves);
}
  
void Controller::set_arpeggio_step(Arpeggio::Step step) {
  arpeggio_.set_step(step);
}
  
void Controller::set_modulation_amount(float amount) {
  modulation_amount_.set_value(amount);
}

void Controller::set_modulation_frequency(float frequency) {
  modulation_frequency_.set_value(frequency);
}

void Controller::set_modulation_source(ModulationSource src) {
  modulation_source_ = src;
  reset_routing();
}

void Controller::set_modulation_destination(ModulationDestination dest) {
  modulation_destination_ = dest;
  reset_routing();
}

void Controller::reset_routing() {
  switch (modulation_source_) {
    case LFO_SRC_SQUARE:
      modulation_osc_.set_wave_type(Oscillator::SQUARE);
      break;
    case LFO_SRC_TRIANGLE:
      modulation_osc_.set_wave_type(Oscillator::TRIANGLE);
      break;
    case LFO_SRC_SAWTOOTH:
      modulation_osc_.set_wave_type(Oscillator::SAWTOOTH);
      break;
    case LFO_SRC_REVERSE_SAWTOOTH:
      modulation_osc_.set_wave_type(Oscillator::REVERSE_SAWTOOTH);
      break;
    default:
      assert(false);
  }

  // Reset the destinations
  volume_.set_modulation(NULL);
  filter_cutoff_.set_modulation(NULL);
  combined_osc_.set_frequency_modulation(NULL);

  // Route modulation into the correct pipeline
  switch (modulation_destination_) {
    case LFO_DEST_WAVE:
      // Modulate the volume (tremelo)
      volume_.set_modulation(&modulation_);
      break;
    case LFO_DEST_PITCH:
      // Modulate the frequency (vibrato)
      combined_osc_.set_frequency_modulation(&modulation_);
      break;
    case LFO_DEST_FILTER:
      // Modulate the cutoff frequency
      filter_cutoff_.set_modulation(&modulation_);
      break;
    default:
      assert(false);
  }
  
  if (arpeggio_enabled_) {
    key_lag_processor_.set_param(&arpeggio_);
  } else {
    key_lag_processor_.set_param(&key_frequency_);
  }
}

void Controller::set_filter_cutoff(float frequency) {
  filter_cutoff_.set_cutoff(frequency);
}
  
void Controller::set_filter_resonance(float value) {
  resonant_filter_.set_resonance(value);
}

void Controller::GetFloatSamples(float* buffer, int size) {
  for (int i = 0; i < size; ++i) {
    buffer[i] = GetSample();
  }
}

float Controller::GetSample() {
  if (volume_envelope()->released() || filter_envelope()->released()) {
    return 0;
  }

  // Combined oscillators, volume/envelope/modulation
  float value = combined_osc_.GetValue();
  // Clip!
  value = fmaxf(-1.0f, value);
  value = fminf(1.0f, value);
  // Combined filter with envelope/modulation
  value = lowpass_filter_.GetValue(value);
  value = resonant_filter_.GetValue(value);
  // Clip!
  value = fmaxf(-1.0f, value);
  value = fminf(1.0f, value);
  // Adjust volume
  value *= volume_.GetValue();
  return value;
}


Volume::Volume() : level_(1.0f),
                   modulation_(NULL) { }

Volume::~Volume() { }

float Volume::GetValue() {
  float value = level_ * envelope_.GetValue();
  if (modulation_) {
    value *= modulation_->GetValue();
  }
  return value;
}
  

}  // namespace synth
