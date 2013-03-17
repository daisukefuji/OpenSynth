/*
 * Copyright 2013 Daisuke Fuji <daisuke@indigo-lab.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thebends.synth;

/**
 * An envelope controls a value over time.
 */
public class Envelope implements Parameter {
    private long mAttack;
    private double mAttackSlope;
    private long mDecay;
    private long mDecayEnd;
    private double mDecaySlope;
    private double mSustain;
    private long mRelease;
    private long mReleaseStart;
    private long mReleaseEnd;
    private double mReleaseSlope;
    private double mMax;
    private double mMin;
    private long mCurrentSample;
    private double mLastValue;
    private double mReleaseStartValue;
    enum State {
        ATTACK,
        DECAY,
        SUSTAIN,
        RELEASE,
        DONE
    };
    private State mState;

    public Envelope() {
        mAttack = 0;
        mAttackSlope = 0.0;
        mDecay = 0;
        mDecayEnd = 0;
        mDecaySlope = 0.0;
        mSustain = 1.0;
        mRelease = 0;
        mReleaseStart = 0;
        mReleaseEnd = 0;
        mReleaseSlope = 0.0;
        mMin = 0.0;
        mMax = 1.0;
        mCurrentSample = 0;
        mReleaseStartValue = 0.0;
        mState = State.DONE;
    }

    /**
     * @param attack in samples
     */
    public void setAttack(long mAttack) {
        this.mAttack = mAttack;
    }

    /**
     * @param decay in samples
     */
    public void setDecay(long mDecay) {
        this.mDecay = mDecay;
    }

    /**
     * @param sustain volume (usually between 0.0 and 1.0)
     */
    public void setSustain(double mSustain) {
        this.mSustain = mSustain;
    }

    /**
     * @param release in samples
     */
    public void setRelease(long mRelease) {
        this.mRelease = mRelease;
    }

    public void setMax(double mMax) {
        this.mMax = mMax;
    }

    public void setMin(double mMin) {
        this.mMin = mMin;
    }

    /**
     * Invoked when the note is pressed.
     */
    public void noteOn() {
        mCurrentSample = 0;
        mDecayEnd = mAttack + mDecay;
        if (mAttack == 0) {
            mAttackSlope = 1;
        } else {
            mAttackSlope = (mMax - mMin) / mAttack;
        }
        if (mDecay == 0) {
            mDecaySlope = 1;
        } else {
            mDecaySlope = (mMax - mSustain) / mDecay;
        }
        mState = State.ATTACK;
    }

    /**
     * Invoked when the note is mReleased.
     */
    public void noteOff() {
        mState = State.RELEASE;
        mReleaseStartValue = mLastValue;
        if (mRelease == 0) {
            mReleaseSlope = 1;
        } else {
            mReleaseSlope = (mReleaseStartValue - mMin) / mRelease;
        }
        mReleaseStart = mCurrentSample;
        mReleaseEnd = mCurrentSample + mRelease;
    }

    /**
     * @return true when the note has finished playing.
     */
    public boolean isReleased() {
        return (mState == State.DONE);
    }

    @Override
    public double getValue() {
        mCurrentSample++;
        double value = 0;
        if (mState == State.ATTACK || mState == State.DECAY) {
            if (mCurrentSample > mDecayEnd) {
                mState = State.SUSTAIN;
            } else if (mCurrentSample > mAttack) {
                mState = State.DECAY;
            }
        }
        if (mState == State.SUSTAIN) {
            if (mSustain <= 0) {
                mState = State.DONE;
            }
        }
        if (mState == State.RELEASE) {
            if (mCurrentSample > mReleaseEnd) {
                mState = State.DONE;
            }
        }
        switch (mState) {
            case ATTACK:
                value = mCurrentSample * mAttackSlope + mMin;
                value = Math.min(value, mMax);
                break;
            case DECAY:
                value = mMax - (mCurrentSample - mAttack) * mDecaySlope;
                value = Math.max(value, mSustain);
                break;
            case SUSTAIN:
                value = mSustain;
                break;
            case RELEASE:
                value = mReleaseStartValue -
                        (mCurrentSample - mReleaseStart) * mReleaseSlope;
                value = Math.max(value, mMin);
                break;
            case DONE:
                value = mMin;
                break;
            default:
                throw new IllegalStateException("Unhandled mState: " + mState);
        }
        mLastValue = value;
        return value;
    } 
}
