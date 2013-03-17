package org.thebends.synth;

/**
 * An output value that may change over time.
 */
public interface Parameter {

  /**
   * Gets the current value of the parameter.  Parameters that change over time
   * expect to be invoked once for every output sample.
   *
   * @return the current value of the parameter.
   */
  public double getValue();

}
