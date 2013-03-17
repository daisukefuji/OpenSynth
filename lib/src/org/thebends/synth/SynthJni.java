package org.thebends.synth;

public class SynthJni {
  public static native float[] getSamples(int numSamples);

  /**
   * Loads the 'synth' library on application startup.  The library has already
   * been unpacked at installation time by the package manager.
   */
  static {
    System.loadLibrary("synth");
  }

};
