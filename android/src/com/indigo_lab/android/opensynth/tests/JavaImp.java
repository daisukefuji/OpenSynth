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

import org.thebends.synth.Arpeggio;
import org.thebends.synth.Controller;
import org.thebends.synth.Envelope;
import org.thebends.synth.Oscillator;
import org.thebends.synth.SynthTrack;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.indigo_lab.android.opensynth.R;

public class JavaImp extends Activity {
    private static Logger LOG = Logger.getLogger(
            JavaImp.class.getSimpleName());

    private SynthTrack mSynthTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_javaimp);

        View v = findViewById(R.id.javaimp_layout);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mSynthTrack != null) {
                        mSynthTrack.getController().noteOn(60);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mSynthTrack != null) {
                        mSynthTrack.getController().noteOff(60);
                    }
                    break;
                }
                return true;
            }
        });

        mSynthTrack = new SynthTrack();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LOG.info("Pausing synth activity");
        mSynthTrack.shutdown();
        mSynthTrack = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        LOG.info("Resuming synth activity");
        mSynthTrack.start();
        setControllerSettings(mSynthTrack.getController());
    }

    private void setControllerSettings(Controller c) {
        /**
         * Oscillator
         */
        // OSC1
        c.setOsc1Level(0.5f);
        c.setOsc1WaveForm(Oscillator.WaveForm.TRIANGLE);
        c.setOsc1Octave(Controller.OctaveShift.OCTAVE_1);

        // 0SC2
        c.setOsc2Level(0.3f);
        c.setOsc2WaveForm(Oscillator.WaveForm.REVERSE_SAWTOOTH);
        c.setOsc2Octave(Controller.OctaveShift.OCTAVE_2);

        c.setGlideSamples(0);

        /**
         * Modulation
         */
        c.setModulationAmount(0.2f);
        c.setModulationFrequency(0.3f);
        c.setModulationSource(Controller.ModulationSource.LFO_SRC_TRIANGLE);
        c.setModulationDestination(Controller.ModulationDestination.LFO_DEST_PITCH);

        /**
         *  Filter
         */
        c.setFilterCutoff(10000);
        c.setFilterResonance(0);

        /**
         * Envelope
         */
        Envelope envelope = c.volumeEnvelope();
        envelope.setAttack(0);
        envelope.setDecay(0);
        envelope.setSustain(1);
        envelope.setRelease(1);

        /**
         * Filter envelope
         */
        envelope = c.filterEnvelope();
        envelope.setAttack(0);
        envelope.setDecay(0);
        envelope.setSustain(1);
        envelope.setRelease(1);

        /**
         * Arpeggio
         */
        c.setArpeggioEnabled(false);
        c.setArpeggioOctaves(3);
        final float value = 0.5f;
        final long kArpeggioMaxSamples = SynthTrack.SAMPLE_RATE_HZ;  // 1 second
        final long kArpeggioMinSamples = (long)(SynthTrack.SAMPLE_RATE_HZ / 8.);  // 0.125 seconds
        final long arpeggioSamples = (long)(value * (kArpeggioMinSamples - kArpeggioMaxSamples) + kArpeggioMaxSamples);
        c.setArpeggioSamples(arpeggioSamples);
        c.setArpeggioStep(Arpeggio.Step.UP_DOWN);
    }
}