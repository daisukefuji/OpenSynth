package org.thebends.synth;

/**
 * An envelope controls a value over time.
 */
public class Envelope implements Parameter {
  private long attack;
  private double attackSlope;
  private long decay;
  private long decayEnd;
  private double decaySlope;
  private double sustain;
  private long release;
  private long releaseStart;
  private long releaseEnd;
  private double releaseSlope;
  private double max;
  private double min;
  private long currentSample;
  private double lastValue;
  private double releaseStartValue;
  enum State {
    ATTACK,
    DECAY,
    SUSTAIN,
    RELEASE,
    DONE
  };
  private State state;

  public Envelope() {
    attack = 0;
    attackSlope = 0.0;
    decay = 0;
    decayEnd = 0;
    decaySlope = 0.0;
    sustain = 1.0;
    release = 0;
    releaseStart = 0;
    releaseEnd = 0;
    releaseSlope = 0.0;
    min = 0.0;
    max = 1.0;
    currentSample = 0;
    releaseStartValue = 0.0;
    state = State.DONE;
  }

  /**
   * @param attack in samples
   */
  public void setAttack(long attack) {
    this.attack = attack;
  }

  /**
   * @param decay in samples
   */
  public void setDecay(long decay) {
    this.decay = decay;
  }

  /**
   * @param sustain volume (usually between 0.0 and 1.0)
   */
  public void setSustain(double sustain) {
    this.sustain = sustain;
  }

  /**
   * @param release in samples
   */
  public void setRelease(long release) {
    this.release = release;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public void setMin(double min) {
    this.min = min;
  }

  /**
   * Invoked when the note is pressed.
   */
  public void noteOn() {
    currentSample = 0;
    decayEnd = attack + decay;
    if (attack == 0) {
      attackSlope = 1;
    } else {
      attackSlope = (max - min) / attack;
    }
    if (decay == 0) {
      decaySlope = 1;
    } else {
      decaySlope = (max - sustain) / decay;
    }
    state = State.ATTACK;
  }

  /**
   * Invoked when the note is released.
   */
  public void noteOff() {
    state = State.RELEASE;
    releaseStartValue = lastValue;
    if (release == 0) {
      releaseSlope = 1;
    } else {
      releaseSlope = (releaseStartValue - min) / release;
    }
    releaseStart = currentSample;
    releaseEnd = currentSample + release;
  }

  /**
   * @return true when the note has finished playing.
   */
  public boolean isReleased() {
    return (state == State.DONE);
  }

  @Override
  public double getValue() {
    currentSample++;
    double value = 0;
    if (state == State.ATTACK || state == State.DECAY) {
      if (currentSample > decayEnd) {
        state = State.SUSTAIN;
      } else if (currentSample > attack) {
        state = State.DECAY;
      }
    }
    if (state == State.SUSTAIN) {
      if (sustain <= 0) {
        state = State.DONE;
      }
    }
    if (state == State.RELEASE) {
      if (currentSample > releaseEnd) {
        state = State.DONE;
      }
    }
    switch (state) {
      case ATTACK:
        value = currentSample * attackSlope + min;
        value = Math.min(value, max);
        break;
      case DECAY:
        value = max - (currentSample - attack) * decaySlope;
        value = Math.max(value, sustain);
        break;
      case SUSTAIN:
        value = sustain;
        break;
      case RELEASE:
        value = releaseStartValue -
            (currentSample - releaseStart) * releaseSlope;
        value = Math.max(value, min);
        break;
      case DONE:
        value = min;
        break;
      default:
        throw new IllegalStateException("Unhandled state: " + state);
    }
    lastValue = value;
    return value;
  } 

}
