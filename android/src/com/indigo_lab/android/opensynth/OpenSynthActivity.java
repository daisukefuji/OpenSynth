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

import com.google.synthesizer.android.widgets.piano.PianoView;
import com.google.synthesizer.core.midi.MidiListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

public class OpenSynthActivity extends Activity {
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private PianoView mPianoView;

    private MidiListener mMidiListener = new MidiListener(){
        @Override
        public void onNoteOff(int channel, int note, int velocity) {
            SynthJni.noteOff(note);
        }

        @Override
        public void onNoteOn(int channel, int note, int velocity) {
            SynthJni.noteOn(note);
        }

        @Override
        public void onNoteAftertouch(int channel, int note, int aftertouch) {
        }

        @Override
        public void onController(int channel, int control, int value) {
        }

        @Override
        public void onProgramChange(int channel, int program) {
        }

        @Override
        public void onChannelAftertouch(int channel, int aftertouch) {
        }

        @Override
        public void onPitchBend(int channel, int value) {
        }

        @Override
        public void onTimingClock() {
        }

        @Override
        public void onActiveSensing() {
        }

        @Override
        public void onSequenceNumber(int sequenceNumber) {
        }

        @Override
        public void onText(byte[] text) {
        }

        @Override
        public void onCopyrightNotice(byte[] text) {
        }

        @Override
        public void onSequenceName(byte[] text) {
        }

        @Override
        public void onInstrumentName(byte[] text) {
        }

        @Override
        public void onLyrics(byte[] text) {
        }

        @Override
        public void onMarker(byte[] text) {
        }

        @Override
        public void onCuePoint(byte[] text) {
        }

        @Override
        public void onChannelPrefix(int channel) {
        }

        @Override
        public void onPort(byte[] data) {
        }

        @Override
        public void onEndOfTrack() {
        }

        @Override
        public void onSetTempo(int microsecondsPerQuarterNote) {
        }

        @Override
        public void onSmpteOffset(byte[] data) {
        }

        @Override
        public void onTimeSignature(int numerator, int denominator,
                int metronomePulse, int thirtySecondNotesPerQuarterNote) {
        }

        @Override
        public void onKeySignature(int key, boolean isMinor) {
        }

        @Override
        public void onSequencerSpecificEvent(byte[] data) {
        }

        @Override
        public void onSysEx(byte[] data) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_synth);

        mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);

        mPianoView = (PianoView)findViewById(R.id.piano);
        mPianoView.bindTo(mMidiListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SynthJni.start();
        SynthJni.restoreSettings(getPreferences(Context.MODE_PRIVATE));
    }

    @Override
    protected void onPause() {
        super.onResume();

        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        SynthJni.saveSettings(editor);
        editor.commit();
        SynthJni.shutdown();
    }
}
