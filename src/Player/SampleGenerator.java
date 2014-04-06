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
		
		// WARNING -- SPAGHETTI CODE -- REFACTOR LATER
		
		// Set up some helper vars -- loop limits
		int numStaffsInSheet, numSignaturesInStaff, numMeasuresInSignature, numChordsInMeasure, numNotesInChord;
		
		// Signature settings to keep later
		int tempo;
		KeySignature keySig;
		TimeSignature timeSig;
		
		// Somewhere to store the double samples before combination and conversion
		double[] staffSample, signatureSample, measureSample, chordSample, noteSample;
		
		// Determine the staffs in the sheet
		numStaffsInSheet = this.activeSheet.getStaffSize();
		
		// Start reading the sheet -- For each staff
		for(int staffInd = 0; staffInd < numStaffsInSheet; staffInd++) {
			
			// Determine the signature in the staff
			numSignaturesInStaff = this.activeSheet.getStaff(staffInd).getSize();
			
			// For each signature in the staff
			for(int signatureInd = 0; signatureInd < numSignaturesInStaff; signatureInd++) {
				
				// Determine the measures in the signature
				numMeasuresInSignature = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getSize();
				
				// As well as the settings of the signature
				tempo = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getTempo();
				keySig = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getKeySignature();
				timeSig = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getTimeSignature();
				
				// For each measure in the signature
				for(int measureInd = 0; measureInd < numMeasuresInSignature; measureInd++) {
					// Determine the number of chords in the measure
					numChordsInMeasure = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getMeasure(measureInd).getSize();
					
					// For each chord in the measure
					for(int chordInd = 0; chordInd < numChordsInMeasure; chordInd++) {
						// Determine the number of notes in the chord -- if a chord exists
						if(!this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getMeasure(measureInd).getChord(chordInd).equals(null)) {
							numNotesInChord = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getMeasure(measureInd).getChord(chordInd).getSize();
						}
						else {
							// Null chord, don't look for notes
							numNotesInChord = 0;
						}
						
						// For each note in the chord
						for(int noteInd = 0; noteInd < numNotesInChord; noteInd++) {
							// Generate byte SAMPLE!
							Note currentNote = this.activeSheet.getStaff(staffInd).getSignature(signatureInd).getMeasure(measureInd).getChord(chordInd).getNote(noteInd);
							
							// Generate a double sample
							noteSample = generateNoteSample(timeSig, keySig, tempo, currentNote);
							
							// Combine it with the current chord sample
							
						}
					}
					
				}
			}
		}
		
	}
	
	// Generates a note sample
	public double[] generateNoteSample(TimeSignature timeSig, KeySignature keySig, int tempo, Note n) {
		double frequencyOfNote;
		return null;
	}
	
	
	// Should be accessed in small intervals
	public byte[] getActiveSampleChunk(int startIndex, int length){
		byte[] newSample = new byte[length];
		System.arraycopy(this.activeSample, startIndex, newSample, 0, length);
		return newSample;
	}
	
	
	/*
	 * SAMPLE LENGTH FUNCTIONS
	 */

	/**
	 * Calculates the number of samples required for the whole Sheet
	 * @return
	 */
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
	
	/**
	 * Calculates the sample length of a note
	 * @param n - type of note
	 * @param t - time signature in sheet
	 * @return
	 */
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
	
	/*
	 * BEAT FUNCTIONS
	 */
	
	/**
	 * Returns the number of beats in a measure
	 * @param t
	 * @return
	 */
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
	
	/**
	 * Returns the beat duration of a note
	 * @param n
	 * @param beatNote
	 * @return
	 */
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
	
	/**
	 * Returns the number corresponding to the note which gets the beat
	 * @param t
	 * @return
	 */
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

	

}
