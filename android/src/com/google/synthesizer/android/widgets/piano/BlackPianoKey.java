/*
 * Copyright 2010 Google Inc.
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

package com.google.synthesizer.android.widgets.piano;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.google.synthesizer.core.music.Note;

/**
 * One of the black (non-natural) keys on the piano.
 */
public class BlackPianoKey extends NotePianoKey {
  /**
   * Creates a new key.
   * @param piano - the piano this key is on.
   * @param octaveOffset - octave of the key, relative to the leftmost octave of the piano.
   * @param key - offset of the key from the start of the octave.
   */
  public BlackPianoKey(PianoView piano, int octave, int key) {
    super(piano, octave, key);
  }

  /**
   * Sets rect_ to the position of this key, based on the drawing rect of the piano it's on.
   * @param drawingRect - the position of the piano itself.
   * @param octaves - the number of octaves visible on the piano keyboard.
   */
  @Override
  public void layout(Rect drawingRect, int octaves) {
    int whiteKeyWidth = getWhiteKeyWidth(drawingRect, octaves);
    int blackKeyWidth = getBlackKeyWidth(drawingRect, octaves);
    rect_.top = 0;
    rect_.bottom = rect_.top + getBlackKeyHeight(drawingRect);
    rect_.left = ((octaveOffset_ * WHITE_KEYS.length + key_ + 2) * whiteKeyWidth) -
        (blackKeyWidth/2);
    rect_.right = rect_.left + blackKeyWidth;

    sideStartRect_.set(rect_);
    shrinkRect(sideStartRect_, 4); 
    sideStartRect_.bottom -= 5;
    sideStartRect_.right -= 2;

    topRect_.set(sideStartRect_);
    shrinkRect(topRect_, 5); 
    topRect_.top -= 1;

    sideEndRect_.set(topRect_);
    shrinkRect(sideEndRect_, -2);

    gradientStartPoint_.set(0, 0); 
    gradientEndPoint_.set(0, rect_.height() - 4); 
    super.layout(drawingRect, octaves);
  }

  /**
   * Returns the log frequency of the note of the key.
   */
  public double getLogFrequency() {
    return Note.computeLog12TET(BLACK_KEYS[key_], octaveOffset_ + piano_.getFirstOctave());
  }

  /**
   * Returns true if this is one of the black key positions that should actually have a key.
   */
  public static boolean isValid(int note) {
    return BLACK_KEYS[note] != Note.NONE;
  }

  /**
   * Utility function to calculate the width that a standard black key on this keyboard should be.
   */
  protected static int getBlackKeyWidth(Rect drawingRect, int octaves) {
    return (getWhiteKeyWidth(drawingRect, octaves) * 2) / 3;
  }

  /**
   * Utility function to calculate the height that a standard black key on this keyboard should be.
   */
  protected static int getBlackKeyHeight(Rect drawingRect) {
    return getWhiteKeyHeight(drawingRect) / 2;
  }

  @Override
  protected int getSideStartColor() {
    return KEY_SIDE_START_COLOR;
  }

  @Override
  protected int getSideEndColor() {
    return KEY_SIDE_END_COLOR;
  }

  @Override
  protected int getTopColor() {
    return KEY_TOP_COLOR;
  }

  private static final int KEY_SIDE_START_COLOR = 0x0d;
  private static final int KEY_SIDE_END_COLOR = 0x69;
  private static final int KEY_TOP_COLOR = 0x52;

}