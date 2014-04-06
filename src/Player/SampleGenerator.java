package Player;

import MusicSheet.*;

public class SampleGenerator {
	private static final NoteTable FREQUENCIES = new NoteTable();
	
	private final int SAMPLE_RATE;
	private byte[] activeSample;
	private Sheet activeSheet;
	
	SampleGenerator(Sheet s, int sampleRate) {
		this.activeSheet = new Sheet(s);
		this.SAMPLE_RATE = sampleRate;
	}
	
	public void createSample() {
		int sampleSize = calculateLengthOfSheetSample();
	}
	
	public void createSample(Sheet s) {
		this.activeSheet = new Sheet(s);
	}
	
	public void createSample(Staff s) {
		
	}
	
	public void createSample(Signature s) {
		
	}
	
	public void createSample(Measure m) {
		
	}
	
	public void createSample(Chord c) {
		
	}
	
	// Generating a sample of a note is just the frequency
	public void createSample(Note n) {
		
	}
	
	// Need to calculate the sample length of the whole sample
	private int calculateLengthOfSheetSample() {
		/*
		 * 
		 */
		return 0;
	}
	
	private double getDuration(NoteType n){
		return 0.0;
	}
	

}
