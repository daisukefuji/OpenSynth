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
 *  A simple LowPassFilter FIR filter
 */
class LowPassFilter extends Filter {
    // Used to keep the last value of the frequency cutoff.  If it changes, we
    // need to re-initialize the filter co-efficients.
    private double mLastCutoff;

    // for input value x[k] and output y[k]
    private double mX1;  // input value x[k-1]
    private double mX2;  // input value x[k-2]
    private double mY1;  // output value y[k-1]
    private double mY2;  // output value y[k-2]

    // filter coefficients
    private double mA0;
    private double mA1;
    private double mA2;
    private double mB1;
    private double mB2;

    public LowPassFilter() {
        mCutoff = null;
        mLastCutoff = 0;
        mX1 = 0;
        mX2 = 0;
        mY1 = 0;
        mY2 = 0;
    }

    public double getValue(double x) {
        if (mCutoff == null) {
            return x;
        }

        // Re-initialize the filter co-efficients if they changed
        double cutoff = mCutoff.getValue();
        if (cutoff <= 0.0f) {
            return x;
        } else if (cutoff < 0.001f) {
            // Filtering all frequencies
            return 0.0f;
        }
        if (Math.abs(cutoff - mLastCutoff) > 0.001f) {
            reset(cutoff);
            mLastCutoff = cutoff;
        }

        double y = mA0 * x + mA1 * mX1 + mA2 *  mX2 + mB1 * mY1 + mB2 * mY2;
        mX1 = x;
        mX2 = mX1;
        mY2 = mY1;
        mY1 = y;
        return y;
    }

    public void reset(double frequency) {  
        // Number of filter passes
        float n = 1;

        // 3dB cutoff frequency
        double f0 = frequency;

        // Sample frequency
        final double fs = SynthTrack.SAMPLE_RATE_HZ;

        // 3dB cutoff correction
        double c = Math.pow(Math.pow(2, 1.0f / n) - 1, -0.25);

        // Polynomial coefficients
        double g = 1;
        double p = Math.sqrt(2.);

        // Corrected cutoff frequency
        double fp = c * (f0 / fs);

        // TODO(allen): We should probably do more filter passes so that we can ensure  // these stability constraints are met for sufficiently high frequencies.
        // Ensure fs is OK for stability constraint
        //assert fp > 0;
        //assert fp < 0.125;

        // Warp cutoff freq from analog to digital domain
        double w0 = Math.tan(Math.PI * fp);

        // Calculate the filter co-efficients
        double k1 = p * w0;
        double k2 = g * w0 * w0;

        mA0 = k2 / (1 + k1 + k2);
        mA1 = 2 * mA0;
        mA2 = mA0;
        mB1 = 2 * mA0 * (1 / k2 - 1);
        mB2 = 1 - (mA0 + mA1 + mA2 + mB1);
    }
}

