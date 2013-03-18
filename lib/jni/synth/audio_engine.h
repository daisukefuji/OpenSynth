// audio_engine.h
// Daisuke Fuji <daisuke@indigo-lab.com>

#ifndef AUDIO_ENGINE_H
#define AUDIO_ENGINE_H

#ifdef __cplusplus

#include "controller.h"

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <stdint.h>

namespace synth {
class AudioEngine {
public:
  AudioEngine();
  ~AudioEngine();
  bool start();
  bool shutdown();

  virtual int render(int16_t* buf, int buf_size) = 0;

private:
  SLresult initialize();
  SLresult finalize();
  SLresult enable();
  SLresult disable();
  SLresult setup_player();
  bool is_started() { return started_; }

  static void bq_player_callback(SLAndroidSimpleBufferQueueItf bq, void* data);
  void bq_player_callback_handler(SLAndroidSimpleBufferQueueItf bq);

  static const int N_BUFFERS = 2;
  static const int BUFFER_SIZE = 384;

  int16_t* buffer_;
  int cur_buffer;
  bool started_;
  SLObjectItf engine_object_;
  SLEngineItf engine_engine_;
  SLObjectItf output_mix_object_;
  SLObjectItf bq_player_object_;
  SLPlayItf bq_player_play_;
  SLAndroidSimpleBufferQueueItf bq_player_buffer_queue_;
}; 
};	// end of namespace


#endif //__cpluspluse

#endif //AUDIO_ENGINE_H

