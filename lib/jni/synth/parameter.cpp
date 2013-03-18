// parameter.cpp
// Author: Allen Porter <allen@thebends.org>

#include "parameter.h"
#include <math.h>

namespace synth {

FixedParameter::FixedParameter(float value) : value_(value) { }

FixedParameter::~FixedParameter() { }

float FixedParameter::GetValue() {
  return value_;
}

MutableParameter::MutableParameter(float value) : value_(value) { }

MutableParameter::~MutableParameter() { }

float MutableParameter::GetValue() {
  return value_;
}

void MutableParameter::set_value(float value) {
  value_ = value;
}

}  // namespace synth
