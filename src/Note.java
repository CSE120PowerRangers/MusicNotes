public class Note {
	private int frequency; //hz
	private int duration; //milliseconds
	private int sampleRate;
	private double[] sample;
	
	public Note() {
		frequency = 0;
		duration = 0;
		sample = null;
	}
	
	
	/**
	 * Note() is a constructor that creates a note with a given tone, duration, and sample rate
	 * @param frequency is the tone of the note
	 * @param duration is how long the note is played for in milliseconds
	 * @param sampleRate is how many samples per second the note will need when being rendered
	 */
	public Note(int frequency, int duration, int sampleRate) {
		this.frequency = frequency;
		this.duration = duration;
		this.sampleRate = sampleRate;
		
		int numSamples = this.sampleRate*(this.duration / 1000);
		sample = new double[numSamples];
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
	
	
	/**
	 * Sets the sample rate of the note
	 * @param newSampleRate
	 */
	public void setSampleRate(int newSampleRate) {
		sampleRate = newSampleRate;
	}
	
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
	
	/**
	 * Gets the sample rate of the note
	 * @return sampleRate
	 */
	public int getSampleRate() {
		return sampleRate;
	}
}
