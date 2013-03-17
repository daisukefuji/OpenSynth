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

import org.thebends.synth.Controller;
import org.thebends.synth.Oscillator;

import junit.framework.TestCase;

public class ControllerTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testController() {
      Controller controller = new Controller();
      controller.setOsc1Level(0.5);
      controller.setOsc1WaveForm(Oscillator.WaveForm.TRIANGLE);
      controller.setSampleRate(16);

      for (int i = 0; i < 10; ++i) {
        assertEquals(0.0, controller.getSample());
      }

      controller.noteOnFrequency(2);  // 2 cycles per second

      for (int i = 0; i < 10; ++i) {
          assertEquals(0.5, controller.getSample());
          assertEquals(0.25, controller.getSample());
          assertEquals(0.0, controller.getSample());
          assertEquals(-0.25, controller.getSample());
          assertEquals(-0.5, controller.getSample());
          assertEquals(-0.25, controller.getSample());
          assertEquals(0.0, controller.getSample());
          assertEquals(0.25, controller.getSample());
      }

      // Silence
      controller.noteOff();
      for (int i = 0; i < 10; i++) {
          assertEquals(0.0, controller.getSample());
      }
    }
}
