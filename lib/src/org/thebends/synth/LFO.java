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
 * A parameter that implements modulation via a low frequency oscillator.
 */
public class LFO implements Parameter {
    private Parameter mLevel;
    private Parameter mOscillator;

    public LFO() {
        this(null, null);
    }

    public LFO(Parameter oscillator, Parameter level) {
        mOscillator = oscillator;
        mLevel = level;
    }

    public void setLevel(Parameter level) {
        mLevel = level;
    }

    public void setOscillator(Parameter oscillator) {
        mOscillator = oscillator;
    }

    @Override
    public double getValue() {
        double m = 0.5 * mLevel.getValue();
        double b = 1.0 - m;
        return m * mOscillator.getValue() + b;
    }
}
