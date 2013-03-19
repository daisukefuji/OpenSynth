package com.indigo_lab.android.opensynth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class ControllerView extends FrameLayout {
    public ControllerView(Context context, AttributeSet attrs, int defStyle, int resource) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource, null, false);
        //View v = View.inflate(context, resource, this);  
        addView(v);
    }

}
