// synth-jni.cpp
// Daisuke Fuji <daisuke@indigo-lab.com>

#include "synth_engine.h"
#include "controller.h"
#include "oscillator.h"
#include "envelope.h"
#include "synth-jni.h"

#include <stddef.h>
#include <jni.h>

using namespace synth;
static SynthEngine* engine_ = NULL;

static void jniThrowException
   (JNIEnv* env, const char* name, const char* msg)
{
  env->ThrowNew(env->FindClass(name), msg);
}

JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeStart
   (JNIEnv* env, jclass obj)
{
  engine_ = new SynthEngine();
  if (engine_ == NULL) {
    jniThrowException(env, "java/lang/RuntimeException", "Out of memory");
    return -1;
  }
  engine_->start();
  return 0;
}

JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeShutdown
   (JNIEnv* env, jclass obj)
{
  if (engine_ != NULL) {
    engine_->shutdown();
    delete engine_;
    engine_ = NULL;
  }
  return 0;
}

JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeNoteOn
  (JNIEnv* env, jclass obj, jint note)
{
  engine_->get_controller()->NoteOn(note);
  return 0;
}

JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeNoteOff
  (JNIEnv* env, jclass obj, jint note)
{
  engine_->get_controller()->NoteOff(note);
  return 0;
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1Level
   (JNIEnv* env, jclass obj, jfloat level)
{
  engine_->get_controller()->set_osc1_level(level);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1WaveType
   (JNIEnv* env, jclass obj, jint wave_type)
{
  engine_->get_controller()->set_osc1_wave_type(static_cast<Oscillator::WaveType>(wave_type));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1Octave
   (JNIEnv* env, jclass obj, jint octave)
{
  engine_->get_controller()->set_osc1_octave(static_cast<Controller::OctaveShift>(octave));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2Level
   (JNIEnv* env, jclass obj, jfloat level)
{
  engine_->get_controller()->set_osc2_level(level);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2WaveType
   (JNIEnv* env, jclass obj, jint wave_type)
{
  engine_->get_controller()->set_osc2_wave_type(static_cast<Oscillator::WaveType>(wave_type));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2Octave
   (JNIEnv* env, jclass obj, jint octave)
{
  engine_->get_controller()->set_osc2_octave(static_cast<Controller::OctaveShift>(octave));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetGlideSamples
   (JNIEnv* env, jclass obj, jlong samples)
{
  engine_->get_controller()->set_glide_samples(samples);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOsc2Shift
   (JNIEnv* env, jclass obj, jint cents)
{
  engine_->get_controller()->set_osc2_shift(cents);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOscSync
   (JNIEnv* env, jclass obj, jboolean sync)
{
  engine_->get_controller()->set_osc_sync(sync);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationAmount
   (JNIEnv* env, jclass obj, jfloat amount)
{
  engine_->get_controller()->set_modulation_amount(amount);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationFrequency
   (JNIEnv* env, jclass obj, jfloat frequency)
{
  engine_->get_controller()->set_modulation_frequency(frequency);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationSource
   (JNIEnv* env, jclass obj, jint source)
{
  engine_->get_controller()->set_modulation_source(static_cast<Controller::ModulationSource>(source));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationDestination
   (JNIEnv* env, jclass obj, jint destination)
{
  engine_->get_controller()->set_modulation_destination(static_cast<Controller::ModulationDestination>(destination));
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetFilterCutoff
   (JNIEnv* env, jclass obj, jfloat frequency)
{
  engine_->get_controller()->set_filter_cutoff(frequency);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetFilterResonance
   (JNIEnv* env, jclass obj, jfloat value)
{
  engine_->get_controller()->set_filter_resonance(value);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetAttackToVolumeEnvelope
   (JNIEnv* env, jclass obj, jlong attack)
{
  synth::Envelope* envelope = engine_->get_controller()->volume_envelope();
  envelope->set_attack(attack);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetDecayToVolumeEnvelope
   (JNIEnv* env, jclass obj, jlong decay)
{
  synth::Envelope* envelope = engine_->get_controller()->volume_envelope();
  envelope->set_decay(decay);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetSustainToVolumeEnvelope
   (JNIEnv* env, jclass obj, jfloat sustain)
{
  synth::Envelope* envelope = engine_->get_controller()->volume_envelope();
  envelope->set_sustain(sustain);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetReleaseToVolumeEnvelope
   (JNIEnv* env, jclass obj, jlong release)
{
  synth::Envelope* envelope = engine_->get_controller()->volume_envelope();
  envelope->set_release(release);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetAttackToFilterEnvelope
   (JNIEnv* env, jclass obj, jlong attack)
{
  synth::Envelope* envelope = engine_->get_controller()->filter_envelope();
  envelope->set_attack(attack);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetDecayToFilterEnvelope
   (JNIEnv* env, jclass obj, jlong decay)
{
  synth::Envelope* envelope = engine_->get_controller()->filter_envelope();
  envelope->set_decay(decay);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetSustainToFilterEnvelope
   (JNIEnv* env, jclass obj, jfloat sustain)
{
  synth::Envelope* envelope = engine_->get_controller()->filter_envelope();
  envelope->set_sustain(sustain);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetReleaseToFilterEnvelope
   (JNIEnv* env, jclass obj, jlong release)
{
  synth::Envelope* envelope = engine_->get_controller()->filter_envelope();
  envelope->set_release(release);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioEnabled
   (JNIEnv* env, jclass obj, jboolean enabled)
{
  engine_->get_controller()->set_arpeggio_enabled(enabled);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioOctaves
   (JNIEnv* env, jclass obj, jint octaves)
{
  engine_->get_controller()->set_arpeggio_octaves(octaves);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioSamples
   (JNIEnv* env, jclass obj, jlong samples)
{
  engine_->get_controller()->set_arpeggio_samples(samples);
}

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioStep
   (JNIEnv* env, jclass obj, jint step)
{
  engine_->get_controller()->set_arpeggio_step(static_cast<Arpeggio::Step>(step));
}

