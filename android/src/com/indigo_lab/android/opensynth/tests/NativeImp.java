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
package com.indigo_lab.android.opensynth.tests;

import java.util.logging.Logger;

import org.thebends.synth.SynthJni;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.indigo_lab.android.opensynth.R;

public class NativeImp extends Activity {
    private static Logger LOG = Logger.getLogger(
            JavaImp.class.getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        View v = findViewById(R.id.make_a_sound_button);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    SynthJni.noteOn(60);
                    break;
                case MotionEvent.ACTION_UP:
                    SynthJni.noteOff(60);
                    break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        LOG.info("Pausing synth activity");
        SynthJni.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LOG.info("Resuming synth activity");
        SynthJni.start();
        setControllerSettings();
    }

    private void setControllerSettings() {
        /**
         * Oscillator
         */
        // OSC1
        SynthJni.setOSC1Level(0.5f);
        SynthJni.setOSC1WaveType(SynthJni.WAVE_TYPE_TRIANGLE);
        SynthJni.setOSC1Octave(SynthJni.OCTAVE_SHIFT_1);

        // 0SC2
        SynthJni.setOSC2Level(0.3f);
        SynthJni.setOSC2WaveType(SynthJni.WAVE_TYPE_REVERSE_SAWTOOTH);
        SynthJni.setOSC2Octave(SynthJni.OCTAVE_SHIFT_2);

        SynthJni.setGlideSamples(0);

        /**
         * Modulation
         */
        SynthJni.setModulationAmount(0.2f);
        SynthJni.setModulationFrequency(0.3f);
        SynthJni.setModulationSource(SynthJni.MODULATION_SOURCE_LFO_SRC_TRIANGLE);
        SynthJni.setModulationDestination(SynthJni.MODULATION_DESTINATION_LFO_DEST_PITCH);

        /**
         *  Filter
         */
        SynthJni.setFilterCutoff(10000);
        SynthJni.setFilterResonance(0);

        /**
         * Volume envelope
         */
        SynthJni.setAttackToVolumeEnvelope(0);
        SynthJni.setDecayToVolumeEnvelope(0);
        SynthJni.setSustainToVolumeEnvelope(1);
        SynthJni.setReleaseToVolumeEnvelope(1);

        /**
         * Filter envelope
         */
        SynthJni.setAttackToFilterEnvelope(0);
        SynthJni.setDecayToFilterEnvelope(0);
        SynthJni.setSustainToFilterEnvelope(1);
        SynthJni.setReleaseToFilterEnvelope(1);

        /**
         * Arpeggio
         */
        SynthJni.setArpeggioEnabled(false);
        SynthJni.setArpeggioOctaves(3);
        final float value = 0.5f;
        final int kArpeggioMaxSamples = SynthJni.SAMPLE_RATE_HZ;  // 1 second
        final int kArpeggioMinSamples = (int)(SynthJni.SAMPLE_RATE_HZ / 8.);  // 0.125 seconds
        final int arpeggioSamples = (int)(value * (kArpeggioMinSamples - kArpeggioMaxSamples) + kArpeggioMaxSamples);
        SynthJni.setArpeggioSamples(arpeggioSamples);
        SynthJni.setArpeggioStep(SynthJni.STEP_UP_DOWN);
    }
}
