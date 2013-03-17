package org.thebends.synth;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.logging.Logger;

/**
 * A thread that continually outputs synthesizer audio.
i*/
class SynthTrack {
  private static Logger LOG = Logger.getLogger(
      SynthTrack.class.getSimpleName());

  private static final int SAMPLE_RATE_HZ = 44100;

  // Give the audio track a large buffer, writing much smaller chunks
  private static final int BUFFER_SIZE = 50 * 1024;
  private static final int CHUNK_SIZE = 4 * 1024;

  private final SynthRunner runner;
  private final Thread thread;

  public SynthTrack() {
    runner = new SynthRunner();
    thread = new Thread(runner, "SynthTrack");
    thread.start();
    LOG.info("SynthTrack started");
  }

  /**
   * Stops the synth thread.
   */
  public void shutdown() {
    runner.beginShutdown();
    try {
      thread.join();
    } catch (InterruptedException e) {
      // We expect SynthRunner to stop quickly
      throw new RuntimeException(e);
    }
    LOG.info("SynthTrack shutdown");
  }

  private static class SynthRunner implements Runnable {
    private final AudioTrack track;
    private boolean running = false;

    public SynthRunner() {
      track = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE_HZ,
          AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
          BUFFER_SIZE, AudioTrack.MODE_STREAM);
      running = true;
    }

    @Override
    public void run() {
      LOG.info("run");
      track.play();
      final short chunk[] = new short[CHUNK_SIZE];
      while (isRunning()) {
/*
        final float chunkFloat[] = SynthJni.getSamples(CHUNK_SIZE);
        for (int i = 0; i < CHUNK_SIZE; ++i) {
          chunk[i] = (short)(chunkFloat[i] * Short.MAX_VALUE);
        }
        track.write(chunk, 0, CHUNK_SIZE);
*/
      }
      track.stop();
      LOG.info("stop");
    }

    private synchronized boolean isRunning() {
      return running;
    }

    /**
     * Tells the synth runner to begin stopping.
     */
    public synchronized void beginShutdown() {
      running = false;
    }

    /**
     * Converts a linear PCM value from a double to a short.
     */
    private static short getShortValue(double value) {
      return (short) (value * Short.MAX_VALUE);
    }
  };
}
