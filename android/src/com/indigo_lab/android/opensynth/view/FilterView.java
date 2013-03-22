package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FilterView extends ControllerView implements OnSeekBarChangeListener {
    private TextView mCutoffText;
    private SeekBar mCutoffSeekBar;
    private SeekBar mResonanceSeekBar;

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.filter_view);

        mCutoffText = (TextView)findViewById(R.id.cutoff_hz);

        mCutoffSeekBar = (SeekBar)findViewById(R.id.cutoff_seekbar);
        mCutoffSeekBar.setOnSeekBarChangeListener(this);
        mCutoffSeekBar.setMax(100);

        mResonanceSeekBar = (SeekBar)findViewById(R.id.resonance_seekbar);
        mResonanceSeekBar.setOnSeekBarChangeListener(this);
        mResonanceSeekBar.setMax(100);
        
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.cutoff_seekbar:
            int min = 40;
            int max = 10000;
            int delta = max - min;
            float cutoff = ((progress / 100f) * delta) + min;
            String text = String.format("%dHz",  Math.round(cutoff));
            mCutoffText.setText(text);
            SynthJni.setFilterCutoff(cutoff);
            break;
        case R.id.resonance_seekbar:
            float value = (progress / 100f) * 0.85f;
            SynthJni.setFilterResonance(value);
            break;
        default:
            throw new RuntimeException("unknown seekbar: " + seekBar.getId());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
