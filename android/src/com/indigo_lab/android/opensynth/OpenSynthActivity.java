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
package com.indigo_lab.android.opensynth;

import org.thebends.synth.SynthJni;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;

public class OpenSynthActivity extends Activity {
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_synth);

        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);

        //TODO this code is replaced by keyboard view
        android.view.View v = findViewById(R.id.keyboard_view);
        v.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
                switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    SynthJni.nativeNoteOn(10);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                    SynthJni.nativeNoteOff(10);
                    break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SynthJni.nativeStart();
        setControllerSettings();
    }

    @Override
    protected void onPause() {
        super.onResume();

        SynthJni.nativeShutdown();
    }

    //TODO remove me
    private void setControllerSettings() {
        /**
         * Oscillator
         */
        // OSC1
        SynthJni.nativeSetOSC1Level(0.5f);
        SynthJni.nativeSetOSC1WaveType(SynthJni.WAVE_TYPE_TRIANGLE);
        SynthJni.nativeSetOSC1Octave(SynthJni.OCTAVE_SHIFT_1);

        // 0SC2
        SynthJni.nativeSetOSC2Level(0.3f);
        SynthJni.nativeSetOSC2WaveType(SynthJni.WAVE_TYPE_REVERSE_SAWTOOTH);
        SynthJni.nativeSetOSC2Octave(SynthJni.OCTAVE_SHIFT_2);

        SynthJni.nativeSetGlideSamples(0);

        /**
         * Modulation
         */
        SynthJni.nativeSetModulationAmount(0.2f);
        SynthJni.nativeSetModulationFrequency(0.3f);
        SynthJni.nativeSetModulationSource(SynthJni.MODULATION_SOURCE_LFO_SRC_TRIANGLE);
        SynthJni.nativeSetModulationDestination(SynthJni.MODULATION_DESTINATION_LFO_DEST_PITCH);

        /**
         *  Filter
         */
        SynthJni.nativeSetFilterCutoff(10000);
        SynthJni.nativeSetFilterResonance(0);

        /**
         * Volume envelope
         */
        SynthJni.nativeSetAttackToVolumeEnvelope(0);
        SynthJni.nativeSetDecayToVolumeEnvelope(0);
        SynthJni.nativeSetSustainToVolumeEnvelope(1);
        SynthJni.nativeSetReleaseToVolumeEnvelope(1);

        /**
         * Filter envelope
         */
        SynthJni.nativeSetAttackToFilterEnvelope(0);
        SynthJni.nativeSetDecayToFilterEnvelope(0);
        SynthJni.nativeSetSustainToFilterEnvelope(1);
        SynthJni.nativeSetReleaseToFilterEnvelope(1);

        /**
         * Arpeggio
         */
        SynthJni.nativeSetArpeggioEnabled(false);
        SynthJni.nativeSetArpeggioOctaves(3);
        final float value = 0.5f;
        final int kArpeggioMaxSamples = SynthJni.SAMPLE_RATE_HZ;  // 1 second
        final int kArpeggioMinSamples = (int)(SynthJni.SAMPLE_RATE_HZ / 8.);  // 0.125 seconds
        final int arpeggioSamples = (int)(value * (kArpeggioMinSamples - kArpeggioMaxSamples) + kArpeggioMaxSamples);
        SynthJni.nativeSetArpeggioSamples(arpeggioSamples);
        SynthJni.nativeSetArpeggioStep(SynthJni.STEP_UP_DOWN);
    }
}
