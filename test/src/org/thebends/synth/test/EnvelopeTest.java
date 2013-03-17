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

import org.thebends.synth.Envelope;

import junit.framework.TestCase;

public class EnvelopeTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFlat() {
        Envelope env = new Envelope();
        env.noteOn();
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, env.getValue());
        }
        env.noteOff();
        assertEquals(0.0, env.getValue());
    }

    public void testZero() {
        Envelope env = new Envelope();
        env.setAttack(0);
        env.setDecay(0);
        env.setSustain(0);
        env.setRelease(0);
        env.noteOn();
        for (int i = 0; i < 10; ++i) { 
            assertEquals(0.0, env.getValue());
        }
        env.noteOff();
        assertEquals(0.0, env.getValue());
    }

    public void testCurve() {
        Envelope env = new Envelope();
        env.setAttack(4);
        env.setDecay(4);
        env.setSustain(0.45);
        env.setRelease(8);
        env.noteOn();
        // Attack
        assertEquals(0.25, env.getValue());
        assertEquals(0.5, env.getValue());
        assertEquals(0.75, env.getValue());
        assertEquals(1.0, env.getValue());
        // Decay
        assertEquals(0.8625, env.getValue());
        assertEquals(0.725, env.getValue());
        assertEquals(0.5875, env.getValue(), 1E-15);
        // Sustain
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.45, env.getValue());
        }
        env.noteOff();
        assertEquals(0.39375, env.getValue());
        assertEquals(0.3375, env.getValue());
        assertEquals(0.28125, env.getValue());
        assertEquals(0.225, env.getValue());
        assertEquals(0.16875, env.getValue());
        assertEquals(0.1125, env.getValue(), 1E-16);
        assertEquals(0.05625, env.getValue(), 1E-16);
        // Done
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, env.getValue());
        }
    }

    public void testAttackRelease() {
        Envelope env = new Envelope();
        env.setAttack(4);
        env.setDecay(0);
        env.setSustain(0.99);        // ignored since we never reach it
        env.setRelease(3);
        env.noteOn();
        // Attack
        assertEquals(0.25, env.getValue());
        assertEquals(0.5, env.getValue());
        assertEquals(0.75, env.getValue());
        env.noteOff();
        // Released before we finished attacking.        Release from the current value
        assertEquals(0.5, env.getValue());
        assertEquals(0.25, env.getValue());
        assertEquals(0.0, env.getValue());
    }

    public void testDecay() {
        Envelope env = new Envelope();
        env.setAttack(0);
        env.setDecay(5);
        env.setSustain(0.0);
        env.setRelease(8);
        env.noteOn();
        assertEquals(0.8, env.getValue());
        assertEquals(0.6, env.getValue());
        assertEquals(0.4, env.getValue(), 1E-15);
        assertEquals(0.2, env.getValue(), 1E-16);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, env.getValue());
        }
        env.noteOff();
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, env.getValue());
        }
    }

    public void testDecayRelease() {
        Envelope env = new Envelope();
        env.setAttack(4);
        env.setDecay(4);
        env.setSustain(0.5);        // ignored since we never reach it
        env.setRelease(8);
        env.noteOn();
        // Attack
        assertEquals(0.25, env.getValue());
        assertEquals(0.5, env.getValue());
        assertEquals(0.75, env.getValue());
        assertEquals(1.0, env.getValue());
        // Decay
        assertEquals(0.875, env.getValue());
        assertEquals(0.75, env.getValue());
        env.noteOff();
        // Released before we finished decaying.        Release starts from the decay point.
        assertEquals(0.65625, env.getValue());
        assertEquals(0.5625, env.getValue());
        assertEquals(0.46875, env.getValue());
        assertEquals(0.375, env.getValue());
        assertEquals(0.28125, env.getValue());
        assertEquals(0.1875, env.getValue());
        assertEquals(0.09375, env.getValue());
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, env.getValue());
        }
    }
}
