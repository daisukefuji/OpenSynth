package com.indigo_lab.android.opensynth.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

public class RadioButton extends android.widget.RadioButton {
    private Drawable mIndicatorDrawable;

    private static final int NO_ALPHA = 0xFF;
    private float mDisabledAlpha;
 
    private final static int com_android_internal_R_attr_buttonStyleToggle =
            Resources.getSystem().getIdentifier("buttonStyleToggle", "attr", "android");
    private final static int com_android_internal_R_id_toggle =
            Resources.getSystem().getIdentifier("toggle", "id", "android");

    public RadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDisabledAlpha = 0.5f;
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, com_android_internal_R_attr_buttonStyleToggle);
    }

    public RadioButton(Context context) {
        this(context, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        updateReferenceToIndicatorDrawable(getBackground());
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);

        updateReferenceToIndicatorDrawable(d);
    }

    private void updateReferenceToIndicatorDrawable(Drawable backgroundDrawable) {
        if (backgroundDrawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) backgroundDrawable;
            mIndicatorDrawable =
                    layerDrawable.findDrawableByLayerId(com_android_internal_R_id_toggle);
        } else {
            mIndicatorDrawable = null;
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mIndicatorDrawable != null) {
            mIndicatorDrawable.setAlpha(isEnabled() ? NO_ALPHA : (int) (NO_ALPHA * mDisabledAlpha));
        }
    }
}
