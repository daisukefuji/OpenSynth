package org.thebends.synth;

/**
 * A parameter that can have its value changed.  This is usually just used for
 * testing.
 */
public class MutableParameter implements Parameter {
  private double value;

  public MutableParameter() {
    value = 0.0;
  }

  public void setValue(double value) {
    this.value = value;
  } 

  @Override
  public double getValue() {
    return value;
  }
}
