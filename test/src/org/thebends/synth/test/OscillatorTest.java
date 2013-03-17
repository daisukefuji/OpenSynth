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
import org.thebends.synth.Oscillator;

import junit.framework.TestCase;

public class OscillatorTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSine() {
        FixedParameter freq = new FixedParameter(1.0);  // one cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(4);
        osc.setWaveForm(Oscillator.WaveForm.SINE);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, osc.getValue());
            assertEquals(1.0, osc.getValue());
            assertEquals(Math.sin(Math.toRadians(180)), osc.getValue());
            assertEquals(-1.0, osc.getValue());
        }
    }

    public void testSquare() {
        FixedParameter freq = new FixedParameter(1.0);  // one cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(7);
        osc.setWaveForm(Oscillator.WaveForm.SQUARE);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, osc.getValue());
            assertEquals(1.0, osc.getValue());
            assertEquals(1.0, osc.getValue());
            assertEquals(-1.0, osc.getValue());
            assertEquals(-1.0, osc.getValue());
            assertEquals(-1.0, osc.getValue());
            assertEquals(-1.0, osc.getValue());
        }
    }

    public void testSawtooth() {
        FixedParameter freq = new FixedParameter(1.0);  // one cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(8);
        osc.setWaveForm(Oscillator.WaveForm.SAWTOOTH);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(-1.0, osc.getValue());
            assertEquals(-0.75, osc.getValue());
            assertEquals(-0.5, osc.getValue());
            assertEquals(-0.25, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(0.25, osc.getValue());
            assertEquals(0.5, osc.getValue());
            assertEquals(0.75, osc.getValue());
        }
    }

    public void testSawtooth2() {
        FixedParameter freq = new FixedParameter(2.0);  // two cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(8);
        osc.setWaveForm(Oscillator.WaveForm.SAWTOOTH);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(-1.0, osc.getValue());
            assertEquals(-0.5, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(0.5, osc.getValue());
        }
    }

    public void testReverseSawtooth() {
        FixedParameter freq = new FixedParameter(1.0);  // one cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(8);
        osc.setWaveForm(Oscillator.WaveForm.REVERSE_SAWTOOTH);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, osc.getValue());
            assertEquals(0.75, osc.getValue());
            assertEquals(0.5, osc.getValue());
            assertEquals(0.25, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(-0.25, osc.getValue());
            assertEquals(-0.5, osc.getValue());
            assertEquals(-0.75, osc.getValue());
        }
    }

    public void testTriangle() {
        FixedParameter freq = new FixedParameter(1.0);  // one cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(8);
        osc.setWaveForm(Oscillator.WaveForm.TRIANGLE);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, osc.getValue());
            assertEquals(0.5, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(-0.5, osc.getValue());
            assertEquals(-1.0, osc.getValue());
            assertEquals(-0.5, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(0.5, osc.getValue());
        }
    }

    public void testTriangle2() {
        FixedParameter freq = new FixedParameter(2.0);  // two cycle per second
        Oscillator osc = new Oscillator();
        osc.setSampleRate(8);
        osc.setWaveForm(Oscillator.WaveForm.TRIANGLE);
        osc.setFrequency(freq);

        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, osc.getValue());
            assertEquals(0.0, osc.getValue());
            assertEquals(-1.0, osc.getValue());
            assertEquals(0.0, osc.getValue());
        }
    }
}