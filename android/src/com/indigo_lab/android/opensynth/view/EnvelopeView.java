package com.indigo_lab.android.opensynth.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.indigo_lab.android.opensynth.R;

public abstract class EnvelopeView extends ControllerView implements OnSeekBarChangeListener {
    private SeekBar mAttackSeekBar;
    private SeekBar mDecaySeekBar;
    private SeekBar mSustainSeekBar;
    private SeekBar mReleaseSeekBar;

    public EnvelopeView(Context context, AttributeSet attrs, int defStyle, int resource) {
        super(context, attrs, defStyle, resource);

        mAttackSeekBar = (SeekBar)findViewById(R.id.attack_seekbar);
        mAttackSeekBar.setOnSeekBarChangeListener(this);
        mAttackSeekBar.setMax(100);
        mDecaySeekBar = (SeekBar)findViewById(R.id.decay_seekbar);
        mDecaySeekBar.setOnSeekBarChangeListener(this);
        mAttackSeekBar.setMax(100);
        mSustainSeekBar = (SeekBar)findViewById(R.id.sustain_seekbar);
        mSustainSeekBar.setOnSeekBarChangeListener(this);
        mAttackSeekBar.setMax(100);
        mReleaseSeekBar = (SeekBar)findViewById(R.id.release_seekbar);
        mReleaseSeekBar.setOnSeekBarChangeListener(this);
        mAttackSeekBar.setMax(100);
    }

    protected long calcSample(int progress, float scale) {
        float seconds = (progress / 100f) * scale;
        return Math.round(44100 * seconds);
    }

    public abstract void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) ;

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}