#include <stddef.h>
#include <jni.h>
#include <synth/synth-jni.h>
#include <synth/controller.h>

static synth::Controller* controller = NULL;

JNIEXPORT jfloatArray JNICALL Java_org_thebends_synth_SynthJni_getSamples(
    JNIEnv* env,
    jclass obj,
    jint num_samples) {
  if (controller == NULL) {
    controller = new synth::Controller;
    controller->set_osc1_level(1.0);
    controller->set_osc1_wave_type(synth::Oscillator::TRIANGLE);
    controller->NoteOnFrequency(1024);
  }
  float* buffer = new float[num_samples];
  controller->GetFloatSamples(buffer, num_samples);
  jfloatArray result = env->NewFloatArray(num_samples);
  env->SetFloatArrayRegion(result, 0, num_samples, buffer); 
  delete buffer;
  return result;
}
