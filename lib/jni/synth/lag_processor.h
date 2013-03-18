// lag_processor.h
// Author: Allen Porter <allen@thebends.org>
//
// A lag processor, used to implement glide.

#include "parameter.h"
#include "envelope.h"

#ifndef __LAG_PROCESSOR_H__
#define __LAG_PROCESSOR_H__

namespace synth {

class LagProcessor : public Parameter {
 public:
  LagProcessor(Parameter* param);
  virtual ~LagProcessor();
  
  void set_param(Parameter* param);

  // Number of samples for each 1.0 change in the parameters value
  void set_samples(long samples);
  void set_samples_up(long samples);
  void set_samples_down(long samples);

  // Reset the last value used (effectively disables glide from the last value)
  void reset();

  virtual float GetValue();

 private:
  long samples_up_;
  long samples_down_;
  Parameter* param_;
  bool has_last_value_;
  float last_value_;
  long samples_;

  Envelope envelope_;
};

}  // namespace

#endif  // __LOG_PROCESSOR_H__
