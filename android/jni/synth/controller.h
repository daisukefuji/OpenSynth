// controller.h
// Author: Allen Porter <allen@thebends.org>
//
// The controller module returns samples and drives the oscillator and envelope.

#ifndef __CONTROLLER_H__
#define __CONTROLLER_H__

#include "synth/arpeggio.h"
#include "synth/envelope.h"
#include "synth/filter.h"
#include "synth/lag_processor.h"
#include "synth/modulation.h"
#include "synth/oscillator.h"
#include "synth/parameter.h"
#include "synth/key_stack.h"

namespace synth {

class Volume : public Parameter {
 public:
  Volume();
  virtual ~Volume();

  virtual float GetValue();

  void set_level(float level) { level_ = level; }
  void set_modulation(Parameter* param) { modulation_ = param; }
  Envelope* envelope() { return &envelope_; }

 private:
  // base
  float level_;
  Envelope envelope_;
  Parameter* modulation_;
};

class Controller {
 public:
  Controller();

  // Volume [0, 1.0]
  void set_volume(float volume);

  // Start/Stop playing a note.  These may trigger the Attack and Release of the
  // volume and filter envelopes, depending on the order of the on/off events.
  // It is an error to call NoteOff() for a note that was never the argument of
  // NoteOn();
  void NoteOn(int midi_note);
  void NoteOff(int midi_note);
  void NoteOnFrequency(float frequency);  // For testing
  void NoteOff();  // Invoked when all notes have been released as a fallback
  
  // True when nothing is playing
  bool released() {
    return (volume_envelope()->released() || filter_envelope()->released());
  }
  
  void set_sample_rate(float sample_rate);  // For testing

  // A shift in frequency by the specified amount.  The frequency gets
  // multiplied by 2^n
  enum OctaveShift {
    OCTAVE_1 = 1,  // No shift
    OCTAVE_2 = 2,
    OCTAVE_4 = 4,
    OCTAVE_8 = 8,
    OCTAVE_16 = 16
  };

  // OSC 1

  // Set the volume of oscillator
  void set_osc1_level(float level);
  // Set the wave form of oscillator
  void set_osc1_wave_type(Oscillator::WaveType wave_type);
  // The oscillator frequency is shifted by the specified amount.
  void set_osc1_octave(OctaveShift octave);

  // OSC 2
  void set_osc2_level(float level);
  void set_osc2_wave_type(Oscillator::WaveType wave_type);
  void set_osc2_octave(OctaveShift octave);
  void set_osc2_shift(int cents);

  void set_osc_sync(bool sync);

  Envelope* volume_envelope() { return volume_.envelope(); }
  Envelope* filter_envelope() { return filter_cutoff_.envelope(); }

  enum ModulationSource {
    LFO_SRC_SQUARE,
    LFO_SRC_TRIANGLE,
    LFO_SRC_SAWTOOTH,
    LFO_SRC_REVERSE_SAWTOOTH,
// TODO(allen): How do you determine the sustain length?
// Is it sustain = period - (attack + decay + release)?
//    LFO_FILTER_ENVELOPE,
// TODO(allen): OSC2 needs a manual frequency control for this to work, i think.
//    OSC2,
  };
  enum ModulationDestination {
    LFO_DEST_WAVE,  // Tremelo
    LFO_DEST_PITCH,  // Vibrato
    LFO_DEST_FILTER,
    // TODO(allen): Is this Ring modulation?
    // LFO_DEST_OSC2,
  };
  void set_modulation_source(ModulationSource source);
  void set_modulation_destination(ModulationDestination dest);
  void set_modulation_amount(float amount);
  void set_modulation_frequency(float frequency);

  void set_filter_cutoff(float frequency);
  
  // [0.0, 1.0]
  void set_filter_resonance(float value);

  void set_glide_samples(long samples);
  
  void set_arpeggio_enabled(bool enabled);
  void set_arpeggio_samples(long samples);
  void set_arpeggio_octaves(int octaves);
  void set_arpeggio_step(Arpeggio::Step step);

  // Get a single sample
  float GetSample();

  void GetFloatSamples(float* buffer, int size);
  
 private:
  // Invoked when one of the routing parameters changes, such as the source
  // or destination of modulation.
  void reset_routing();

  KeyStack key_stack_;
  MutableParameter key_frequency_;

  bool arpeggio_enabled_;
  Arpeggio arpeggio_;
  LagProcessor key_lag_processor_;

  Oscillator osc1_;
  Oscillator osc2_;

  // The two oscillators combined
  KeyboardOscillator combined_osc_;
  Volume volume_;

  bool osc_sync_;

  ModulationSource modulation_source_;
  ModulationDestination modulation_destination_;
  MutableParameter modulation_frequency_;
  Oscillator modulation_osc_;
  MutableParameter modulation_amount_;
  LFO modulation_;

  FilterCutoff filter_cutoff_;
  LowPassFilter lowpass_filter_;
  ResonantFilter resonant_filter_;
};

}  // namespace synth

#endif // __CONTROLLER_H__
