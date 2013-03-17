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

import org.thebends.synth.KeyStack;

import junit.framework.TestCase;

public class KeyStackTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimple() {
        KeyStack stack = new KeyStack();
        assertEquals(0, stack.getCurrentNote());
        stack.noteOn(1);
        assertEquals(1, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOff(1);
        assertEquals(0, stack.getCurrentNote());
        assertEquals(0, stack.size());
    }

    public void testMultiple() {
        KeyStack stack = new KeyStack();
        assertEquals(0, stack.getCurrentNote());
        stack.noteOn(1);
        assertEquals(1, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOn(2);
        assertEquals(2, stack.getCurrentNote());
        assertEquals(2, stack.size());
        stack.noteOff(1);
        assertEquals(2, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOn(3);
        assertEquals(3, stack.getCurrentNote());
        assertEquals(2, stack.size());
        stack.noteOff(3);
        assertEquals(2, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOff(2);
        assertEquals(0, stack.getCurrentNote());
        assertEquals(0, stack.size());
        assertEquals(0, stack.getNote(0));
    }

    public void testDuplicates() {
        KeyStack stack = new KeyStack();
        stack.noteOn(1);
        assertEquals(1, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOn(2);
        assertEquals(2, stack.getCurrentNote());
        assertEquals(2, stack.size());
        stack.noteOn(1);
        assertEquals(2, stack.getCurrentNote());
        assertEquals(3, stack.size());
        stack.noteOff(2);
        assertEquals(1, stack.getCurrentNote());
        assertEquals(2, stack.size());
        stack.noteOff(1);
        assertEquals(1, stack.getCurrentNote());
        assertEquals(1, stack.size());
        stack.noteOff(1);
        assertEquals(0, stack.getCurrentNote());
        assertEquals(0, stack.size());
    }
}
