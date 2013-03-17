// oscillator.h
// Author: Allen Porter <allen@thebends.org>
//
// An oscillator generates a signal with a particular frequency and amplitude.

#ifndef __OSCILLATOR_H__
#define __OSCILLATOR_H__

#include "synth/parameter.h"
#include "synth/lag_processor.h"

namespace synth {

static const long kDefaultSampleRate = 44100;

class Oscillator : public Parameter {
 public:
  Oscillator();
  virtual ~Oscillator();

  enum WaveType {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH,
    REVERSE_SAWTOOTH,
  };
  void set_wave_type(WaveType wave_type);

  void set_frequency(Parameter* frequency);

  // Override the default sample rate
  void set_sample_rate(long sample_rate);

  // Returns the value at the specific time [0.0, 1.0].  The returned value
  // returned value is in the range [-1.0, 1.0].
  virtual float GetValue();

  // Start at the beginning of the period
  void Reset() { sample_num_ = 0; }

  // Returns true if this is the first sample in the period
  bool IsStart() { return (sample_num_ == 0); }

 private:
  WaveType wave_type_;
  Parameter* frequency_;

  long sample_rate_;  
  long sample_num_;
};

// Groups logic related to running the oscillators from keyboard input.
class KeyboardOscillator : public Parameter {
 public:
  KeyboardOscillator(Oscillator* osc1,
                     Oscillator* osc2,
                     Parameter* frequency);
  virtual ~KeyboardOscillator();

  // Multiple to the specified octave
  void set_osc1_octave(float multiply) { osc1_octave_ = multiply; }
  void set_osc2_octave(float multiply) { osc2_octave_ = multiply; }

  void set_osc1_level(float level) { osc1_level_ = level; }
  void set_osc2_level(float level) { osc2_level_ = level; }

  // Number of cents to shift osc2
  void set_osc2_shift(int cents);
  
  // Sync osc2 to osc1 (master)
  void set_osc_sync(bool sync) { sync_ = sync; }

  // Can be NUL to disable frequency modulation, otherwise a multiplier of the
  // current frequency intended to change over time.
  void set_frequency_modulation(Parameter* parameter) {
    frequency_modulation_ = parameter;
  }

  // Return the value of the combine oscillators
  virtual float GetValue();

 private:
  Parameter* base_frequency_;

  float osc1_octave_;
  float osc2_octave_;
  float osc1_level_;
  float osc2_level_;
  float osc2_shift_;

  MutableParameter osc1_freq_;
  MutableParameter osc2_freq_;
  Parameter* frequency_modulation_;

  bool sync_;

  Oscillator* osc1_;
  Oscillator* osc2_;
};


}  // namespace synth

#endif  // __OSCILLATOR_H__
