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
 * An immutable parameter that always returns the same fixed value.  This is
 * mostly used for testing other components with a simple parameter.  New
 * instances are obtained from the static {@link #get(double)} method.
 */
public final class FixedParameter implements Parameter {
    private final double mValue;

    public FixedParameter(double value) {
        this.mValue = value;
    }

    /**
     * @return a Parameter that always returns the specified value.
     */
    public static Parameter get(double value) {
        return new FixedParameter(value);
    }

    @Override
    public double getValue() {
        return mValue;
    }
}
