/*
 * Copyright 2011 Google Inc.
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
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;

import com.google.synthesizer.core.music.Note;

/**
 * PianoKey is the abstract base class for any key on the piano.
 * It keeps track of whether the key is currently being pressed.
 */
public abstract class PianoKey {
  public PianoKey(PianoView piano) {
    piano_ = piano;
    pressed_ = new boolean[PianoView.FINGERS];
    rect_ = new Rect();

    for (int i = 0; i < pressed_.length; ++i) {
      pressed_[i] = false;
    }

    // Set up some default objects for the key to draw itself with.
    fillPaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);
    strokePaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);
    fillPaint_.setStyle(Paint.Style.FILL);
    strokePaint_.setStyle(Paint.Style.STROKE);
    strokePaint_.setColor(Color.BLACK);

    sideStartRect_ = new Rect();
    topRect_ = new Rect();
    sideEndRect_ = new Rect();

    gradientStartPoint_ = new PointF();
    gradientEndPoint_ = new PointF();

    sideStartPaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);
    sideStartPaint_.setColor(Color.argb(0xff, getSideStartColor(), getSideStartColor(), getSideStartColor()));
    sideStartPaint_.setStrokeJoin(Paint.Join.ROUND);
    sideStartPaint_.setStrokeWidth(SIDE_START_STROKE_WIDTH);
    sideStartPaint_.setStyle(Paint.Style.STROKE);
    sideStartPaint_.setShadowLayer(4.0f, 0.0f, -3.0f, Color.BLACK);

    sideEndPaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);
    sideEndPaint_.setColor(Color.argb(0xff, getSideEndColor(), getSideEndColor(), getSideEndColor()));
    sideEndPaint_.setStrokeJoin(Paint.Join.ROUND);
    sideEndPaint_.setStrokeWidth(SIDE_END_STROKE_WIDTH);
    sideEndPaint_.setStyle(Paint.Style.STROKE);

    topPaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);
    topPaint_.setColor(Color.argb(0xff, getTopColor(), getTopColor(), getTopColor()));
    topPaint_.setStrokeJoin(Paint.Join.ROUND);
    topPaint_.setStrokeWidth(TOP_STROKE_WIDTH);
    topPaint_.setStyle(Paint.Style.FILL_AND_STROKE);

    shaderPaint_ = new Paint(Paint.ANTI_ALIAS_FLAG);

    topPath_ = new Path();
    sideStartPath_ = new Path();
    sideEndPath_ = new Path();
  }

  /**
   * Sets rect_ to the position of this key, based on the drawing rect of the piano it's on.
   * @param drawingRect - the position of the piano itself.
   * @param octaves - the number of octaves visible on the piano keyboard.
   */
  public void layout(Rect drawingRect, int octaves) {
      sideStartPath_.reset();
      sideStartPath_.addRect(new RectF(sideStartRect_), Path.Direction.CW);
 
      sideEndPath_.reset();
      sideEndPath_.addRect(new RectF(sideEndRect_), Path.Direction.CW);
 
      topPath_.reset();
      topPath_.addRect(new RectF(topRect_), Path.Direction.CW);

      LinearGradient shader = new LinearGradient(
              gradientStartPoint_.x, gradientStartPoint_.y,
              gradientEndPoint_.x, gradientEndPoint_.y,
              GRADIENT_COLOR0, GRADIENT_COLOR1, TileMode.CLAMP);
      shaderPaint_.setShader(shader);
  }

  /**
   * Draws the key in the current rect_.
   */
  public void draw(Canvas canvas) {
    canvas.drawPath(topPath_, topPaint_);
    canvas.drawPath(sideStartPath_, sideStartPaint_);
    canvas.drawPath(sideEndPath_, sideEndPaint_);
    if (isPressed()) {
      // When the key is pressed, draw an extra alpha gradient on top.
      canvas.drawRect(topRect_, shaderPaint_);
    }
  }

  /**
   * Called when the key's pressed_ state has changed.
   * @param move - true if the key became pressed because the touch moved onto it.
   */
  abstract protected void onPressedChanged(boolean move);

  /**
   * Returns true if the given co-ordinate is inside the key's current rect_.
   */
  public boolean contains(int x_, int y_) {
    return rect_.contains(x_, y_);
  }

  /**
   * Returns true if any finger is pressing this key.
   */
  public boolean isPressed() {
    for (int i = 0; i < pressed_.length; ++i) {
      if (pressed_[i]) {
        return true;
      }
    }
    return false;
  }

  /**
   * Called when a finger has touched down onto this key.
   * Returns true iff whether the pressed state changed.
   */
  final public boolean onTouchDown(int finger) {
    if (finger >= pressed_.length) {
      Log.e(getClass().getName(),
            "Finger " + finger + " was pressed down, but PianoKey only supports " +
            pressed_.length + " fingers.");
    }
    boolean wasPressed = isPressed();
    pressed_[finger] = true;
    if (!wasPressed) {
      onPressedChanged(false);
      return true;
    }
    return false;
  }

  /**
   * Called on a touch event where this key is not being touched.  It may already be up.
   * Returns true iff whether the pressed state changed.
   */
  final public boolean onTouchUp(int finger) {
    if (finger >= pressed_.length) {
      Log.e(getClass().getName(),
            "Finger " + finger + " was released, but PianoKey only supports " +
            pressed_.length + " fingers.");
    }
    boolean wasPressed = isPressed();
    pressed_[finger] = false;
    boolean isPressed = isPressed();
    if (wasPressed && !isPressed) {
      onPressedChanged(false);
      return true;
    }
    return false;
  }

  /**
   * Called when there's a touch event where the finger was moved onto this key.
   * Returns true iff whether the pressed state changed.
   */
  final public boolean onTouchMoved(int finger) {
    if (finger >= pressed_.length) {
      Log.e(getClass().getName(),
            "Finger " + finger + " was pressed down, but PianoKey only supports " +
            pressed_.length + " fingers.");
    }
    boolean wasPressed = isPressed();
    pressed_[finger] = true;
    if (!wasPressed) {
      onPressedChanged(true);
      return true;
    }
    return false;
  }

  /**
   * Utility function to calculate the width that a standard white key on this keyboard should be.
   */
  protected static int getWhiteKeyWidth(Rect drawingRect, int octaves) {
    // It's +2 to reserve space for the octave-up/down buttons.
    return drawingRect.width() / ((WHITE_KEYS.length * octaves) + 2);
  }
  
  /**
   * Utility function to calculate the height that a standard white key on this keyboard should be.
   */
  protected static int getWhiteKeyHeight(Rect drawingRect) {
    return drawingRect.height();
  }

  protected int getSideStartColor() {
    return 0;
  }

  protected int getSideEndColor() {
    return 0;
  }

  protected int getTopColor() {
    return 0;
  }

  protected void shrinkRect(Rect rect, int pixels) {
      rect.left += pixels;
      rect.top += pixels;
      rect.right -= pixels;
      rect.bottom -= pixels;
  }

  // The piano this key is on.
  protected PianoView piano_;

  // Is each keys currently being pressed?
  protected boolean[] pressed_;

  // The area this key occupies.
  protected Rect rect_;

  // Objects for subclasses to use for painting, just so they don't have to reallocate every time.
  protected Paint fillPaint_;
  protected Paint strokePaint_;

  protected Rect sideStartRect_;
  protected Rect sideEndRect_;
  protected Rect topRect_;

  protected PointF gradientStartPoint_;
  protected PointF gradientEndPoint_;

  private Paint sideStartPaint_;
  private Paint sideEndPaint_;
  private Paint topPaint_;
  private Paint shaderPaint_;

  private Path sideStartPath_;
  private Path sideEndPath_;
  private Path topPath_;

  // Constants to map notes onto the keys.
  protected static final int WHITE_KEYS[] = {
      Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B };
  protected static final int BLACK_KEYS[] = {
      Note.C_SHARP, Note.E_FLAT, Note.NONE, Note.F_SHARP, Note.A_FLAT, Note.B_FLAT, Note.NONE };

  private static final float SIDE_START_STROKE_WIDTH = 8.0f;
  private static final float SIDE_END_STROKE_WIDTH = 1.0f;
  private static final float TOP_STROKE_WIDTH = 7.0f;

  private static final int GRADIENT_COLOR0 = Color.argb(0xcc, 0x1a, 0x1a, 0x1a);
  private static final int GRADIENT_COLOR1 = Color.argb(0x00, 0x1a, 0x1a, 0x1a);
}