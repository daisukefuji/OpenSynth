package com.indigo_lab.android.opensynth.view;

import org.thebends.synth.SynthJni;

import com.indigo_lab.android.opensynth.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class FilterEnvelopeView extends EnvelopeView {
    public FilterEnvelopeView(Context context) {
        this(context, null);
    }

    public FilterEnvelopeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterEnvelopeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, R.layout.filter_envelope_view);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        switch (seekBar.getId()) {
        case R.id.attack_seekbar:
            SynthJni.nativeSetAttackToFilterEnvelope(calcSample(progress, 1f));
            break;
        case R.id.decay_seekbar:
            SynthJni.nativeSetDecayToFilterEnvelope(calcSample(progress, 1f));
            break;
        case R.id.sustain_seekbar:
            SynthJni.nativeSetSustainToFilterEnvelope(progress / 100f);
            break;
        case R.id.release_seekbar:
            SynthJni.nativeSetReleaseToFilterEnvelope(calcSample(progress, 4f));
            break;
        default:
            throw new RuntimeException("unknown seekbar: " + seekBar.getId());
        }
    }
}
