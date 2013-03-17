// parameter.h
// Author: Allen Porter <allen@thebends.org>
//
// The parameter interface is used to abstract the notion of a setting of a
// another module (such as an Oscillator or Filter) that may change over time.
//
// For example, the frequency parameter of an oscillator can change via:
// - A note being struck
// - Glide setting that controls the time to move from one note to another
// - An "octave" setting
// - A fine tune parameter
// The parameter lets us separate the way we determine the frequency from the
// module that is interested in using the value.

#ifndef __PARAMETER_H__
#define __PARAMETER_H__

namespace synth {
  
class Parameter {
 public:
  virtual ~Parameter() { }

  // Return the current value of the parameter.  Parameters that change over
  // time expect to be invoked once for every sample.  All modules assume they
  // are using the same default sample rate.
  virtual float GetValue() = 0;

 protected:
  Parameter() { }
};

// A parameter that always returns a fixed value.  This is mostly used for
// testing other components with a simple parameter.
class FixedParameter : public Parameter {
 public:
  FixedParameter(float value);
  virtual ~FixedParameter();

  virtual float GetValue();

 private:
  float value_;
};

// A parameter that can be changed.
class MutableParameter : public Parameter {
 public:
  MutableParameter(float value);
  virtual ~MutableParameter();

  virtual float GetValue();
  void set_value(float value);

 private:
  float value_;
};

}  // namespce synth

#endif  // __PARAMETER_H__
