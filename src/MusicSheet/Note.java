<<<<<<< HEAD:src/Note.java
/**
 * The note class contains all of the relevant information about a particular note on a sheet of music.
 * 
 * The note takes in three parameters. The first two specify what note is being played and how long it's being played for.
 * The sample rate is for generating the sound sample of the note based on its frequency and duration.
 * 
 * Contents are subject to change as we iterate through the project
 * @author Robert Wang
 */

=======
package MusicSheet;
>>>>>>> fe84f7c041eef09e4a9338696bfaec60f935d9c9:src/MusicSheet/Note.java
public class Note {
	private int frequency; //hz
	private int duration; //milliseconds
	
<<<<<<< HEAD:src/Note.java
=======
	public Note() {
		frequency = 0;
		duration = 0;
	}
	
	
>>>>>>> fe84f7c041eef09e4a9338696bfaec60f935d9c9:src/MusicSheet/Note.java
	/**
	 * Note() is a constructor that creates a note with a given tone, duration, and sample rate
	 * @param frequency is the tone of the note and specifies the sound of the note/what note it is
	 * @param duration is how long the note is played for in milliseconds
	 */
	public Note(int frequency, int duration, int sampleRate) {
		this.frequency = frequency;
		this.duration = duration;
	}
	
	/**
	 * Sets the frequency of the note
	 * @param newFreq
	 */	
	public void setFrequency(int newFreq) {
		frequency = newFreq;
	}
	
	/**
	 * Sets the duration of the note
	 * @param newDuration
	 */
	public void setDuration(int newDuration) {
		duration = newDuration;
	}
	
<<<<<<< HEAD:src/Note.java
	/**
	 * Sets the sample rate of the note
	 * @param newSampleRate
	 */
	public void setSampleRate(int newSampleRate) {
		sampleRate = newSampleRate;
	}
	
=======
>>>>>>> fe84f7c041eef09e4a9338696bfaec60f935d9c9:src/MusicSheet/Note.java
	/**
	 * Gets the frequency of the note
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * Gets the duration of the note
	 * @return duration
	 */
	public int getDuration() {
		return duration;
	}
}
