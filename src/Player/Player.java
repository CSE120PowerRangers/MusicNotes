package Player;

import java.util.Queue;

import MusicSheet.*;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Player {
	private static final int SAMPLE_RATE = 16000;


	private SampleGenerator sampleGen; // Creates the samples we interpret
	private AudioTrack soundTrack; 	   // Actually outputs the music
	private Sheet activeSheet;		   // Contains the rendering of the sheet
	
	private byte[] streamBuffer; // Contains whatever will be written to soundTrack

	Player() {
		// Determine constants for our soundTrack
		int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		int bufferSize = 4 * minBufferSize; // Supposedly a decent size for
											// AudioTrack
		
		// Initialize members
		this.streamBuffer = new byte[bufferSize / 2]; // Encourages double
														// buffering

		
		this.sampleGen = new SampleGenerator();
		
		this.soundTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
				SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize,
				AudioTrack.MODE_STREAM);
	}
	
	public void setActiveSheet(Sheet s){
		this.activeSheet = new Sheet(s);
		
		sampleGen.createSample(this.activeSheet);
	}

	public void play() {	
	}

	/*
	 * EXPERIMENTAL -- Getting this to work with Sheet is a pain so far
	 */

	private boolean mStopAudioThreads = false;
	private Thread mSampleThread, mStreamThread;

	// Runnable for creating samples -- experimental right now
	private Runnable mSample = new Runnable() {
		// Render 4 measures at a time
		public void run() {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		}
	};

	// Runnable for streaming music -- experimental right now
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

	private byte[] getNextSample() {
		return null;
	}

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
