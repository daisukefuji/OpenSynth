package org.thebends.synth;

/**
 * A parameter that implements modulation via a low frequency oscillator.
 */
public class LFO implements Parameter {
  private final Parameter level;
  private final Parameter oscillator;

  public LFO(Parameter oscillator, Parameter level) {
    this.oscillator = oscillator;
    this.level = level;
  }

  @Override
  public double getValue() {
    double m = 0.5 * level.getValue();
    double b = 1.0 - m;
    return m * oscillator.getValue() + b;
  }
}
