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
 * Simple VCF
 */
public class ResonantFilter extends Filter {
    private float mResonance;

    private double mY1;
    private double mY2;
    private double mY3;
    private double mY4;
    private double mOldX;
    private double mOldY1;
    private double mOldY2;
    private double mOldY3;

    public ResonantFilter() {
        mCutoff = null;
        mResonance = 0.0f;
        mY1 = 0.0f;
        mY2 = 0.0f;
        mY3 = 0.0f;
        mY4 = 0.0f;
        mOldX = 0.0f;
        mOldY1 = 0.0f;
        mOldY2 = 0.0f;
        mOldY3 = 0.0f;
    }

    public double getValue(double x) {
        if (mCutoff == null) {
            return x;
        }
        double cutoff = mCutoff.getValue();
        double f = 2.0f * cutoff / SynthTrack.SAMPLE_RATE_HZ;
        double k = 3.6f * f - 1.6f * f * f - 1;
        double p = (k + 1.0f) * 0.5f;
        double scale = Math.pow(kE, (1.0f - p) * 1.386249);
        double r = mResonance * scale;

        double out = x - r * mY4;
        mY1 = out * p + mOldX * p - k * mY1;
        mY2 = mY1 * p + mOldY1 * p - k * mY2;
        mY3 = mY2 * p + mOldY2 * p - k * mY3;
        mY4 = mY3 * p + mOldY3 * p - k * mY4;
        mY4 = mY4 - Math.pow(mY4, 3.0f) / 6.0f;
        mOldX = out;
        mOldY1 = mY1;
        mOldY2 = mY2;
        mOldY3 = mY3;
        return out;
    }

    public void setResonance(float resonance) {
        mResonance = resonance;
    }
}
