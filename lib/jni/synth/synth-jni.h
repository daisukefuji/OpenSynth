// synth-jni.h
// Daisuke Fuji <daisuke@indigo-lab.com>

#include <jni.h>
/* Header for class org_thebends_synth_SynthJni */

#ifndef _Included_org_thebends_synth_SynthJni
#define _Included_org_thebends_synth_SynthJni
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeStart
  (JNIEnv *, jclass);
JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeShutdown
  (JNIEnv *, jclass);

JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeNoteOn
  (JNIEnv *, jclass, jint);
JNIEXPORT jint JNICALL Java_org_thebends_synth_SynthJni_nativeNoteOff
  (JNIEnv *, jclass, jint);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1Level
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1WaveType
    (JNIEnv *, jclass, jint);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC1Octave
   (JNIEnv *, jclass, jint);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2Level
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2WaveType
    (JNIEnv *, jclass, jint);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOSC2Octave
   (JNIEnv *, jclass, jint);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetGlideSamples
   (JNIEnv *, jclass, jlong);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOsc2Shift
   (JNIEnv *, jclass, jint);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetOscSync
   (JNIEnv *, jclass, jboolean);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationAmount
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationFrequency
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationSource
   (JNIEnv *, jclass, jint);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetModulationDestination
   (JNIEnv *, jclass, jint);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetFilterCutoff
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetFilterResonance
   (JNIEnv *, jclass, jfloat);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetAttackToVolumeEnvelope
   (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetDecayToVolumeEnvelope
   (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetSustainToVolumeEnvelope
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetReleaseToVolumeEnvelope
   (JNIEnv *, jclass, jlong);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetAttackToFilterEnvelope
   (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetDecayToFilterEnvelope
   (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetSustainToFilterEnvelope
   (JNIEnv *, jclass, jfloat);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetReleaseToFilterEnvelope
   (JNIEnv *, jclass, jlong);

JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioEnabled
   (JNIEnv *, jclass, jboolean);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioOctaves
   (JNIEnv *, jclass, jint);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioSamples
   (JNIEnv *, jclass, jlong);
JNIEXPORT void JNICALL Java_org_thebends_synth_SynthJni_nativeSetArpeggioStep
   (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
