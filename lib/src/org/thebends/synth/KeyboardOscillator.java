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
 * Groups logic related to running the oscillators from keyboard input.
 */
public class KeyboardOscillator implements Parameter {
    private final float OCTAVE_CENTS = 1200;

    private Parameter mBaseFrequency;

    private double mOsc1Octave;
    private double mOsc2Octave;
    private double mOsc1Level;
    private double mOsc2Level;
    private double mOsc2Shift;

    private MutableParameter mOsc1Freq;
    private MutableParameter mOsc2Freq;
    private Parameter mFrequencyModulation;

    private boolean mSync;

    private Oscillator mOsc1;
    private Oscillator mOsc2;

    public KeyboardOscillator(Oscillator osc1,
            Oscillator osc2,
            Parameter frequency) {
        mBaseFrequency = frequency;
        mOsc1Octave = 1;
        mOsc2Octave = 1;
        mOsc1Level = 0;
        mOsc2Level = 0;
        mOsc2Shift = 1.0f;
        mOsc1Freq = new MutableParameter();
        mOsc2Freq = new MutableParameter();
        mFrequencyModulation = null;
        mSync = false;
        mOsc1 = osc1;
        mOsc2 = osc2;

        mOsc1.setFrequency(mOsc1Freq);
        mOsc2.setFrequency(mOsc2Freq);
    }

    /**
     * Multiple to the specified octave
     */
    public void setOsc1Octave(double multiply) {
        mOsc1Octave = multiply;
    }

    public void setOsc2Octave(double multiply) {
        mOsc2Octave = multiply;
    }

    public void setOsc1Level(double level) {
        mOsc1Level = level;
    }

    public void setOsc2Level(double level) {
        mOsc2Level = level;
    }

    /**
     * Number of cents to shift osc2
     */
    public void setOsc2Shift(int cents) {
        if (cents == 0) {
            mOsc2Shift = 1.0f;
        } else {
            mOsc2Shift = Math.pow(2.0f, cents / OCTAVE_CENTS);
        }
    }

    /**
     * Sync osc2 to osc1 (master)
     */
    public void setOscSync(boolean sync) {
        mSync = sync;
    }

    /**
     * Can be NUL to disable frequency modulation, otherwise a multiplier of the
     * current frequency intended to change over time.
     */
    public void setFrequencyModulation(Parameter parameter) {
        mFrequencyModulation = parameter;
    }

    /**
     * Return the value of the combine oscillators
     */
    public double getValue() {
        // osc2 is a slave to osc1 when sync is enabled.
        if (mSync && mOsc1.isStart()) {
            mOsc2.reset();
        }
  
        double root_note = mBaseFrequency.getValue(); 
        if (mFrequencyModulation != null) {
            root_note *= mFrequencyModulation.getValue();
        }
        mOsc1Freq.setValue(root_note * mOsc1Octave);
        double mOsc2freq = root_note * mOsc2Octave;
        mOsc2freq *= mOsc2Shift;
        mOsc2Freq.setValue(mOsc2freq);

        double value = mOsc1Level * mOsc1.getValue() +
            mOsc2Level * mOsc2.getValue();
        // Clip
        value = Math.min(value, 1.0f);
        return Math.max(value, -1.0f);
    }
}
