package com.indigo_lab.android.opensynth.view;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

public class ImageRadioButton extends RadioButton {
    private Drawable mButtonDrawable;

    public ImageRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundButton, 0, 0);
        mButtonDrawable = a.getDrawable(R.styleable.CompoundButton_android_src);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mButtonDrawable != null) {
            mButtonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = mButtonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
            case Gravity.BOTTOM:
                y = getHeight() - height;
                break;
            case Gravity.CENTER_VERTICAL:
                y = (getHeight() - height) / 2;
                break;
            }

            int buttonWidth = mButtonDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            mButtonDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
            mButtonDrawable.draw(canvas);
        }
    }   
}