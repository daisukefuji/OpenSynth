// filter.cpp
// Author: Allen Porter <allen@thebends.org>

#include "synth/filter.h"
#include <math.h>
#include <stddef.h>

namespace synth {

static const float kSampleRate = 44100.0f;
static const float kE = 2.71828183f;

Filter::Filter() { }

Filter::~Filter() { }

LowPassFilter::LowPassFilter() : cutoff_(NULL), last_cutoff_(0), x1_(0), x2_(0), y1_(0), y2_(0) {
}

LowPassFilter::~LowPassFilter() { }

void LowPassFilter::set_cutoff(Parameter* cutoff) {
  cutoff_ = cutoff;
}

void LowPassFilter::reset(float frequency) {  
  // Number of filter passes
  float n = 1;

  // 3dB cutoff frequency
  float f0 = frequency;

  // Sample frequency
  const float fs = 44100.0f;

  // 3dB cutoff correction
  float c = powf(powf(2, 1.0f / n) - 1, -0.25);

  // Polynomial coefficients
  float g = 1;
  float p = sqrtf(2);

  // Corrected cutoff frequency
  float fp = c * (f0 / fs);

  // TODO(allen): We should probably do more filter passes so that we can ensure  // these stability constraints are met for sufficiently high frequencies.
  // Ensure fs is OK for stability constraint
  //assert(fp > 0);
  //assert(fp < 0.125);

  // Warp cutoff freq from analog to digital domain
  float w0 = tanf(M_PI * fp);

  // Calculate the filter co-efficients
  float k1 = p * w0;
  float k2 = g * w0 * w0;

  a0_ = k2 / (1 + k1 + k2);
  a1_ = 2 * a0_;
  a2_ = a0_;
  b1_ = 2 * a0_ * (1 / k2 - 1);
  b2_ = 1 - (a0_ + a1_ + a2_ + b1_);
}

float LowPassFilter::GetValue(float x) {
  if (cutoff_ == NULL) {
    return x;
  }

  // Re-initialize the filter co-efficients if they changed
  float cutoff = cutoff_->GetValue();
  if (cutoff <= 0.0f) {
    return x;
  } else if (cutoff < 0.001f) {
    // Filtering all frequencies
    return 0.0f;
  }
  if (fabs(cutoff - last_cutoff_) > 0.001f) {
    reset(cutoff);
    last_cutoff_ = cutoff;
  }

  float y = a0_ * x + a1_ * x1_ + a2_ *  x2_ + b1_ * y1_ + b2_ * y2_;
  x1_ = x;
  x2_ = x1_;
  y2_ = y1_;
  y1_ = y;
  return y;
}

ResonantFilter::ResonantFilter()
   : cutoff_(NULL), resonance_(0.0f),
     y1_(0.0f), y2_(0.0f), y3_(0.0f), y4_(0.0f),
     oldx_(0.0f), oldy1_(0.0f), oldy2_(0.0f), oldy3_(0.0f) { }

ResonantFilter::~ResonantFilter() { }

void ResonantFilter::set_cutoff(Parameter* cutoff) {
  cutoff_ = cutoff;
}

void ResonantFilter::set_resonance(float resonance) {
  resonance_ = resonance;
}

float ResonantFilter::GetValue(float x) {
  if (cutoff_ == NULL) {
    return x;
  }
  float cutoff = cutoff_->GetValue();
  float f = 2.0f * cutoff / kSampleRate;
  float k = 3.6f * f - 1.6f * f * f - 1;
  float p = (k + 1.0f) * 0.5f;
  float scale = powf(kE, (1.0f - p) * 1.386249);
  float r = resonance_ * scale;

  float out = x - r * y4_;
  y1_ = out * p + oldx_ * p - k * y1_;
  y2_ = y1_ * p + oldy1_ * p - k * y2_;
  y3_ = y2_ * p + oldy2_ * p - k * y3_;
  y4_ = y3_ * p + oldy3_ * p - k * y4_;
  y4_ = y4_ - powf(y4_, 3.0f) / 6.0f;
  oldx_ = out;
  oldy1_ = y1_;
  oldy2_ = y2_;
  oldy3_ = y3_;
  return out;
}

FilterCutoff::FilterCutoff() : cutoff_(-1.0f),
                               modulation_(NULL) { }

FilterCutoff::~FilterCutoff() { }

float FilterCutoff::GetValue() {
  float value = cutoff_ * envelope_.GetValue();
  if (modulation_) {
    value *= modulation_->GetValue();
  }
  return value;
}

}  // namespace synth
