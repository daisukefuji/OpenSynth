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
package org.thebends.synth.test;

import org.thebends.synth.FixedParameter;
import org.thebends.synth.LFO;
import org.thebends.synth.MutableParameter;

import junit.framework.TestCase;

public class LFOTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoLevel() {
        FixedParameter osc = new FixedParameter(0.2);
        FixedParameter level = new FixedParameter(0.0);
        LFO mod = new LFO();
        mod.setOscillator(osc);
        mod.setLevel(level);
        assertEquals(1.0, mod.getValue()); 
    }

    public void testMaxLevel() {
        MutableParameter osc = new MutableParameter(0.2);
        FixedParameter level = new FixedParameter(1.0);

        LFO mod = new LFO();
        mod.setLevel(level);
        mod.setOscillator(osc);

        osc.setValue(-1);
        assertEquals(0.0, mod.getValue());
        osc.setValue(-0.5);
        assertEquals(0.25, mod.getValue());
        osc.setValue(0.0);
        assertEquals(0.5, mod.getValue());
        osc.setValue(0.5);
        assertEquals(0.75, mod.getValue());
        osc.setValue(1.0);
        assertEquals(1.0, mod.getValue());
    }

    public void testMidLevel() {
        MutableParameter osc = new MutableParameter(0.0);
        FixedParameter level = new FixedParameter(0.2);

        LFO mod = new LFO();
        mod.setLevel(level);
        mod.setOscillator(osc);

        osc.setValue(-1);
        assertEquals(0.8, mod.getValue());
        osc.setValue(-0.5);
        assertEquals(0.85, mod.getValue());
        osc.setValue(0.0);
        assertEquals(0.9, mod.getValue());
        osc.setValue(0.5);
        assertEquals(0.95, mod.getValue(), 1E-15);
        osc.setValue(1.0);
        assertEquals(1.0, mod.getValue());
    }
}
