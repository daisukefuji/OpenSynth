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

import org.thebends.synth.Arpeggio;
import org.thebends.synth.KeyStack;

import junit.framework.TestCase;

public class ArpeggioTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoKeysDown() {
        KeyStack keys = new KeyStack();
        Arpeggio arpeggio = new Arpeggio(keys);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 0);
        }
    }

    public void testOneKeyDown() {
        KeyStack keys = new KeyStack();
        Arpeggio arpeggio = new Arpeggio(keys);
        keys.noteOn(16);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 16);
        }
    }

    public void testUp() {
        KeyStack keys = new KeyStack();
        keys.noteOn(16);
        keys.noteOn(17);
        keys.noteOn(18);

        Arpeggio arpeggio = new Arpeggio(keys);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 18);
        }
    }

    public void testDown() {
        KeyStack keys = new KeyStack();
        keys.noteOn(16);
        keys.noteOn(17);
        keys.noteOn(18);

        Arpeggio arpeggio = new Arpeggio(keys);
        arpeggio.setStep(Arpeggio.Step.DOWN);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 18);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 16);
        }
    }

    public void testUpDown() {
        KeyStack keys = new KeyStack();
        keys.noteOn(16);
        keys.noteOn(17);
        keys.noteOn(18);

        Arpeggio arpeggio = new Arpeggio(keys);
        arpeggio.setStep(Arpeggio.Step.UP_DOWN);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 18);
            assertEquals(arpeggio.getNote(), 17);
        }
    }

    public void testOctaves() {
        KeyStack keys = new KeyStack();
        keys.noteOn(16);
        keys.noteOn(17);

        Arpeggio arpeggio = new Arpeggio(keys);
        arpeggio.setOctaves(2);
        arpeggio.setStep(Arpeggio.Step.UP);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 16 + 12);
            assertEquals(arpeggio.getNote(), 17 + 12);
        }
    }

    public void testSamplesPerNote() {
        KeyStack keys = new KeyStack();
        keys.noteOn(16);
        keys.noteOn(17);

        Arpeggio arpeggio = new Arpeggio(keys);
        arpeggio.setSamplesPerNote(3);
        arpeggio.setStep(Arpeggio.Step.UP);
        for (int i = 0; i < 10; ++i) {
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 16);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 17);
            assertEquals(arpeggio.getNote(), 17);
        }
    }
}
