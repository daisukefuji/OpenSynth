// modualtion.cpp
// Author: Allen Porter <allen@thebends.org>

#include "modulation.h"
#include <assert.h>
#include <stddef.h>
#include <sys/param.h>
#include "oscillator.h"

namespace synth {

LFO::LFO()
    : level_(NULL),
      oscillator_(NULL)
      { }

void LFO::set_level(Parameter* level) {
  level_ = level;
}

void LFO::set_oscillator(Parameter* oscillator) {
  oscillator_ = oscillator;
}

// The oscillator affects the amplitude more as the level is increased, or
// not at all if the level is zero.
float LFO::GetValue() {
  if (level_ == NULL || oscillator_ == NULL) {
    return 1.0;
  }
  float level = level_->GetValue();
  float m = 0.5 * level;
  float b = 1.0 - m;
  float value = m * oscillator_->GetValue() + b;
  assert(value >= 0);
  assert(value <= 1.0);
  return value;
}

}  // namespace synth
