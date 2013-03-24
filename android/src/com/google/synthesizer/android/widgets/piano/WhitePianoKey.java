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
 * A white key on the piano.
 */
public class WhitePianoKey extends NotePianoKey {
  /**
   * Creates a new key.
   * @param piano - the piano this key is on.
   * @param octaveOffset - octave of the key, relative to the leftmost octave of the piano.
   * @param key - offset of the key from the start of the octave.
   */
  public WhitePianoKey(PianoView piano, int octave, int key) {
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
    rect_.top = 0;
    rect_.bottom = getWhiteKeyHeight(drawingRect);
    rect_.left = ((octaveOffset_ * WHITE_KEYS.length + key_ + 1) * whiteKeyWidth);
    rect_.right = rect_.left + whiteKeyWidth;

    sideStartRect_.set(rect_);
    shrinkRect(sideStartRect_, 4); 
    sideStartRect_.right -= 2;

    topRect_.set(sideStartRect_);
    shrinkRect(topRect_, 5); 
    topRect_.top -= 1;

    sideEndRect_.set(topRect_);
    shrinkRect(sideEndRect_, -2);

    gradientStartPoint_.set(1, 1); 
    gradientEndPoint_.set(rect_.width() - 1, rect_.height() - 1); 
    super.layout(drawingRect, octaves);
  }

  /**
   * Returns the log frequency of the note of the key.
   */
  public double getLogFrequency() {
    return Note.computeLog12TET(WHITE_KEYS[key_], octaveOffset_ + piano_.getFirstOctave());
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

  private static final int KEY_SIDE_START_COLOR = 0xc7;
  private static final int KEY_SIDE_END_COLOR = 0xee;
  private static final int KEY_TOP_COLOR = 0xfa;

}