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

/**
 * The controller module returns samples and drives the oscillator and envelope.
 */
public class Controller {
    // A shift in frequency by the specified amount.  The frequency gets
    // multiplied by 2^n
    public enum OctaveShift {
        OCTAVE_1(1),  // No shift
        OCTAVE_2(2),
        OCTAVE_4(4),
        OCTAVE_8(8),
        OCTAVE_16(16);

        private int mIntValue;

        OctaveShift(final int intValue) {
            mIntValue = intValue;
        }

        public int getIntValue() {
            return mIntValue;
        }
    };

    public enum ModulationSource {
        LFO_SRC_SQUARE,
        LFO_SRC_TRIANGLE,
        LFO_SRC_SAWTOOTH,
        LFO_SRC_REVERSE_SAWTOOTH,
        // TODO(allen): How do you determine the sustain length?
        // Is it sustain = period - (attack + decay + release)?
        //        LFO_FILTER_ENVELOPE,
        // TODO(allen): OSC2 needs a manual frequency control for this to work, i think.
        //        OSC2,
    };

    public enum ModulationDestination {
        LFO_DEST_WAVE,  // Tremelo
        LFO_DEST_PITCH,  // Vibrato
        LFO_DEST_FILTER,
        // TODO(allen): Is this Ring modulation?
        // LFO_DEST_OSC2,
    };

    private KeyStack mKeyStack = new KeyStack();
    private MutableParameter mKeyFrequency = new MutableParameter();

    private boolean mArpeggioEnabled;
    private Arpeggio mArpeggio;
    private LagProcessor mKeyLagProcessor;

    private Oscillator mOsc1 = new Oscillator();
    private Oscillator mOsc2 = new Oscillator();

    // The two oscillators combined
    private KeyboardOscillator mCombinedOsc;
    private Volume mVolume = new Volume();

    private ModulationSource mModulationSource;
    private ModulationDestination mModulationDestination;
    private MutableParameter mModulationFrequency;
    private Oscillator mModulationOsc = new Oscillator();
    private MutableParameter mModulationAmount;
    private LFO mModulation = new LFO();

    private FilterCutoff mFilterCutoff = new FilterCutoff();
    private LowPassFilter mLowpassFilter = new LowPassFilter();
    private ResonantFilter mResonantFilter = new ResonantFilter();

    public Controller() {
        mKeyFrequency = new MutableParameter();
        mArpeggioEnabled = false;
        mArpeggio = new Arpeggio(mKeyStack);
        mKeyLagProcessor = new LagProcessor(mArpeggio);
        mCombinedOsc = new KeyboardOscillator(mOsc1, mOsc2, mKeyLagProcessor);
        mModulationSource = ModulationSource.LFO_SRC_SQUARE;
        mModulationDestination = ModulationDestination.LFO_DEST_WAVE;
        mModulationFrequency = new MutableParameter();
        mModulationAmount = new MutableParameter();
        mModulationOsc.setFrequency(mModulationFrequency);
        mModulation.setOscillator(mModulationOsc);
        mModulation.setLevel(mModulationAmount);

        mLowpassFilter.setCutoff(mFilterCutoff);
        mResonantFilter.setCutoff(mFilterCutoff);

        resetRouting();
    }

    // Volume [0, 1.0]
    public void setVolume(float volume) {
        mVolume.setLevel(volume);
    }

    /**
     * Start/Stop playing a note.  These may trigger the Attack and Release of the
     * volume and filter envelopes, depending on the order of the on/off events.
     * It is an error to call NoteOff() for a note that was never the argument of
     * noteOn();
     */
    public void noteOn(int note) {
        assert note >= 1;
        assert note <= 88;
        mKeyStack.noteOn(note);
        if (mKeyStack.size() == 1) {
            // This is the first note played, so start attacking
            mKeyLagProcessor.reset();
            mArpeggio.reset();
            volumeEnvelope().noteOn();
            filterEnvelope().noteOn();
        }
        double frequency = KeyStack.toFrequency(mKeyStack.getCurrentNote());
        mKeyFrequency.setValue(frequency);
    }

    public void noteOff(int note) {
        mKeyStack.noteOff(note);
        if (mKeyStack.size() == 0) {
            // All notes were release, so start the release phase of the envelope
            noteOff();
        } else {
            // There are still notes on key stack -- switch!
            double frequency = KeyStack.toFrequency(mKeyStack.getCurrentNote());
            mKeyFrequency.setValue(frequency);
        }
    }

    /**
     * For testing
     */
    public void noteOnFrequency(float frequency) {
        mKeyFrequency.setValue(frequency);
        volumeEnvelope().noteOn();
        filterEnvelope().noteOn();
    }

    /**
     * Invoked when all notes have been released as a fallback
     */
    public void noteOff() {
        mKeyStack.clear();
        volumeEnvelope().noteOff();
        filterEnvelope().noteOff();
    }

    /**
     * True when nothing is playing
     */
    public boolean isReleased() {
        return (volumeEnvelope().isReleased() || filterEnvelope().isReleased());
    }

    /**
     * For testing
     */
    public void setSampleRate(float sample_rate) {
        mOsc1.setSampleRate(sample_rate);
        mOsc2.setSampleRate(sample_rate);
    }


    // OSC 1

    /**
     * Set the volume of oscillator
     */
    public void setOsc1Level(double level) {
        mCombinedOsc.setOsc1Level(level);
    }

    /**
     * Set the wave form of oscillator
     */
    public void setOsc1WaveForm(Oscillator.WaveForm waveForm) {
        mOsc1.setWaveForm(waveForm);
    }

    /**
     * The oscillator frequency is shifted by the specified amount.
     */
    public void setOsc1Octave(OctaveShift octave) {
        mCombinedOsc.setOsc1Octave(octave.getIntValue());
    }


    // OSC 2

    public void setOsc2Level(double level) {
        mCombinedOsc.setOsc2Level(level);
    }

    public void setOsc2WaveForm(Oscillator.WaveForm waveForm) {
        mOsc2.setWaveForm(waveForm);
    }

    public void setOsc2Octave(OctaveShift octave) {
        mCombinedOsc.setOsc2Octave(octave.getIntValue());
    }

    public void setOsc2Shift(int cents) {
        mCombinedOsc.setOsc2Shift(cents);
    }

    public void setOscSync(boolean sync) {
        mCombinedOsc.setOscSync(sync);
    }

    public Envelope volumeEnvelope() {
        return mVolume.envelope();
    }

    public Envelope filterEnvelope() {
        return mFilterCutoff.envelope();
    }

    public void setModulationSource(ModulationSource source) {
        mModulationSource = source;
        resetRouting();
    }

    public void setModulationDestination(ModulationDestination dest) {
        mModulationDestination = dest;
        resetRouting();
    }

    public void setModulationAmount(float amount) {
        mModulationAmount.setValue(amount);
    }

    public void setModulationFrequency(float frequency) {
        mModulationFrequency.setValue(frequency);
    }

    public void setFilterCutoff(float frequency) {
        mFilterCutoff.setCutoff(frequency);
    }

    /**
     *  [0.0, 1.0]
     */
    public void setFilterResonance(float value) {
        mResonantFilter.setResonance(value);
    }

    public void setGlideSamples(long samples) {
        mKeyLagProcessor.setSamples(samples);
    }

    public void setArpeggioEnabled(boolean enabled) {
        mArpeggioEnabled = enabled;
        resetRouting();
    }

    public void setArpeggioSamples(long samples) {
        mArpeggio.setSamplesPerNote(samples);
    }

    public void setArpeggioOctaves(int octaves) {
        mArpeggio.setOctaves(octaves);
    }

    public void setArpeggioStep(Arpeggio.Step step) {
        mArpeggio.setStep(step);
    }

    /**
     * Get a single sample
     */
    public double getSample() {
        if (volumeEnvelope().isReleased() || filterEnvelope().isReleased()) {
            return 0;
        }

        // Combined oscillators, volume/envelope/modulation
        double value = mCombinedOsc.getValue();
        // Clip!
        value = Math.max(-1.0f, value);
        value = Math.min(1.0f, value);
        // Combined filter with envelope/modulation
        value = mLowpassFilter.getValue(value);
        value = mResonantFilter.getValue(value);
        // Clip!
        value = Math.max(-1.0f, value);
        value = Math.min(1.0f, value);
        // Adjust volume
        value *= mVolume.getValue();
        return value;
    }

    public void getSamples(double[] buffer, int size) {
        for (int i = 0; i < size; ++i) {
            buffer[i] = getSample();
        }
    }

    public void getSamples(short[] buffer, int size) {
        for (int i = 0; i < size; ++i) {
            buffer[i] = (short)(getSample() * Short.MAX_VALUE);
        }
    }

    /*
     * Invoked when one of the routing parameters changes, such as the source
     * or destination of modulation.
     */
    private void resetRouting() {
        switch (mModulationSource) {
        case LFO_SRC_SQUARE:
            mModulationOsc.setWaveForm(Oscillator.WaveForm.SQUARE);
            break;
        case LFO_SRC_TRIANGLE:
            mModulationOsc.setWaveForm(Oscillator.WaveForm.TRIANGLE);
            break;
        case LFO_SRC_SAWTOOTH:
            mModulationOsc.setWaveForm(Oscillator.WaveForm.SAWTOOTH);
            break;
        case LFO_SRC_REVERSE_SAWTOOTH:
            mModulationOsc.setWaveForm(Oscillator.WaveForm.REVERSE_SAWTOOTH);
         break;
        default:
            assert false;
        }

        // Reset the destinations
        mVolume.setModulation(null);
        mFilterCutoff.setModulation(null);
        mCombinedOsc.setFrequencyModulation(null);

        // Route modulation into the correct pipeline
        switch (mModulationDestination) {
        case LFO_DEST_WAVE:
            // Modulate the volume (tremelo)
            mVolume.setModulation(mModulation);
            break;
        case LFO_DEST_PITCH:
            // Modulate the frequency (vibrato)
            mCombinedOsc.setFrequencyModulation(mModulation);
            break;
        case LFO_DEST_FILTER:
            // Modulate the cutoff frequency
            mFilterCutoff.setModulation(mModulation);
            break;
        default:
            assert false;
        }

        if (mArpeggioEnabled) {
            mKeyLagProcessor.setParam(mArpeggio);
        } else {
            mKeyLagProcessor.setParam(mKeyFrequency);
        }
    }
}
