// envelope.h
// Author: Allen Porter <allen@thebends.org>
//
// An envelope controls a value over time.

#ifndef __ENVELOPE_H__
#define __ENVELOPE_H__

#include "parameter.h"

namespace synth {

class Envelope : public Parameter {
 public:
  Envelope();
  virtual ~Envelope();

  // samples
  void set_attack(long attack);

  // samples
  void set_decay(long decay);

  // Sustain Volumne
  void set_sustain(float sustain);

  // samples
  void set_release(long release);

  // The value reached at the peak of the attack (Typically 1.0).
  void set_max(float max);
  // The value at the start and release (Typically 0.0).
  void set_min(float min);

  // Invoked when the note is pressed, resets all counters.
  void NoteOn();
  // Invoked when the note is released.
  void NoteOff();

  // Advances the clock and returns the value for the current step.  Should not
  // be called when Done() returns false.
  virtual float GetValue();

  // True when the note has finished playing.
  bool released() const;

 private:
  enum State {
    ATTACK = 0,
    DECAY = 1,
    SUSTAIN = 2,
    RELEASE = 3,
    DONE = 4,
  };
  long attack_;
  float attack_slope_;
  long decay_;
  long decay_end_;
  float decay_slope_;
  float sustain_;
  long release_;
  long release_start_;
  long release_end_;
  float release_slope_;

  float min_;
  float max_;

  long current_;  // sample
  float last_value_;
  State state_;
  float release_start_value_;
};

}  // namespace synth

#endif  // __ENVELOPE_H__
