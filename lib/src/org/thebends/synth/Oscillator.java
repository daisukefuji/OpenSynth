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

public final class Oscillator implements Parameter {
    public enum WaveForm {
        SINE,
        SQUARE,
        TRIANGLE,
        SAWTOOTH,
        REVERSE_SAWTOOTH,
    };

    private Parameter mFreqParam;
    private double mSampleRate;
    private WaveForm mWaveForm = WaveForm.SQUARE;
    private long mCurrentSample = 0;

    public Oscillator() {
        this(null, SynthTrack.SAMPLE_RATE_HZ);
        mWaveForm = WaveForm.SINE;
    }

    public Oscillator(Parameter freqParam,
                                        double mSampleRate) {
        this.mFreqParam = freqParam;
        this.mSampleRate = mSampleRate;
    }

    public void setSampleRate(double sampleRate) {
            mSampleRate = sampleRate;
    }

    public void setWaveForm(WaveForm mWaveForm) {
        this.mWaveForm = mWaveForm;
    }

    public void setFrequency(Parameter freq) {
        mFreqParam = freq;
    }

    @Override
    public double getValue() {
        double freq = mFreqParam.getValue();
        long periodSamples = (long) (mSampleRate / freq);
        double x = (double)mCurrentSample / periodSamples;
        double value = 0;
        switch (mWaveForm) {
            case SINE:
                value = Math.sin(2.0f * Math.PI * x);
                break;
            case SQUARE:
                if (mCurrentSample < (periodSamples / 2)) {
                    value = 1.0f;
                } else {
                    value = -1.0f;
                }
                break;
            case TRIANGLE:
                value = (2.f * Math.abs(2.0 * x - 2.0 * Math.floor(x) - 1.0f) - 1.0f);
                break;
            case SAWTOOTH:
                value = 2.0f * (x - Math.floor(x) - 0.5f);
                break;
            case REVERSE_SAWTOOTH:
                value = 2.0f * (Math.floor(x) - x + 0.5f);
                break;
            default:
                throw new RuntimeException("Illegal wave type: " + mWaveForm);
        }
        mCurrentSample = (mCurrentSample + 1) % (periodSamples);
        return value;
    }

    // Start at the beginning of the period
    public void reset() {
        mCurrentSample = 0;
    }

    // Returns true if this is the first sample in the period
    public boolean isStart() {
        return (mCurrentSample == 0);
    }
}