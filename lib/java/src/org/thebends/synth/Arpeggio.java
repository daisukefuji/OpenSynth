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
package org.thebends.synth;

public class Arpeggio implements Parameter {
    // Describes how to determine the next note
    public enum Step {
        UP,
        DOWN,
        UP_DOWN,
        RANDOM
    };

    private static final int kNotesPerOctave = 12;

    private KeyStack mKeys;
    private long mSample;
    private long mSamplesPerNote;
    private int mOctaves;
    private int mNote;
    private boolean mMovingUp;  // Only used by UP_DOWN
    private Step mStep;

    public Arpeggio(KeyStack keys) {
        mKeys = keys;
        mSample = 0;
        mSamplesPerNote = 1;
        mOctaves = 1;
        mNote = -1;
        mMovingUp = true;
        mStep = Step.UP;
    }

    public double getValue() {
        int note = getNote();
        if (note > 0) {
            return KeyStack.toFrequency(getNote());
        }
        return 0.0f;
    }

    public int getNote() {
        int size = mKeys.size();
        if (size <= 0) {
            return 0;
        }
        int max = mOctaves * size;
        if (mSample == 0) {
            if (mStep == Step.UP) {
                mNote = (mNote + 1) % max;
            } else if (mStep == Step.DOWN) {
                mNote--;
                if (mNote < 0) {
                    mNote = max - 1;
                }
            } else if (mStep == Step.UP_DOWN) {
                if (mMovingUp) {
                    mNote++;
                    if (mNote >= max - 1) {
                        mNote = max - 1;
                        mMovingUp = false;
                    }
                } else {
                    mNote--;
                    if (mNote <= 0) {
                        mMovingUp = true;
                        mNote = 0;
                    }
                }
            } else {
                mNote = (int)(Math.random() % max);
            }
        }
        mSample = (mSample + 1) % mSamplesPerNote;
        int octave = mNote / size;
        return octave * kNotesPerOctave + mKeys.getNote(mNote % size);
    }

    /*
     *  The number of octaves up to include
     */
    public void setOctaves(int count) {
        assert count >= 1;
        mOctaves = count;
    }

    public void setStep(Step step) {
        mStep = step;
    }

    public void setSamplesPerNote(long samples_per_note) {
        mSamplesPerNote = samples_per_note;
    }

    public void reset() {
        mSample = 0;
    }
}
