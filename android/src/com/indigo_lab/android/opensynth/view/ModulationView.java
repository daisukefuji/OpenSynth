package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ModulationView extends ControllerView implements OnCheckedChangeListener, OnSeekBarChangeListener {
    private SeekBar mLFORateSeekBar;
    private SeekBar mLFOAmountSeekBar;
    private RadioGroup mModulationSource;
    private RadioGroup mModulationDestination;

    public ModulationView(Context context) {
        this(context, null);
    }

    public ModulationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModulationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.modulation_view);

        mLFORateSeekBar = (SeekBar)findViewById(R.id.lfo_rate_seekbar);
        mLFOAmountSeekBar = (SeekBar)findViewById(R.id.lfo_amount_seekbar);
        mLFORateSeekBar.setOnSeekBarChangeListener(this);
        mLFORateSeekBar.setMax(100);
        mLFOAmountSeekBar.setOnSeekBarChangeListener(this);
        mLFOAmountSeekBar.setMax(100);

        mModulationSource = (RadioGroup)findViewById(R.id.modulation_source);
        mModulationSource.setOnCheckedChangeListener(this);
        mModulationDestination = (RadioGroup)findViewById(R.id.modulation_destination);
        mModulationDestination.setOnCheckedChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.lfo_rate_seekbar:
            float frequency = (progress / 100f) * 15f;
            SynthJni.setModulationFrequency(frequency);
            break;
        case R.id.lfo_amount_seekbar:
            float amount = progress / 100f;
            SynthJni.setModulationAmount(amount);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
        case R.id.modulation_source:
        {
            int source;
            switch (checkedId) {
            case R.id.radio_square_wave:
                source = SynthJni.MODULATION_SOURCE_LFO_SRC_SQUARE;
                break;
            case R.id.radio_triangle_wave:
                source = SynthJni.MODULATION_SOURCE_LFO_SRC_TRIANGLE;
                break;
            case R.id.radio_sawtooth_wave:
                source = SynthJni.MODULATION_SOURCE_LFO_SRC_SAWTOOTH;
                break;
            case R.id.radio_reverse_sawtooth_wave:
                source = SynthJni.MODULATION_SOURCE_LFO_SRC_REVERSE_SAWTOOTH;
                break;
            default:
                throw new RuntimeException("unknown modulation source: " + checkedId);
            }
            SynthJni.setModulationSource(source);
            break;
        }
        case R.id.modulation_destination:
        {
            int destination;
            switch (checkedId) {
            case R.id.radio_filter:
                destination = SynthJni.MODULATION_DESTINATION_LFO_DEST_FILTER;
                break;
            case R.id.radio_pitch:
                destination = SynthJni.MODULATION_DESTINATION_LFO_DEST_PITCH;
                break;
            case R.id.radio_wave:
                destination = SynthJni.MODULATION_DESTINATION_LFO_DEST_WAVE;
                break;
            default:
                throw new RuntimeException("unknown modulation destination: " + checkedId);
            }
            SynthJni.setModulationDestination(destination);
            break;
        }
        default:
            throw new RuntimeException("unknown RadioGroup: " + group.getId());
        }
    }
}
