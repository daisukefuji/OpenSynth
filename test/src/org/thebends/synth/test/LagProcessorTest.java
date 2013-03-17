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

import org.thebends.synth.LagProcessor;
import org.thebends.synth.MutableParameter;

import junit.framework.TestCase;

public class LagProcessorTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFlat() {
        MutableParameter param = new MutableParameter(1.0);
        LagProcessor glide = new LagProcessor(param);
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, glide.getValue());
        }
        // Rate has no effect when nothing changes
        glide.setSamples(10);
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, glide.getValue());
        }
    }

    public void testUpDown() {
        MutableParameter param = new MutableParameter(0.0);
        LagProcessor glide = new LagProcessor(param);
        glide.setSamplesUp(4);
        glide.setSamplesDown(10);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
        // Walk up over 5 samples
        param.setValue(2.0);

        assertEquals(0.25, glide.getValue());
        assertEquals(0.5, glide.getValue());
        assertEquals(0.75, glide.getValue());
        assertEquals(1.0, glide.getValue());
        assertEquals(1.25, glide.getValue());
        assertEquals(1.5, glide.getValue());
        assertEquals(1.75, glide.getValue());
        for (int i = 0; i < 10; ++i) {
            assertEquals(2.0, glide.getValue());
        }
        param.setValue(1.0);
        glide.setSamplesDown(0);
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, glide.getValue());
        }
        // Walk down over 10 samples
        param.setValue(0.0);
        glide.setSamplesDown(10);
        assertEquals(0.9, glide.getValue());
        assertEquals(0.8, glide.getValue());
        assertEquals(0.7, glide.getValue());
        assertEquals(0.6, glide.getValue());
        assertEquals(0.5, glide.getValue());
        assertEquals(0.4, glide.getValue(), 1E-2);
        assertEquals(0.3, glide.getValue(), 1E-2);
        assertEquals(0.2, glide.getValue(), 1E-2);
        assertEquals(0.1, glide.getValue(), 1E-2);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
    }

    public void testInterrupt() {
        MutableParameter param = new MutableParameter(0.0);
        LagProcessor glide = new LagProcessor(param);
        glide.setSamplesUp(5);
        glide.setSamplesDown(4);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
        // Walk up over 5 samples, but interrupt in the middle.
        param.setValue(1.0);
        assertEquals(0.2, glide.getValue());
        assertEquals(0.4, glide.getValue());
        param.setValue(0.0);
        assertEquals(0.3, glide.getValue(), 1E-16);
        assertEquals(0.2, glide.getValue());
        assertEquals(0.1, glide.getValue(), 1E-16);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
    }

    public void testImmediate() {
        MutableParameter param = new MutableParameter(0.0);
        LagProcessor glide = new LagProcessor(param);
        glide.setSamples(0);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
        param.setValue(1.0);
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, glide.getValue());
        }
    }

    public void testReset() {
        MutableParameter param = new MutableParameter(0.0);
        LagProcessor glide = new LagProcessor(param);
        glide.setSamples(2);
        for (int i = 0; i < 10; ++i) {
            assertEquals(0.0, glide.getValue());
        }
        // No glide happens after a reset
        glide.reset();
        param.setValue(1.0);
        for (int i = 0; i < 10; ++i) {
            assertEquals(1.0, glide.getValue());
        }
        param.setValue(2.0);
        assertEquals(1.5, glide.getValue());
        for (int i = 0; i < 10; ++i) {
            assertEquals(2.0, glide.getValue());
        }
        // No glide happens after a reset
        glide.reset();
        param.setValue(3.0);
        for (int i = 0; i < 10; ++i) {
            assertEquals(3.0, glide.getValue());
        }
        param.setValue(2.0);
        assertEquals(2.5, glide.getValue());
        for (int i = 0; i < 10; ++i) {
            assertEquals(2.0, glide.getValue());
        }
    }
}