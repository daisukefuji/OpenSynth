// argpeggio.h
// Author: Allen Porter <allen@thebends.org>
//
// TODO

#include "parameter.h"

namespace synth {

class KeyStack;

class Arpeggio : public Parameter {
 public:
  Arpeggio(KeyStack* keys);
  virtual ~Arpeggio();

  virtual float GetValue();
  int GetNote();

  // The number of octaves up to include
  void set_octaves(int count);

  // Describes how to determine the next note
  enum Step {
    UP,
    DOWN,
    UP_DOWN,
    RANDOM
  };
  void set_step(Step step);

  void set_samples_per_note(long samples_per_note);
  
  void reset();

 private:
  KeyStack* keys_;
  long sample_;
  long samples_per_note_;
  int octaves_;
  int note_;
  bool moving_up_;  // Only used by UP_DOWN
  Step step_;
};

}  // namespace synth
