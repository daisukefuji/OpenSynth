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

import android.content.SharedPreferences;

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
    
    private static native int nativeStart();
    private static native int nativeShutdown();

    private static native int nativeNoteOn(int note);
    private static native int nativeNoteOff(int note);

    private static native void nativeSetOSC1Level(float level);
    private static native void nativeSetOSC1WaveType(int waveType);
    private static native void nativeSetOSC1Octave(int octave);

    private static native void nativeSetOSC2Level(float level);
    private static native void nativeSetOSC2WaveType(int waveType);
    private static native void nativeSetOSC2Octave(int octave);

    private static native void nativeSetGlideSamples(long samples);

    private static native void nativeSetOsc2Shift(int cents);
    private static native void nativeSetOscSync(boolean sync);

    private static native void nativeSetModulationAmount(float amount);
    private static native void nativeSetModulationFrequency(float frequency);
    private static native void nativeSetModulationSource(int modulationSource);
    private static native void nativeSetModulationDestination(int modulationDestination);

    private static native void nativeSetFilterCutoff(float frequency);
    private static native void nativeSetFilterResonance(float value);

    private static native void nativeSetAttackToVolumeEnvelope(long attack);
    private static native void nativeSetDecayToVolumeEnvelope(long decay);
    private static native void nativeSetSustainToVolumeEnvelope(float sustain);
    private static native void nativeSetReleaseToVolumeEnvelope(long release);

    private static native void nativeSetAttackToFilterEnvelope(long attack);
    private static native void nativeSetDecayToFilterEnvelope(long decay);
    private static native void nativeSetSustainToFilterEnvelope(float sustain);
    private static native void nativeSetReleaseToFilterEnvelope(long release);

    private static native void nativeSetArpeggioEnabled(boolean enabled);
    private static native void nativeSetArpeggioOctaves(int octaves);
    private static native void nativeSetArpeggioSamples(int samples);
    private static native void nativeSetArpeggioStep(int step);

    private static float sOSC1Level;
    private static int sOSC1WaveType;
    private static int sOSC1Octave;
    private static float sOSC2Level;
    private static int sOSC2WaveType;
    private static int sOSC2Octave;
    private static long sGlideSamples;
    private static int sOsc2Shift;
    private static boolean sOscSync;
    private static float sModulationAmount;
    private static float sModulationFrequency;
    private static int sModulationSource;
    private static int sModulationDestination;
    private static float sFilterCutoff;
    private static float sFilterResonance;
    private static long sAttackToVolumeEnvelope;
    private static long sDecayToVolumeEnvelope;
    private static float sSustainToVolumeEnvelope;
    private static long sReleaseToVolumeEnvelope;
    private static long sAttackToFilterEnvelope;
    private static long sDecayToFilterEnvelope;
    private static float sSustainToFilterEnvelope;
    private static long sReleaseToFilterEnvelope;
    private static boolean sArpeggioEnabled;
    private static int sArpeggioOctaves;
    private static int sArpeggioSamples;
    private static int sArpeggioStep;

    public static void restoreSettings(SharedPreferences state) {
        setOSC1Level(state.getFloat("sOSC1Level", 0.5f));
        setOSC1WaveType(state.getInt("sOSC1WaveType", WAVE_TYPE_SQUARE));
        setOSC1Octave(state.getInt("sOSC1Octave", OCTAVE_SHIFT_1));
        setOSC2Level(state.getFloat("sOSC2Level", 0.5f));
        setOSC2WaveType(state.getInt("sOSC2WaveType", WAVE_TYPE_SQUARE));
        setOSC2Octave(state.getInt("sOSC2Octave", OCTAVE_SHIFT_1));
        setGlideSamples(state.getLong("sGlideSamples", 0));
        setOsc2Shift(state.getInt("sOsc2Shift", 0));
        setOscSync(state.getBoolean("sOscSync", false));
        setModulationAmount(state.getFloat("sModulationAmount", 0));
        setModulationFrequency(state.getFloat("sModulationFrequency", 0));
        setModulationSource(state.getInt("sModulationSource", MODULATION_SOURCE_LFO_SRC_SQUARE));
        setModulationDestination(state.getInt("sModulationDestination", MODULATION_DESTINATION_LFO_DEST_WAVE));
        setFilterCutoff(state.getFloat("sFilterCutoff", -1f));
        setFilterResonance(state.getFloat("sFilterResonance", 0));
        setAttackToVolumeEnvelope(state.getLong("sAttackToVolumeEnvelope", 0));
        setDecayToVolumeEnvelope(state.getLong("sDecayToVolumeEnvelope", 0));
        setSustainToVolumeEnvelope(state.getFloat("sSustainToVolumeEnvelope", 1f));
        setReleaseToVolumeEnvelope(state.getLong("sReleaseToVolumeEnvelope", 0));
        setAttackToFilterEnvelope(state.getLong("sAttackToFilterEnvelope", 0));
        setDecayToFilterEnvelope(state.getLong("sDecayToFilterEnvelope", 0));
        setSustainToFilterEnvelope(state.getFloat("sSustainToFilterEnvelope", 1f));
        setReleaseToFilterEnvelope(state.getLong("sReleaseToFilterEnvelope", 0));
        setArpeggioEnabled(state.getBoolean("sArpeggioEnabled", false));
        setArpeggioOctaves(state.getInt("sArpeggioOctaves", 1));
        setArpeggioSamples(state.getInt("sArpeggioSamples", SAMPLE_RATE_HZ));
        setArpeggioStep(state.getInt("sArpeggioStep", STEP_UP));
    }

    public static void saveSettings(SharedPreferences.Editor state) {
        state.putFloat("sOSC1Level", sOSC1Level);
        state.putInt("sOSC1WaveType", sOSC1WaveType);
        state.putInt("sOSC1Octave", sOSC1Octave);
        state.putFloat("sOSC2Level", sOSC2Level);
        state.putInt("sOSC2WaveType", sOSC2WaveType);
        state.putInt("sOSC2Octave", sOSC2Octave);
        state.putLong("sGlideSamples", sGlideSamples);
        state.putInt("sOsc2Shift", sOsc2Shift);
        state.putBoolean("sOscSync", sOscSync);
        state.putFloat("sModulationAmount", sModulationAmount);
        state.putFloat("sModulationFrequency", sModulationFrequency);
        state.putInt("sModulationSource", sModulationSource);
        state.putInt("sModulationDestination", sModulationDestination);
        state.putFloat("sFilterCutoff", sFilterCutoff);
        state.putFloat("sFilterResonance", sFilterResonance);
        state.putLong("sAttackToVolumeEnvelope", sAttackToVolumeEnvelope);
        state.putLong("sDecayToVolumeEnvelope", sDecayToVolumeEnvelope);
        state.putFloat("sSustainToVolumeEnvelope", sSustainToVolumeEnvelope);
        state.putLong("sReleaseToVolumeEnvelope", sReleaseToVolumeEnvelope);
        state.putLong("sAttackToFilterEnvelope", sAttackToFilterEnvelope);
        state.putLong("sDecayToFilterEnvelope", sDecayToFilterEnvelope);
        state.putFloat("sSustainToFilterEnvelope", sSustainToFilterEnvelope);
        state.putLong("sReleaseToFilterEnvelope", sReleaseToFilterEnvelope);
        state.putBoolean("sArpeggioEnabled", sArpeggioEnabled);
        state.putInt("sArpeggioOctaves", sArpeggioOctaves);
        state.putInt("sArpeggioSamples", sArpeggioSamples);
        state.putInt("sArpeggioStep", sArpeggioStep);
    }

    public static int start() {
        return nativeStart();
    }
    public static int shutdown() {
        return nativeShutdown();
    }

    public static int noteOn(int note) {
        return nativeNoteOn(note);
    }
    public static int noteOff(int note) {
        return nativeNoteOff(note);
    }

    public static float getOSC1Level() {
        return sOSC1Level;
    }
    public static void setOSC1Level(float level) {
        sOSC1Level = level;
        nativeSetOSC1Level(level);
    }
    public static int getOSC1WaveType() {
        return sOSC1WaveType;
    }
    public static void setOSC1WaveType(int waveType) {
        sOSC1WaveType = waveType;
        nativeSetOSC1WaveType(waveType);
    }
    public static int getOSC1Octave() {
        return sOSC1Octave;
    }
    public static void setOSC1Octave(int octave) {
        sOSC1Octave = octave;
        nativeSetOSC1Octave(octave);
    }

    public static float getOSC2Level() {
        return sOSC2Level;
    }
    public static void setOSC2Level(float level) {
        sOSC2Level = level;
        nativeSetOSC2Level(level);
    }
    public static int getOSC2WaveType() {
        return sOSC2WaveType;
    }
    public static void setOSC2WaveType(int waveType) {
        sOSC2WaveType = waveType;
        nativeSetOSC2WaveType(waveType);
    }
    public static int getOSC2Octave() {
        return sOSC2Octave;
    }
    public static void setOSC2Octave(int octave) {
        sOSC2Octave = octave;
        nativeSetOSC2Octave(octave);
    }

    public static long getGlideSamples() {
        return sGlideSamples;
    }
    public static void setGlideSamples(long samples) {
        sGlideSamples = samples;
        nativeSetGlideSamples(samples);
    }

    public static int getOsc2Shift() {
        return sOsc2Shift;
    }
    public static void setOsc2Shift(int cents) {
        sOsc2Shift = cents;
        nativeSetOsc2Shift(cents);
    }
    public static boolean getOscSync() {
        return sOscSync;
    }
    public static void setOscSync(boolean sync) {
        sOscSync = sync;
        nativeSetOscSync(sync);
    }

    public static float getModulationAmount() {
        return sModulationAmount;
    }
    public static void setModulationAmount(float amount) {
        sModulationAmount = amount;
        nativeSetModulationAmount(amount);
    }
    public static float getModulationFrequency() {
        return sModulationFrequency;
    }
    public static void setModulationFrequency(float frequency) {
        sModulationFrequency = frequency;
        nativeSetModulationFrequency(frequency);
    }
    public static int setModulationSource() {
        return sModulationSource;
    }
    public static void setModulationSource(int modulationSource) {
        sModulationSource = modulationSource;
        nativeSetModulationSource(modulationSource);
    }
    public static int getModulationDestination() {
        return sModulationDestination;
    }
    public static void setModulationDestination(int modulationDestination) {
        sModulationDestination = modulationDestination;
        nativeSetModulationDestination(modulationDestination);
    }

    public static float getFilterCutoff() {
        return sFilterCutoff;
    }
    public static void setFilterCutoff(float frequency) {
        sFilterCutoff = frequency;
        nativeSetFilterCutoff(frequency);
    }
    public static float getFilterResonance() {
        return sFilterResonance;
    }
    public static void setFilterResonance(float value) {
        sFilterResonance = value;
        nativeSetFilterResonance(value);
    }

    public static long getAttackToVolumeEnvelope() {
        return sAttackToVolumeEnvelope;
    }
    public static void setAttackToVolumeEnvelope(long attack) {
        sAttackToVolumeEnvelope = attack;
        nativeSetAttackToVolumeEnvelope(attack);
    }
    public static long getDecayToVolumeEnvelope() {
        return sDecayToVolumeEnvelope;
    }
    public static void setDecayToVolumeEnvelope(long decay) {
        sDecayToVolumeEnvelope = decay;
        nativeSetDecayToVolumeEnvelope(decay);
    }
    public static float getSustainToVolumeEnvelope() {
        return sSustainToVolumeEnvelope;
    }
    public static void setSustainToVolumeEnvelope(float sustain) {
        sSustainToVolumeEnvelope = sustain;
        nativeSetSustainToVolumeEnvelope(sustain);
    }
    public static long getReleaseToVolumeEnvelope() {
        return sReleaseToVolumeEnvelope;
    }
    public static void setReleaseToVolumeEnvelope(long release) {
        sReleaseToVolumeEnvelope = release;
        nativeSetReleaseToVolumeEnvelope(release);
    }

    public static long getAttackToFilterEnvelope() {
        return sAttackToFilterEnvelope;
    }
    public static void setAttackToFilterEnvelope(long attack) {
        sAttackToFilterEnvelope = attack;
        nativeSetAttackToFilterEnvelope(attack);
    }
    public static long sDecayToFilterEnvelope() {
        return sDecayToFilterEnvelope;
    }
    public static void setDecayToFilterEnvelope(long decay) {
        sDecayToFilterEnvelope = decay;
        nativeSetDecayToFilterEnvelope(decay);
    }
    public static float getSustainToFilterEnvelope() {
        return sSustainToFilterEnvelope;
    }
    public static void setSustainToFilterEnvelope(float sustain) {
        sSustainToFilterEnvelope = sustain;
        nativeSetSustainToFilterEnvelope(sustain);
    }
    public static long getReleaseToFilterEnvelope() {
        return sReleaseToFilterEnvelope;
    }
    public static void setReleaseToFilterEnvelope(long release) {
        sReleaseToFilterEnvelope = release;
        nativeSetReleaseToFilterEnvelope(release);
    }

    public static boolean getArpeggioEnabled() {
        return sArpeggioEnabled;
    }
    public static void setArpeggioEnabled(boolean enabled) {
        sArpeggioEnabled = enabled;
        nativeSetArpeggioEnabled(enabled);
    }
    public static int getArpeggioOctaves() {
        return sArpeggioOctaves;
    }
    public static void setArpeggioOctaves(int octave) {
        sArpeggioOctaves = octave;
        nativeSetArpeggioOctaves(octave);
    }
    public static int getArpeggioSamples() {
        return sArpeggioSamples;
    }
    public static void setArpeggioSamples(int samples) {
        sArpeggioSamples = samples;
        nativeSetArpeggioSamples(samples);
    }
    public static int getArpeggioStep() {
        return sArpeggioStep;
    }
    public static void setArpeggioStep(int step) {
        sArpeggioStep = step;
        nativeSetArpeggioStep(step);
    }
};
