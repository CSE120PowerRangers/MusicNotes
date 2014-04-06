package Player;

import MusicSheet.*;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Player {
	private static final int SAMPLE_RATE = 16000;


	private SampleGenerator sampleGen; // Creates the samples we interpret
	private AudioTrack soundTrack; 	   // Actually outputs the music
	
	private byte[] streamBuffer; // Contains whatever will be written to soundTrack
	
	private int bufferPosition; // Allows use to keep track of where we want to draw our sample from

	Player() {
		// Determine constants for our soundTrack
		int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		int bufferSize = 4 * minBufferSize; // Supposedly a decent size for
											// AudioTrack
		
		// Initialize members
		this.bufferPosition = 0;
		
		this.streamBuffer = new byte[bufferSize / 2]; // Encourages double
														// buffering

		// Need to initialize later if no sheet passed in
		this.sampleGen = null;
		
		this.soundTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize,
				AudioTrack.MODE_STREAM);
	}
	
	Player(Sheet s) {
		// Determine constants for our soundTrack
		int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		int bufferSize = 4 * minBufferSize; // Supposedly a decent size for
											// AudioTrack
		
		// Initialize members
		this.bufferPosition = 0;
		
		this.streamBuffer = new byte[bufferSize / 2]; // Encourages double
														// buffering

		// Need to initialize later if no sheet passed in
		this.sampleGen = new SampleGenerator(new Sheet(s), Player.SAMPLE_RATE);
		
		this.soundTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize,
				AudioTrack.MODE_STREAM);
	}
	
	// SampleGenerator
	public void initializeSampleGenerator(Sheet s){		
		this.sampleGen = new SampleGenerator(new Sheet(s), Player.SAMPLE_RATE);
	}

	private byte[] getNextSample() {
		byte[] newSample = sampleGen.getActiveSampleChunk(this.bufferPosition, this.streamBuffer.length);
		this.bufferPosition += this.streamBuffer.length;
		return newSample;
	}
	
	public void play() {
		// Open up audio channel to begin streaming
		soundTrack.play();
		
		// Read from the SampleGenerator
		while(!streamBuffer.equals(null)) {  
			streamBuffer = getNextSample();
			soundTrack.write(streamBuffer, 0, streamBuffer.length);
		}
		
		soundTrack.stop();
	}
	
	public void pause() {
		soundTrack.pause();
	}
	
	public void resume() {
		soundTrack.play();
	}
	
	public void stop() {
		soundTrack.pause();
		soundTrack.flush();
	}

	/*
	 * EXPERIMENTAL -- Getting this to work with Sheet is a pain so far
	 */

	private boolean mStopAudioThreads = false;
	private Thread mSampleThread, mStreamThread;

	// Runnable for creating samples
	private Runnable mSample = new Runnable() {
		// Render 4 measures at a time
		public void run() {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		}
	};

	// Runnable for streaming music
	private Runnable mStreams = new Runnable() {
		public void run() {
			// Create a streaming AudioTrack for playback
			int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
					AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT);

			int bufferSize = 4 * minBufferSize; // Supposedly a decent size for
												// AudioTrack
			byte[] streamBuffer = new byte[bufferSize / 2]; // Encourages double
															// buffering

			AudioTrack mMusicTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize,
					AudioTrack.MODE_STREAM);

			mMusicTrack.play();

			while (!mStopAudioThreads) {
				// Fill buffer with processed data
				streamBuffer = getNextSample();

				// Stream samples into AudioTrack
				mMusicTrack.write(streamBuffer, 0, bufferSize / 2);
			}
		}
	};

	private void RunAudioThreads() {
		mStopAudioThreads = false;

		mSampleThread = new Thread(mSample);
		mSampleThread.start();

		mStreamThread = new Thread(mStreams);
		mStreamThread.start();
	}

	private void StopAudioThreads() {
		mStopAudioThreads = true;

		try {
			mSampleThread.join();
			mStreamThread.join();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

}
