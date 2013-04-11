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

        public void setProgress(float level) {
            int progress = Math.round(level * 100);
            assert progress >= 0 && progress <= 100;

            mLevelSeekBar.setProgress(progress);
        }

        public void check(int whichId, int value) {
            int checkedId = -1;

            switch (whichId) {
            case R.id.osc_wave_selection:
            {
                switch (value) {
                case SynthJni.WAVE_TYPE_SQUARE:
                    checkedId = R.id.radio_square_wave;
                    break;
                case SynthJni.WAVE_TYPE_TRIANGLE:
                    checkedId = R.id.radio_triangle_wave;
                    break;
                case SynthJni.WAVE_TYPE_SAWTOOTH:
                    checkedId = R.id.radio_sawtooth_wave;
                    break;
                case SynthJni.WAVE_TYPE_REVERSE_SAWTOOTH:
                    checkedId = R.id.radio_reverse_sawtooth_wave;
                    break;
                }
                assert checkedId != -1;
                mWaveSelection.check(checkedId);
                break;
            }
            case R.id.octave_selection:
            {
                switch (value) {
                case SynthJni.OCTAVE_SHIFT_1:
                    checkedId = R.id.radio_octave_1;
                    break;
                case SynthJni.OCTAVE_SHIFT_2:
                    checkedId = R.id.radio_octave_2;
                    break;
                case SynthJni.OCTAVE_SHIFT_4:
                    checkedId = R.id.radio_octave_4;
                    break;
                case SynthJni.OCTAVE_SHIFT_8:
                    checkedId = R.id.radio_octave_8;
                    break;
                case SynthJni.OCTAVE_SHIFT_16:
                    checkedId = R.id.radio_octave_16;
                    break;
                }
                assert checkedId != -1;
                mOctavetSelection.check(checkedId);
                break;
            }
            }
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
        setProgress(SynthJni.getGlideSamples());
        mGlideRateSeekbar.setOnSeekBarChangeListener(this);

        mLeft = new Group((LinearLayout)findViewById(R.id.osc1));
        mLeft.mOSCText.setText(mLeft.mOSCText.getText() + "1");
        mLeft.setProgress(SynthJni.getOSC1Level());
        mLeft.check(R.id.osc_wave_selection, SynthJni.getOSC1WaveType());
        mLeft.check(R.id.octave_selection, SynthJni.getOSC1Octave());

        mRight = new Group((LinearLayout)findViewById(R.id.osc2));
        mRight.mOSCText.setText(mRight.mOSCText.getText() + "2");
        mRight.setProgress(SynthJni.getOSC2Level());
        mRight.check(R.id.osc_wave_selection, SynthJni.getOSC2WaveType());
        mRight.check(R.id.octave_selection, SynthJni.getOSC2Octave());
    }

    private void setProgress(long samples) {
        int progress = Math.round(samples / (float)SynthJni.SAMPLE_RATE_HZ / 0.04f * 100f);
        mGlideRateSeekbar.setProgress(progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        float value = (progress / 100f) * 0.04f;
        long samples = Math.round(SynthJni.SAMPLE_RATE_HZ * value);
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
