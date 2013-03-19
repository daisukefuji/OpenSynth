package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class OscillatorDetailView extends ControllerView implements OnSeekBarChangeListener, OnCheckedChangeListener {
    private TextView mSemisText;
    private TextView mCentsText;
    private TextView mTotalText;
    private SeekBar mSemisSeekBar;
    private SeekBar mCentsSeekBar;
    private Switch mSyncSwitch;

    public OscillatorDetailView(Context context) {
        this(context, null);
    }

    public OscillatorDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OscillatorDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.oscillator_detail_view);

        mSemisText = (TextView)findViewById(R.id.osc2_semitones);
        mCentsText = (TextView)findViewById(R.id.osc2_cents);
        mTotalText = (TextView)findViewById(R.id.osc2_total);

        mSemisSeekBar = (SeekBar)findViewById(R.id.semis_seekbar);
        mCentsSeekBar = (SeekBar)findViewById(R.id.cents_seekbar);

        mSemisSeekBar.setOnSeekBarChangeListener(this);
        mSemisSeekBar.setMax(100);
        mSemisSeekBar.setProgress(50);
        mCentsSeekBar.setOnSeekBarChangeListener(this);
        mCentsSeekBar.setMax(100);
        mCentsSeekBar.setProgress(50);

        mSyncSwitch = (Switch)findViewById(R.id.sync_1_2_switch);
        mSyncSwitch.setOnCheckedChangeListener(this);
   }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        setOsc2Shift();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private int getSemitones() {
        float value = ((mSemisSeekBar.getProgress() / 100f) - 0.5f) / 0.5f;
        return Math.round(value * 12);
    }

    private int getCents() {
        float value = ((mCentsSeekBar.getProgress() / 100f) - 0.5f) / 0.5f;
        return Math.round(value * 100);
    }

    private void setOsc2Shift() {
        int osc2Semitones = getSemitones();
        mSemisText.setText(Integer.toString(osc2Semitones));
        int osc2Cents = getCents();
        mCentsText.setText(Integer.toString(osc2Cents));
        int total = osc2Cents + 100 * osc2Semitones;
        mTotalText.setText(Integer.toString(total));
        SynthJni.nativeSetOsc2Shift(total);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SynthJni.nativeSetOscSync(isChecked);
    }
}
