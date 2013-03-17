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
 * Interface for a filter based on some cutoff frequency (usually high or low
 * pass).  The input value is a sample and the output value is a new sample
 * at that time.
 */
public abstract class Filter {
    protected static final float kE = 2.71828183f;
    protected Parameter mCutoff;

    // The cutoff frequency of the filter.
    public void setCutoff(Parameter cutoff) {
        mCutoff = cutoff;
    }

    public abstract double getValue(double x);
}

