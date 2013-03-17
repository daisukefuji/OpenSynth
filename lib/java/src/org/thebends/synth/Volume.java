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

public class Volume implements Parameter {
    // base
    private double mLevel;
    private Envelope mEnvelope = new Envelope();
    private Parameter mModulation;

    public Volume() {
        mLevel = 1.0f;
        mModulation = null;
    }

    public double getValue() {
        double value = mLevel * mEnvelope.getValue();
        if (mModulation != null) {
            value *= mModulation.getValue();
        }
        return value;
    }

    public void setLevel(double level) {
        mLevel = level;
    }

    public void setModulation(Parameter param) {
        mModulation = param;
    }

    public Envelope envelope() {
        return mEnvelope;
    }
}

