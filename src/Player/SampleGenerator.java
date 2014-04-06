package Player;

import MusicSheet.*;

public class SampleGenerator {
	private static final NoteTable FREQUENCIES = new NoteTable();
	
	private byte[] activeSample;
	private Sheet activeSheet;
	
	SampleGenerator(Sheet s) {
		this.activeSheet = new Sheet(s);
		calculateLengthOfSheetSample();
	}
	
	public void createSample() {
		
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
	
	// Need to calculate the length of the whole sample
	private int calculateLengthOfSheetSample() {
		return 0;
	}
	
	private double getDuration(NoteType n){
		return 0.0;
	}
	

}
