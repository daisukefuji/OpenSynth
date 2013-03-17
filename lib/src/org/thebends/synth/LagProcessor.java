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
 * A lag processor, used to implement glide.
 */
public class LagProcessor implements Parameter {
    private Parameter mParam;
    private final Envelope mEnvelope = new Envelope();
    private long mSamplesUp;
    private long mSamplesDown;
    private boolean mHasLastValue;
    private double mLastValue;

    /**
     * @mParam mParam the mParameter to glide.
     */
    public LagProcessor(Parameter mParam) {
        this.mParam = mParam;
        mSamplesUp = 0;
        mSamplesDown = 0;
        mHasLastValue = false;
        mLastValue = 0.0;
    }

    public void setParam(Parameter mParam) {
            this.mParam = mParam;
    }

    public void setSamples(long samples) {
        setSamplesUp(samples);
        setSamplesDown(samples);
    }

    public void setSamplesUp(long samples) {
        mSamplesUp = samples;
    }

    public void setSamplesDown(long samples) {
        mSamplesDown = samples;
    }

    public void reset() {
        mHasLastValue = false;
    }

    @Override
    public double getValue() {
        double value = mParam.getValue();
        if (!mHasLastValue || mLastValue != value) {
            double diff = Math.abs(mLastValue - value);
            if (!mHasLastValue) {
                // No previous value so the mEnvelope simply ways returns the current
                // value in the sustain state.
                mEnvelope.setMin(0.0);
                mEnvelope.setMax(value);
                mEnvelope.setAttack(0);
                mEnvelope.setDecay(0);
                mEnvelope.setSustain(value);
                mEnvelope.setRelease(0);
                mEnvelope.noteOn();
            } else if (mLastValue < value) {
                // Slope up
                mEnvelope.setMin(mLastValue);
                mEnvelope.setMax(value);
                mEnvelope.setAttack((long) (mSamplesUp * diff));
                mEnvelope.setDecay(0);
                mEnvelope.setSustain(value);
                mEnvelope.setRelease(0);
                mEnvelope.noteOn();
            } else {
                // Slope down
                mEnvelope.setMax(mLastValue);
                mEnvelope.setMin(value);
                mEnvelope.setAttack(0);
                mEnvelope.setDecay(0);
                mEnvelope.setSustain(mLastValue);
                mEnvelope.setRelease((long) (mSamplesDown * diff));
                mEnvelope.noteOn();
                mEnvelope.noteOff();
            }
            mLastValue = value;
            mHasLastValue = true;
        }

        return mEnvelope.getValue();
    }
}
