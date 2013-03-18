// modulation.h
// Author: Allen Porter <allen@thebends.org>

#ifndef __MODULATION_H__
#define __MODULATION_H__

#include "parameter.h"

namespace synth {

class LFO : public Parameter {
 public:
  LFO();

  // Set the amount of modulation from [0, 1].
  void set_level(Parameter* level);

  // The specified oscillator should have its level set to 1
  void set_oscillator(Parameter* oscillator);

  // Returns an amplitude multiplier from [0, 1].
  float GetValue();

 private:
  Parameter* level_;
  Parameter* oscillator_;
};

}  // namespace synth

#endif  // __MODULATION_H__
