package org.thebends.synth;

/**
 * An immutable parameter that always returns the same fixed value.  This is
 * mostly used for testing other components with a simple parameter.  New
 * instances are obtained from the static {@link #get(double)} method.
 */
public final class FixedParameter implements Parameter {
  private final double value;

  private FixedParameter(double value) {
    this.value = value;
  }

  /**
   * @return a Parameter that always returns the specified value.
   */
  public static Parameter get(double value) {
    return new FixedParameter(value);
  }

  @Override
  public double getValue() {
    return value;
  }
}
