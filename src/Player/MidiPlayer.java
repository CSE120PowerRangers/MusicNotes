package Player;

import File.FileMaker;
import MusicSheet.*;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.*;

/*
 * Currently no good way to synthesize midi sounds
 * Best way is to generate a midi file from the sheet
 * and pass that to JetPlayer
 */
public class MidiPlayer {
	private MediaPlayer mPlayer;
		
	public MidiPlayer(Sheet s, Context context){
		initSheet(s, context);
	}
	
	public void initSheet(Sheet s, Context context) {
		// Create internal file for sheet
		FileMaker.writeSheetToMidi(s, context);
		
		try {
			String stringPath = context.getFilesDir().getAbsolutePath() + "/"
					+ FileMaker.TEST_FILENAME;
			Uri filePath = Uri.parse(Uri.encode(stringPath));
			System.out.println(stringPath);
			//mPlayer = new MediaPlayer();
			//mPlayer.setDataSource(context, filePath);
			//mPlayer.prepareAsync();
			mPlayer = MediaPlayer.create(context, filePath);
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}



	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}

	// Play sample midiFile
	public void play() {
		if(!this.isPlaying()) {
			mPlayer.start();
		}
	}

	// Pause jPlayer
	public void pause() {
		if(this.isPlaying()){
			mPlayer.pause();
		}
	}

	// Stop MediaPlayer and reset the resources
	public void stop() {
		if(this.isPlaying()) {
			mPlayer.stop();
		}
		
		mPlayer.reset();
	}
	
	public void initSampleTest(Context context) {
		// Create the test file
		// TODO Make sure file isn't already there
		FileMaker.writeTestFile(context);

		try {
			String stringPath = context.getFilesDir().getAbsolutePath() + "/"
					+ FileMaker.TEST_FILENAME;
			Uri filePath = Uri.parse(Uri.encode(stringPath));

			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(context, filePath);
			mPlayer.prepareAsync();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}
}
