// synth_engine.h
// Daisuke Fuji <daisuke@indigo-lab.com>

#ifndef SYNTH_ENGINE_H
#define SYNTH_ENGINE_H

#include "audio_engine.h"

namespace synth {
class SynthEngine : public AudioEngine {
public:
  SynthEngine();
  Controller* get_controller();
  int render(int16_t* buf, int buf_size);

private:
  synth::Controller* controller_;
}; 
};	// end of namespace

#endif //SYNTH_ENGINE_H

