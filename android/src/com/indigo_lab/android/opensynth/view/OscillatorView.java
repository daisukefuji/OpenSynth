package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OscillatorView extends ControllerView implements OnSeekBarChangeListener {
    private class Group implements OnCheckedChangeListener, OnSeekBarChangeListener {
        public TextView mOSCText;
        public SeekBar mLevelSeekBar;
        public RadioGroup mWaveSelection;
        public RadioGroup mOctavetSelection;

        public Group(View v) {
            mOSCText = (TextView)v.findViewById(R.id.which_osc);
            mLevelSeekBar = (SeekBar)v.findViewById(R.id.level_seekbar);
            mLevelSeekBar.setMax(100);
            mLevelSeekBar.setProgress(50);
            mLevelSeekBar.setOnSeekBarChangeListener(this);
            mWaveSelection = (RadioGroup)v.findViewById(R.id.osc_wave_selection);
            mWaveSelection.setOnCheckedChangeListener(this);
            mOctavetSelection = (RadioGroup)v.findViewById(R.id.octave_selection);
            mOctavetSelection.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getId()) {
            case R.id.osc_wave_selection:
            {
                int waveType;
                switch (checkedId) {
                case R.id.radio_square_wave:
                    waveType = SynthJni.WAVE_TYPE_SQUARE;
                    break;
                case R.id.radio_triangle_wave:
                    waveType = SynthJni.WAVE_TYPE_TRIANGLE;
                    break;
                case R.id.radio_sawtooth_wave:
                    waveType = SynthJni.WAVE_TYPE_SAWTOOTH;
                    break;
                case R.id.radio_reverse_sawtooth_wave:
                    waveType = SynthJni.WAVE_TYPE_REVERSE_SAWTOOTH;
                    break;
                default:
                    throw new RuntimeException("unknown wave type : " + checkedId);
                }
                setOSCWaveType(this, waveType);
                break;
            }
            case R.id.octave_selection:
            {
                int octaveShift;
                switch (checkedId) {
                case R.id.radio_octave_1:
                    octaveShift = SynthJni.OCTAVE_SHIFT_1;
                    break;
                case R.id.radio_octave_2:
                    octaveShift = SynthJni.OCTAVE_SHIFT_2;
                    break;
                case R.id.radio_octave_4:
                    octaveShift = SynthJni.OCTAVE_SHIFT_4;
                    break;
                case R.id.radio_octave_8:
                    octaveShift = SynthJni.OCTAVE_SHIFT_8;
                    break;
                case R.id.radio_octave_16:
                    octaveShift = SynthJni.OCTAVE_SHIFT_16;
                    break;
                default:
                    throw new RuntimeException("unknown octave shift : " + checkedId);
                }
                setOSCOctave(this, octaveShift);
                break;
            }
            default:
                throw new RuntimeException("unknown RadioGroup : " + group.getId());
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            float level = progress / 100f;
            setOSCLevel(this, level);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    private Group mLeft;
    private Group mRight;
    private SeekBar mGlideRateSeekbar;

    public OscillatorView(Context context) {
        this(context, null);
    }

    public OscillatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OscillatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.oscillator_view);

        mGlideRateSeekbar = (SeekBar)findViewById(R.id.glide_rate_seekbar);
        mGlideRateSeekbar.setMax(100);
        mGlideRateSeekbar.setProgress(0);
        mGlideRateSeekbar.setOnSeekBarChangeListener(this);

        mLeft = new Group((LinearLayout)findViewById(R.id.osc1));
        mLeft.mOSCText.setText(mLeft.mOSCText.getText() + "1");

        mRight = new Group((LinearLayout)findViewById(R.id.osc2));
        mRight.mOSCText.setText(mRight.mOSCText.getText() + "2");
    }

    private final static int SAMPLE_RATE = 44100;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        float value = (progress / 100f) * 0.04f;
        long samples = Math.round(SAMPLE_RATE * value);
        SynthJni.setGlideSamples(samples);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
   
    private void setOSCLevel(Group group, float level) {
        if (group == mLeft) {
            SynthJni.setOSC1Level(level);
        } else {
            SynthJni.setOSC2Level(level);
        }
    }

    private void setOSCWaveType(Group group, int waveType) {
        if (group == mLeft) {
            SynthJni.setOSC1WaveType(waveType);
        } else {
            SynthJni.setOSC2WaveType(waveType);
        }
    }

    private void setOSCOctave(Group group, int octaveShift) {
        if (group == mLeft) {
            SynthJni.setOSC1Octave(octaveShift);
        } else {
            SynthJni.setOSC2Octave(octaveShift);
        }
    }
}
