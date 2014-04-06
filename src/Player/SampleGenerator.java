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
		this.activeSample = new byte[sampleSize];
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
	
	public byte[] getActiveSampleChunk(int startIndex, int length){
		byte[] newSample = new byte[length];
		System.arraycopy(this.activeSample, startIndex, newSample, 0, length);
		return newSample;
	}
	
	// Need to calculate the sample length of the whole sample
	private int calculateLengthOfSheetSample() {
		/*
		 * Tempo = beats per minute;
		 * Duration of a Signature in seconds = Total beats *(Tempo / 60)
		 * Duration of whole Sheet in seconds =  Sum(Signatures in a staff)
		 * 
		 * Beats per Signature = Beats per Measure * NumMeasures
		 * Beats per Measure = f(TimeSignature)
		 * 
		 * SampleRate = Samples/Second
		 * 
		 * SampleLength = Sum(Signature Duration) * SampleRate
		 * 			    = Sum(Total beats * (Tempo / 60)) * SampleRate
		 */
		
		// Calculate the number of beats on the Sheet
		
		int numSignatures = this.activeSheet.getStaff(0).getSize();
		int numMeasures, beatsPerMeasure, beatsPerSignature, tempo, minSampleSize, finalSampleSize;
		double totalDuration = 0;
		TimeSignature t;
		
		for(int i = 0; i < numSignatures; i++) {
			numMeasures = this.activeSheet.getStaff(0).getSize();
			
			t = this.activeSheet.getStaff(0).getSignature(i).getTimeSignature();
			beatsPerMeasure = getBeatsPerMeasure(t);
			
			beatsPerSignature = beatsPerMeasure * numMeasures;
			tempo = this.activeSheet.getStaff(0).getSignature(i).getTempo();
			
			totalDuration += beatsPerSignature * ((double) tempo / 60);
		}
		
		// Calculate the minimum length
		minSampleSize = (int)Math.floor(totalDuration * this.SAMPLE_RATE);
		
		// Get the biggest number greater than the minSampleSize that's a multiple of the SAMPLE_RATE
		finalSampleSize = 0;
		while(finalSampleSize < minSampleSize) {
			finalSampleSize += this.SAMPLE_RATE;
		}
		return finalSampleSize;
	}
	
	private int getBeatsPerMeasure(TimeSignature t) {
		int beats = 0;
		switch(t){
		case FOUR_FOUR:
			beats = 4;
			break;
		case SEVEN_EIGHT:
			beats = 7;
			break;
		case SIX_EIGHT:
			beats = 6;
			break;
		case THREE_EIGHT:
			beats = 3;
			break;
		case THREE_FOUR:
			beats = 3;
			break;
		case TWO_FOUR:
			beats = 2;
			break;
		default:
			// Unreachable hopefully
			beats = -1;
			break;
		}
		
		return beats;
	}
	
	private int getMeasureBeatNote(TimeSignature t) {
		int beat = 0;
		switch(t){
		case FOUR_FOUR:
			beat = 4;
			break;
		case SEVEN_EIGHT:
			beat = 8;
			break;
		case SIX_EIGHT:
			beat = 8;
			break;
		case THREE_EIGHT:
			beat = 8;
			break;
		case THREE_FOUR:
			beat = 4;
			break;
		case TWO_FOUR:
			beat = 4;
			break;
		default:
			// Shouldn't happen.
			beat = -1;
			break;
		}
		return beat;
	}
	
	// Used in calculating where a note sample goes in the sheet sample
	private int getSampleLengthOfNote(NoteType n, TimeSignature t){
		double beats, noteDurationInSeconds;
		int tempo, beatNote, minNoteSampleSize;
		
		beatNote = getMeasureBeatNote(t);
		beats = getBeatLengthOfNote(n, beatNote);
		tempo = this.activeSheet.getStaff(0).getSignature(0).getTempo();
		
		noteDurationInSeconds = beats * (tempo / 60);
		
		minNoteSampleSize = (int)Math.floor(noteDurationInSeconds * this.SAMPLE_RATE);
		
		return minNoteSampleSize;

	}
	
	private double getBeatLengthOfNote(NoteType n, int beatNote) {
		double beatSize = 0;
		switch(n) {
		case EIGHTH_NOTE:
			beatSize = (4.0/32.0) * beatNote;
			break;
		case EIGTH_REST:
			beatSize = (4.0/32.0) * beatNote;
			break;
		case HALF_NOTE:
			beatSize = (16.0/32.0) * beatNote;
			break;
		case HALF_REST:
			beatSize = (16.0/32.0) * beatNote;
			break;
		case NOTANOTE:
			// No length associated with it
			beatSize = 0;
			break;
		case QUARTER_NOTE:
			beatSize = (8.0/32.0) * beatNote;
			break;
		case QUARTER_REST:
			beatSize = (8.0/32.0) * beatNote;
			break;
		case SIXTEENTH_NOTE:
			beatSize = (2.0/32.0) * beatNote;
			break;
		case SIXTEENTH_REST:
			beatSize = (2.0/32.0) * beatNote;
			break;
		case THIRTYSECOND_NOTE:
			beatSize = (1.0/32.0) * beatNote;
			break;
		case THIRTYSECOND_REST:
			beatSize = (1.0/32.0) * beatNote;
			break;
		case WHOLE_NOTE:
			beatSize = beatNote;
			break;
		case WHOLE_REST:
			beatSize = beatNote;
			break;
		default:
			beatSize = -1;
			break;
		}
		return beatSize;
	}
	

}
