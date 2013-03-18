// audio_engine.cpp
// Daisuke Fuji <daisuke@indigo-lab.com>

#include "audio_engine.h"
#include "config.h"

#include <stddef.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include <android/log.h>

#define TAG "AudioEngine"
#define LOG_D(fmt, ...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##__VA_ARGS__)
#define LOG_V(fmt, ...) __android_log_print(ANDROID_LOG_INFO, TAG, fmt, ##__VA_ARGS__)
#define LOG_E(fmt, ...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##__VA_ARGS__)

using namespace synth;

namespace synth {
AudioEngine::AudioEngine() : cur_buffer(0),
                             started_(false),
                             engine_object_(NULL),
                             engine_engine_(NULL),
                             output_mix_object_(NULL),
                             bq_player_object_(NULL),
                             bq_player_play_(NULL),
                             bq_player_buffer_queue_(NULL)
{
  buffer_ = new int16_t[CONFIG_CH * N_BUFFERS * BUFFER_SIZE];
}

AudioEngine::~AudioEngine()
{
  if (is_started()) {
    shutdown();
  }
  delete buffer_;
  buffer_ = NULL;
}

bool AudioEngine::start()
{
  SLresult ret = SL_RESULT_SUCCESS;

  if (is_started()) {
    LOG_E("error: already started");
    return false;
  }

  ret = initialize();
  if (SL_RESULT_SUCCESS != ret) {
    goto err;
  }

  ret = setup_player();
  if (SL_RESULT_SUCCESS != ret) {
    goto err;
  }

  ret = enable();
  if (SL_RESULT_SUCCESS != ret) {
    goto err;
  }

  started_ = true;

  return true;

err:
  finalize();
  return false;
}

bool AudioEngine::shutdown()
{
  SLresult ret = SL_RESULT_SUCCESS;

  if (!is_started()) {
    LOG_E("error: not started yet");
    return false;
  }

  ret = disable();
  if (SL_RESULT_SUCCESS != ret) {
    goto err;
  }

  finalize();
  started_ = false;

  return true;

err:
  finalize();
  started_ = false;
  return false;
}

SLresult AudioEngine::initialize()
{
  SLresult ret = SL_RESULT_SUCCESS;

  ret = slCreateEngine(&engine_object_, 0, NULL, 0, NULL, NULL);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: slCreateEngine ret = %lx", ret);
    goto err;
  }

  ret = (*engine_object_)->Realize(engine_object_, SL_BOOLEAN_FALSE);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: Realize ret = %lx", ret);
    goto err;
  }

  ret = (*engine_object_)->GetInterface(engine_object_, SL_IID_ENGINE, &engine_engine_);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: GetInterface SL_IID_ENGINE ret = %lx", ret);
    goto err;
  }

  ret = (*engine_engine_)->CreateOutputMix(engine_engine_, &output_mix_object_, 0, NULL, NULL);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: CreateOutputMix ret = %lx", ret);
    goto err;
  }

  ret = (*output_mix_object_)->Realize(output_mix_object_, SL_BOOLEAN_FALSE);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: Realize ret = %lx", ret);
    goto err;
  }

err:
  return ret;
}

SLresult AudioEngine::finalize()
{
  if (bq_player_object_ != NULL) {
    (*bq_player_object_)->Destroy(bq_player_object_);
    bq_player_object_ = NULL;
    bq_player_play_ = NULL;
    bq_player_buffer_queue_ = NULL;
  }

  if (output_mix_object_) {
    (*output_mix_object_)->Destroy(output_mix_object_);
    output_mix_object_ = NULL;
  }

  if (engine_object_) {
    (*engine_object_)->Destroy(engine_object_);
    engine_object_ = NULL;
    engine_engine_ = NULL;
  }

  return SL_RESULT_SUCCESS;
}

SLresult AudioEngine::enable()
{
  SLresult ret = SL_RESULT_SUCCESS;

  ret = (*bq_player_play_)->SetPlayState(bq_player_play_, SL_PLAYSTATE_PLAYING);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: SetPlayState ret = %lx", ret);
    goto err;
  }

err:
  return ret;
}

SLresult AudioEngine::disable()
{
  SLresult ret = SL_RESULT_SUCCESS;

  ret = (*bq_player_play_)->SetPlayState(bq_player_play_, SL_PLAYSTATE_PAUSED);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: SetPlayState ret = %lx", ret);
    goto err;
  }

err:
  return ret;
}

SLresult AudioEngine::setup_player()
{
  SLDataLocator_AndroidSimpleBufferQueue loc_bufq = {
    SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, N_BUFFERS
  };
  SLDataFormat_PCM format_pcm = {
    SL_DATAFORMAT_PCM, CONFIG_CH, CONFIG_SAMPLINGRATE, SL_PCMSAMPLEFORMAT_FIXED_16,
    SL_PCMSAMPLEFORMAT_FIXED_16,
#if CONFIG_CH == CONFIG_CH_MONO
    SL_SPEAKER_FRONT_CENTER,
#else
    SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
#endif // end of CONFIG_CH
    SL_BYTEORDER_LITTLEENDIAN
  };
  SLDataSource audioSrc = {&loc_bufq, &format_pcm};

  SLDataLocator_OutputMix loc_outmix = {
    SL_DATALOCATOR_OUTPUTMIX, output_mix_object_
  };
  SLDataSink audioSnk = {&loc_outmix, NULL};

  const SLInterfaceID ids[2] = {SL_IID_BUFFERQUEUE, SL_IID_VOLUME};
  const SLboolean req[2] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};

  SLresult ret = SL_RESULT_SUCCESS;

  ret = (*engine_engine_)->CreateAudioPlayer(engine_engine_, &bq_player_object_, &audioSrc, &audioSnk, 2, ids, req);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: CreateAudioPlayer ret = %lx", ret);
    goto err;
  }

  ret = (*bq_player_object_)->Realize(bq_player_object_, SL_BOOLEAN_FALSE);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: Realize ret = %lx", ret);
    goto err;
  }

  ret = (*bq_player_object_)->GetInterface(bq_player_object_, SL_IID_PLAY, &bq_player_play_);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: GetInterface SL_IID_PLAY ret = %lx", ret);
    goto err;
  }

  ret = (*bq_player_object_)->GetInterface(bq_player_object_, SL_IID_BUFFERQUEUE, &bq_player_buffer_queue_);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: GetInterface SL_IID_BUFFERQUEUE ret = %lx", ret);
    goto err;
  }

  ret = (*bq_player_buffer_queue_)->RegisterCallback(bq_player_buffer_queue_, bq_player_callback, this);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: RegisterCallback ret = %lx", ret);
    goto err;
  }

  for (int i = 0; i < N_BUFFERS - 1; ++i) {
    bq_player_callback_handler(bq_player_buffer_queue_);
  }

err:
  return ret;
}

void AudioEngine::bq_player_callback(SLAndroidSimpleBufferQueueItf bq, void* data)
{
  AudioEngine* engine = static_cast<AudioEngine*> (data);
  engine->bq_player_callback_handler(bq);
}

void AudioEngine::bq_player_callback_handler(SLAndroidSimpleBufferQueueItf bq)
{
  int16_t *buf_ptr = buffer_ + BUFFER_SIZE * cur_buffer;
  render(buf_ptr, BUFFER_SIZE);
  SLresult ret = (*bq_player_buffer_queue_)->Enqueue(bq_player_buffer_queue_, buf_ptr, BUFFER_SIZE * 2);
  if (SL_RESULT_SUCCESS != ret) {
    LOG_E("error: Enqueue ret = %lx", ret);
  }
  cur_buffer = (cur_buffer + 1) % N_BUFFERS;
}
} // end of android

