// synth_engine.cpp
// Daisuke Fuji <daisuke@indigo-lab.com>

#include "synth_engine.h"
#include "config.h"

using namespace synth;

namespace synth {
SynthEngine::SynthEngine()
{
  controller_ = new Controller();
}

Controller* SynthEngine::get_controller()
{
  return controller_;
}

int SynthEngine::render(int16_t* buf, int buf_size)
{
#if CONFIG_CH == CONFIG_CH_MONO
  controller_->GetShortSamples(buf, buf_size);
#else
  for (int i = 0; i < buf_size; i += 2) {
    int16_t tmp;
    controller_->GetShortSamples(&tmp, 1);
    buf[i + 0] = tmp;
    buf[i + 1] = tmp;
  }
#endif // end of CONFIG_CH
  return buf_size;
}
} // end of android

