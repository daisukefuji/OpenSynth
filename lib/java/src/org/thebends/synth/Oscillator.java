package org.thebends.synth;

public final class Oscillator implements Parameter {
  private final Parameter frequencyParameter;
  private final double sampleRate;
  private WaveForm waveForm = WaveForm.SQUARE;
  private long currentSample = 0;

  public enum WaveForm {
    SINE,
    SQUARE,
    TRIANGLE,
    SAWTOOTH,
    REVERSE_SAWTOOTH,
  };

  public Oscillator(Parameter frequencyParameter,
                    double sampleRate) {
    this.frequencyParameter = frequencyParameter;
    this.sampleRate = sampleRate;
  }

  public void setWaveForm(WaveForm waveForm) {
    this.waveForm = waveForm;
  }

  @Override
  public double getValue() {
    double frequency = frequencyParameter.getValue();
    long periodSamples = (long) (sampleRate / frequency);
    double x = (double)currentSample / periodSamples;
    double value = 0;
    switch (waveForm) {
      case SINE:
        value = Math.sin(2.0f * Math.PI * x);
        break;
      case SQUARE:
        if (currentSample < (periodSamples / 2)) {
          value = 1.0f;
        } else {
          value = -1.0f;
        }
        break;
      case TRIANGLE:
        value = (2.f * Math.abs(2.0 * x - 2.0 * Math.floor(x) - 1.0f) - 1.0f);
        break;
      case SAWTOOTH:
        value = 2.0f * (x - Math.floor(x) - 0.5f);
        break;
      case REVERSE_SAWTOOTH:
        value = 2.0f * (Math.floor(x) - x + 0.5f);
        break;
      default:
        throw new RuntimeException("Illegal wave type: " + waveForm);
    }
    currentSample = (currentSample + 1) % (periodSamples);
    return value;
  }

};
