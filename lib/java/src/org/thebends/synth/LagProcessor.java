package org.thebends.synth;

/**
 * A lag processor, used to implement glide.
 */
public class LagProcessor implements Parameter {
  private final Parameter param;
  private final Envelope envelope = new Envelope();
  private long samplesUp;
  private long samplesDown;
  private boolean hasLastValue;
  private double lastValue;

  /**
   * @param param the parameter to glide.
   */
  public LagProcessor(Parameter param) {
    this.param = param;
    samplesUp = 0;
    samplesDown = 0;
    hasLastValue = false;
    lastValue = 0.0;
  }

  public void setSamples(long samples) {
    setSamplesUp(samples);
    setSamplesDown(samples);
  }

  public void setSamplesUp(long samples) {
    samplesUp = samples;
  }

  public void setSamplesDown(long samples) {
    samplesDown = samples;
  }

  public void reset() {
    hasLastValue = false;
  }

  @Override
  public double getValue() {
    double value = param.getValue();
    if (!hasLastValue || lastValue != value) {
      double diff = Math.abs(lastValue - value);
      if (!hasLastValue) {
        // No previous value so the envelope simply ways returns the current
        // value in the sustain state.
        envelope.setMin(0.0);
        envelope.setMax(value);
        envelope.setAttack(0);
        envelope.setDecay(0);
        envelope.setSustain(value);
        envelope.setRelease(0);
        envelope.noteOn();
      } else if (lastValue < value) {
        // Slope up
        envelope.setMin(lastValue);
        envelope.setMax(value);
        envelope.setAttack((long) (samplesUp * diff));
        envelope.setDecay(0);
        envelope.setSustain(value);
        envelope.setRelease(0);
        envelope.noteOn();
      } else {
        // Slope down
        envelope.setMax(lastValue);
        envelope.setMin(value);
        envelope.setAttack(0);
        envelope.setDecay(0);
        envelope.setSustain(lastValue);
        envelope.setRelease((long) (samplesDown * diff));
        envelope.noteOn();
        envelope.noteOff();
      }
      lastValue = value;
      hasLastValue = true;
    }

    return envelope.getValue();
  }
}
