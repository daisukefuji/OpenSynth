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

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * A thread that continually outputs synthesizer audio.
 */
public class SynthTrack {
    private static Logger LOG = Logger.getLogger(SynthTrack.class.getSimpleName());

    public static final int SAMPLE_RATE_HZ = 44100;
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE_HZ,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

    private SynthRunner mRunner;
    private Thread mThread;
    public Controller mController;

    public SynthTrack() {
        mController = new Controller();
    }

    public void start() {
        mRunner = new SynthRunner(mController);
        mThread = new Thread(mRunner, "SynthTrack");
        mThread.start();
        LOG.info("SynthTrack started");
    }

    public Controller getController() {
        return mController;
    }

    /**
     * Stops the synth Thread.
     */
    public void shutdown() {
        mRunner.beginShutdown();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // We expect SynthRunner to stop quickly
            throw new RuntimeException(e);
        }
        LOG.info("SynthTrack shutdown");
    }

    private static class SynthRunner implements Runnable {
        private final AudioTrack mTrack;
        private boolean mRunning = false;
        private Controller mController;

        public SynthRunner(Controller controller) {
            mController = controller;
            mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_HZ,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    BUFFER_SIZE, AudioTrack.MODE_STREAM);
            mRunning = true;
        }


        @Override
        public void run() {
            LOG.info("run");
            mTrack.play();
            short chunk[] = new short[BUFFER_SIZE];
            while (isRunning()) {
                if (mController.isReleased()) {
                    Arrays.fill(chunk, (short)0);
                    continue;
                }
                mController.getSamples(chunk, BUFFER_SIZE);
                mTrack.write(chunk, 0, BUFFER_SIZE);
            }
            mTrack.stop();
            LOG.info("stop");
        }

        private synchronized boolean isRunning() {
            return mRunning;
        }

        /**
         * Tells the synth mRunner to begin stopping.
         */
        public synchronized void beginShutdown() {
            mRunning = false;
        }
    };
}
