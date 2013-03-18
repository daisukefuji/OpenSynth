// filter.h
// Author: Allen Porter <allen@thebends.org>
//
// A filter that passes low-frequency signals.

#ifndef __FILTER_H__
#define __FILTER_H__

#include "parameter.h"
#include "envelope.h"

namespace synth {

// Interface for a filter based on some cutoff frequency (usually high or low
// pass).  The input value is a sample and the output value is a new sample
// at that time.
class Filter {
 public:
  virtual ~Filter();

  // The cutoff frequency of the filter.
  virtual void set_cutoff(Parameter* cutoff) = 0;

  virtual float GetValue(float x) = 0;

 protected:
  Filter();
};

// A simple LowPassFilter FIR filter
class LowPassFilter : public Filter {
 public:
  LowPassFilter();
  virtual ~LowPassFilter();

  virtual void set_cutoff(Parameter* cutoff);

  virtual float GetValue(float x);

 private:
  void reset(float frequency);

  Parameter* cutoff_;
  // Used to keep the last value of the frequency cutoff.  If it changes, we
  // need to re-initialize the filter co-efficients.
  float last_cutoff_;

  // for input value x[k] and output y[k]
  float x1_;  // input value x[k-1]
  float x2_;  // input value x[k-2]
  float y1_;  // output value y[k-1]
  float y2_;  // output value y[k-2]

  // filter coefficients
  float a0_;
  float a1_;
  float a2_;
  float b1_;
  float b2_;
  float b3_;
};

// Simple VCF
class ResonantFilter : public Filter {
 public:
  ResonantFilter();
  virtual ~ResonantFilter();

  virtual void set_cutoff(Parameter* cutoff);
  virtual void set_resonance(float resonance);

  virtual float GetValue(float x);

 private:
  Parameter* cutoff_;
  float resonance_;

  float y1_;
  float y2_;
  float y3_;
  float y4_;
  float oldx_;
  float oldy1_;
  float oldy2_;
  float oldy3_;
};


// Encapsulates the logic for calculating the filter cutoff frequency
class FilterCutoff : public Parameter {
 public:
  FilterCutoff();
  virtual ~FilterCutoff();

  virtual float GetValue();

  void set_cutoff(float cutoff) { cutoff_ = cutoff; }
  void set_modulation(Parameter* param) { modulation_ = param; }
  Envelope* envelope() { return &envelope_; }

 private:
  // base
  float cutoff_;
  Envelope envelope_;
  Parameter* modulation_;
};

}  // namespace synth

#endif  // __FILTER_H__
