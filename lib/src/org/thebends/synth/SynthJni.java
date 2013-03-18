/*
 * Copyright 2013 Daisuke Fuji <daisuke@indigo-lab.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thebends.synth;

public class SynthJni {
    /**
     * Loads the 'synth' library on application startup.  The library has already
     * been unpacked at installation time by the package manager.
     */
    static {
        System.loadLibrary("synth");
    }

    public static final int SAMPLE_RATE_HZ = 44100;

    public static final int WAVE_TYPE_SINE             = 0;
    public static final int WAVE_TYPE_SQUARE           = 1;
    public static final int WAVE_TYPE_TRIANGLE         = 2;
    public static final int WAVE_TYPE_SAWTOOTH         = 3;
    public static final int WAVE_TYPE_REVERSE_SAWTOOTH = 4;

    public static final int OCTAVE_SHIFT_1  = 1;
    public static final int OCTAVE_SHIFT_2  = 2;
    public static final int OCTAVE_SHIFT_4  = 4;
    public static final int OCTAVE_SHIFT_8  = 8;
    public static final int OCTAVE_SHIFT_16 = 16;

    public static final int MODULATION_SOURCE_LFO_SRC_SQUARE            = 0;
    public static final int MODULATION_SOURCE_LFO_SRC_TRIANGLE          = 1;
    public static final int MODULATION_SOURCE_LFO_SRC_SAWTOOTH          = 2;
    public static final int MODULATION_SOURCE_LFO_SRC_REVERSE_SAWTOOTH  = 3;

    public static final int MODULATION_DESTINATION_LFO_DEST_WAVE   = 0;
    public static final int MODULATION_DESTINATION_LFO_DEST_PITCH  = 1;
    public static final int MODULATION_DESTINATION_LFO_DEST_FILTER = 2;

    public static final int STEP_UP      = 0;
    public static final int STEP_DOWN    = 1;
    public static final int STEP_UP_DOWN = 2;
    public static final int STEP_RANDOM  = 3;
    
    public static native int nativeStart();
    public static native int nativeShutdown();

    public static native int nativeNoteOn(int note);
    public static native int nativeNoteOff(int note);

    public static native void nativeSetOSC1Level(float level);
    public static native void nativeSetOSC1WaveType(int waveType);
    public static native void nativeSetOSC1Octave(int octave);

    public static native void nativeSetOSC2Level(float level);
    public static native void nativeSetOSC2WaveType(int waveType);
    public static native void nativeSetOSC2Octave(int octave);

    public static native void nativeSetGlideSamples(long samples);

    public static native void nativeSetOsc2Shift(int cents);
    public static native void nativeSetOscSync(boolean sync);

    public static native void nativeSetModulationAmount(float amount);
    public static native void nativeSetModulationFrequency(float frequency);
    public static native void nativeSetModulationSource(int modulationSource);
    public static native void nativeSetModulationDestination(int modulationDestination);

    public static native void nativeSetFilterCutoff(float frequency);
    public static native void nativeSetFilterResonance(float value);

    public static native void nativeSetAttackToVolumeEnvelope(long attack);
    public static native void nativeSetDecayToVolumeEnvelope(long decay);
    public static native void nativeSetSustainToVolumeEnvelope(float sustain);
    public static native void nativeSetReleaseToVolumeEnvelope(long release);

    public static native void nativeSetAttackToFilterEnvelope(long attack);
    public static native void nativeSetDecayToFilterEnvelope(long decay);
    public static native void nativeSetSustainToFilterEnvelope(float sustain);
    public static native void nativeSetReleaseToFilterEnvelope(long release);

    public static native void nativeSetArpeggioEnabled(boolean enabled);
    public static native void nativeSetArpeggioOctaves(int octaves);
    public static native void nativeSetArpeggioSamples(int samples);
    public static native void nativeSetArpeggioStep(int step);
};
