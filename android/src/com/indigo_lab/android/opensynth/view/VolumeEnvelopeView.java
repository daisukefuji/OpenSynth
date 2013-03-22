package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class VolumeEnvelopeView extends EnvelopeView {
    public VolumeEnvelopeView(Context context) {
        this(context, null);
    }

    public VolumeEnvelopeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeEnvelopeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.volume_envelope_view);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.attack_seekbar:
            SynthJni.setAttackToVolumeEnvelope(calcSample(progress, 2f));
            break;
        case R.id.decay_seekbar:
            SynthJni.setDecayToVolumeEnvelope(calcSample(progress, 2f));
            break;
        case R.id.sustain_seekbar:
            SynthJni.setSustainToVolumeEnvelope(progress / 100f);
            break;
        case R.id.release_seekbar:
            SynthJni.setReleaseToVolumeEnvelope(calcSample(progress, 3f));
            break;
        default:
            throw new RuntimeException("unknown seekbar: " + seekBar.getId());
        }
    }
}
