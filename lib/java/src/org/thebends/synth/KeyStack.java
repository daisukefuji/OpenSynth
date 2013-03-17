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


/**
 * The key stack keeps track of notes pressed and released, determining when
 * the attack phase should be entered or when the note should just change
 * without starting a new note attack.
 */
public class KeyStack {
    private static final int MIDDLE_A_KEY = 49;
    private static final float NOTES_PER_OCTAVE = 12.0f;
    private static final float MIDDLE_A_FREQUENCY = 440.0f;

    public static final int MAX_SIZE = 64;
    private int mSize;
    private int[] mNotes = new int[MAX_SIZE];
    // Number of times the note at the position was pressed
    private int[] mCount = new int[MAX_SIZE];

    public KeyStack() {
        mSize = 0;
    }

    /**
     * Returns true if this was the first note pushed on to the key stack
     */
    public boolean noteOn(int note) {
        assert mSize < MAX_SIZE;
        for (int i = 0; i < mSize; ++i) {
            if (mNotes[i] == note) {
                mCount[i]++;
                return false;
            }
        }
        mNotes[mSize] = note;
        mCount[mSize] = 1;
        mSize++;
        return true;
    }

    /**
     * Returns true if this was the last note removed from the key stack
     */
    public boolean noteOff(int note) {
        for (int i = 0; i < mSize; ++i) {
            if (mNotes[i] == note) {
                mCount[i]--;
                if (mCount[i] == 0) {
                    // Remove this element from the stack -- copy all elements above
                    for (int j = i; j < mSize - 1; ++j) {
                        mNotes[j] = mNotes[j + 1];
                        mCount[j] = mCount[j + 1];
                    }
                    mSize--;
                }
                return true;
            }
        }
        // The note wasn't on the stack.  The multi-touch events on the iphone seem
        // to be flaky, so we don't worry if we were asked to remove something that
        // was not on the stack.  The controller also calls our clear() method when
        // no touch events are left as a fallback. 
        return false;
    }

    public boolean isNoteInStack(int note) {
        for (int i = 0; i < mSize; ++i) {
            if (mNotes[i] == note) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < mSize; ++i) {
            count += mCount[i];
        }
        return count;
    }

    public void clear() {
        mSize = 0;
    }

    /**
     * Returns the current not, or 0 if no note is playing.
     */
    public int getCurrentNote() {
        if (mSize > 0) {
            return mNotes[mSize - 1];
        }
        return 0;
    }

    /**
     * Return the note at the specified position in the stack.  num must be less
     * than size. 
     */
    public int getNote(int num) {
        if (num >= mSize) {
            return 0;
        }
        return mNotes[num];
    }

    public static double toFrequency(int key) {
        return MIDDLE_A_FREQUENCY * Math.pow(2, (key - MIDDLE_A_KEY) / NOTES_PER_OCTAVE);
    }
}
