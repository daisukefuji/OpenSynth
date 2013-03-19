package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;

public class ArpeggioView extends ControllerView
    implements OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener{
    private Switch mArpeggioSwitch;
    private SeekBar mSampleRateSeekbar;
    private RadioGroup mOctaves;
    private RadioGroup mSteps;

    public ArpeggioView(Context context) {
        this(context, null);
    }

    public ArpeggioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArpeggioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.arpeggio_view);

        mArpeggioSwitch = (Switch)findViewById(R.id.arpeggio_switch);
        mArpeggioSwitch.setOnCheckedChangeListener(this);
        mSampleRateSeekbar = (SeekBar)findViewById(R.id.sample_rate_seekbar);
        mSampleRateSeekbar.setOnSeekBarChangeListener(this);
        mSampleRateSeekbar.setMax(100);
        mOctaves = (RadioGroup)findViewById(R.id.octaves);
        mOctaves.setOnCheckedChangeListener(this);
        mSteps = (RadioGroup)findViewById(R.id.steps);
        mSteps.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SynthJni.nativeSetArpeggioEnabled(isChecked);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
        case R.id.octaves:
        {
            int octave;
            switch (checkedId) {
            case R.id.radio_one:
                octave = 1;
                break;
            case R.id.radio_two:
                octave = 2;
                break;
            case R.id.radio_three:
                octave = 3;
                break;
            case R.id.radio_four:
                octave = 4;
                break;
            case R.id.radio_five:
                octave = 5;
                break;
            default:
                throw new RuntimeException("unknown octave: " + checkedId);
            }
            SynthJni.nativeSetArpeggioOctaves(octave);
            break;
        }
        case R.id.steps:
        {
            int step;
            switch (checkedId) {
            case R.id.radio_up:
                step = SynthJni.STEP_UP;
                break;
            case R.id.radio_down:
                step = SynthJni.STEP_DOWN;
                break;
            case R.id.radio_up_down:
                step = SynthJni.STEP_UP_DOWN;
                break;
            case R.id.radio_random:
                step = SynthJni.STEP_RANDOM;
                break;
            default:
                throw new RuntimeException("unknown step: " + checkedId);
            }
            SynthJni.nativeSetArpeggioStep(step);
            break;
        }
        default:
            throw new RuntimeException("unknown RadioGroup: " + group.getId());
        }
    }

    private static final float kSampleRate = 44100.0f;
    private static final long kArpeggioMaxSamples = (long)kSampleRate;  // 1 second
    private static final long kArpeggioMinSamples = Math.round(kSampleRate / 8);  // 0.125 seconds

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        float value = progress / 100f;
        int samples = Math.round(value * (kArpeggioMinSamples - kArpeggioMaxSamples) + kArpeggioMaxSamples);
        SynthJni.nativeSetArpeggioSamples(samples);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

