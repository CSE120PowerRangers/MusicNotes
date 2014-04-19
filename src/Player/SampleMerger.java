package Player;

import MusicSheet.Measure;
import MusicSheet.NoteType;
import MusicSheet.EnumTimeSignature;

public class SampleMerger {
	private int SAMPLE_RATE;

	public SampleMerger(int sampleRate) {
		SAMPLE_RATE = sampleRate;
	}

	// Combines Staff Samples to form Sheet Samples
	public double[] mergeStaffToSheet(double[] staffSample, double[] sheetSample) {
		double[] newSheetSample = null;

		// If staffSample is shorter than sheetSample and neither are null
		if (!(sheetSample == null || staffSample == null)
				&& (staffSample.length > sheetSample.length)) {
			// Create with size equal to bigger array
			newSheetSample = new double[sheetSample.length];

			// Initialize with bigger array
			for (int i = 0; i < sheetSample.length; i++) {
				newSheetSample[i] = sheetSample[i];
			}

			// Add smaller arrays elements
			for (int i = 0; i < staffSample.length; i++) {
				newSheetSample[i] += staffSample[i];
			}
		}

		// Else if staffSample is longer than sheetSample and neither are null
		else if (!(sheetSample == null || staffSample == null)
				&& (staffSample.length < sheetSample.length)) {
			// Create with size equal to bigger array
			newSheetSample = new double[staffSample.length];

			// Initialize with bigger array
			for (int i = 0; i < staffSample.length; i++) {
				newSheetSample[i] = staffSample[i];
			}

			// Add smaller array elements
			for (int i = 0; i < sheetSample.length; i++) {
				newSheetSample[i] += sheetSample[i];
			}
		} else if (sheetSample == null) {
			newSheetSample = staffSample;
		} else if (staffSample == null) {
			newSheetSample = sheetSample;
		}

		return newSheetSample;
	}

	// Appends Signature samples into Staff Samples
	public double[] mergeSignatureToStaff(double[] signatureSample, double[] staffSample) {
		double[] newStaffSample;
		int sampleLength;

		// Combining in this case is simple -- just append them
		if (!(staffSample == null || signatureSample == null)) {
			sampleLength = staffSample.length + signatureSample.length;
			// Create appropriate length new sample
			newStaffSample = new double[sampleLength];

			// Copy old samples over
			for (int i = 0; i < staffSample.length; i++) {
				newStaffSample[i] = staffSample[i];
			}

			for (int i = 0; i < signatureSample.length; i++) {
				newStaffSample[i + staffSample.length] = signatureSample[i];
			}
		}
		// Either Staff is null
		else if (staffSample == null) {
			newStaffSample = signatureSample;
		}
		// Or Signature is null
		else {
			newStaffSample = staffSample;
		}

		return newStaffSample;
	}

	// Appends Measure samples to a Signature Samples
	public double[] mergeMeasureToSignature(double[] measureSample, double[] signatureSample) {
		double[] newSignatureSample;
		int sampleLength;

		// Combining in this case is simple -- just append them
		if (!(signatureSample == null || measureSample == null)) {
			sampleLength = signatureSample.length + measureSample.length;
			// Create appropriate length new sample
			newSignatureSample = new double[sampleLength];

			// Copy old samples over
			for (int i = 0; i < signatureSample.length; i++) {
				newSignatureSample[i] = signatureSample[i];
			}

			for (int i = 0; i < measureSample.length; i++) {
				newSignatureSample[i + signatureSample.length] = measureSample[i];
			}
		}
		// Either Signature is null
		else if (signatureSample == null) {
			newSignatureSample = measureSample;
		}
		// Or measure is null
		else {
			newSignatureSample = signatureSample;
		}

		return newSignatureSample;
	}

	/**
	 * Places chord samples in the correct spot in a measure sample
	 * 
	 * @param chordSample
	 * @param measureSample
	 * @param timeSig
	 * @param tempo
	 * @param chordPositionInMeasure
	 * @return newMeasureSample -- SHOULD NEVER BE NULL
	 */
	public double[] mergeChordToMeasure(double[] chordSample, double[] measureSample, EnumTimeSignature timeSig, int tempo, int chordPositionInMeasure) {
		int beatsPerMeasure, beatNote, sampleLengthOfMeasure, chordPositionInSample, sampleLengthOfMeasureDivision;
		double[] newMeasureSample = null;

		beatsPerMeasure = getBeatsPerMeasure(timeSig);
		beatNote = getMeasureBeatNote(timeSig); // Either quarter(4) or eighth(8);

		// Calculate the sample length for a measure division
		// This actually calculates the sample length for a note type
		// The divisions here are actually in eighth notes
		
		//Default sample length is length of measure division * number of divisions
		//number of divisions = #beats in measure / type of beat * smallest beat (can assume to be 8 for now)
		int divisionType = Measure.getDivisionType();
		double lengthScale = beatsPerMeasure/beatNote;
		
		//This should be okay since the result should always end up as an integral type
		//The divisionType is always equal to or is a multiple of the beat
		//i.e. In 3/4 time, you have 3 quarter notes in a measure
		//However the measure can support divisions down to eighth or sixteen notes, which are multiples of the quarter note
		//This allows for flexible sample lengths and measures
		int numDivisions = (int) (divisionType * lengthScale);
		
		NoteType nType;
		if(divisionType == 4) {
			nType = NoteType.QUARTER_NOTE;
		} else if(divisionType == 8) {
			nType = NoteType.EIGHTH_NOTE;
		} else {
			//Default note type will be quarter
			nType = NoteType.QUARTER_NOTE;
		}

		sampleLengthOfMeasureDivision = getSampleLengthOfNote(nType, timeSig, tempo);
		//Default measure sample length, will override if chord sample length + chord start positition > sample length
		sampleLengthOfMeasure = sampleLengthOfMeasureDivision * numDivisions;

		if (measureSample == null) {
			newMeasureSample = new double[sampleLengthOfMeasure];
		} else {
			newMeasureSample = measureSample;
		}
		
		
		// Determine how to place the chordSample
		if (chordSample != null) {
			//Determine position in sample
			chordPositionInSample = chordPositionInMeasure * sampleLengthOfMeasureDivision;
			if(chordSample.length + chordPositionInSample > sampleLengthOfMeasure) {
				//Set the new length of the measure to be the chord sample length + its position in the measure
				sampleLengthOfMeasure = chordSample.length + chordPositionInSample;
				newMeasureSample = new double[sampleLengthOfMeasure];
				
				//Copy values over
				System.arraycopy(measureSample, 0, newMeasureSample, 0, measureSample.length);
			}

			// Combine sample information
			for (int i = 0; i < chordSample.length; i++) {
				newMeasureSample[i + chordPositionInSample] += chordSample[i];
			}
		}

		return newMeasureSample;
	}

	// Adds a note sample to a chord sample
	public double[] mergeNoteToChord(double[] noteSample, double[] chordSample) {
		double[] newChordSample = null;

		// If noteSample is shorter than chordSample and neither are null
		if (!(chordSample == null || noteSample == null)
				&& (noteSample.length <= chordSample.length)) {
			// Create with size equal to bigger array
			newChordSample = new double[chordSample.length];

			// Initialize with bigger array
			for (int i = 0; i < chordSample.length; i++) {
				newChordSample[i] = chordSample[i];
			}

			// Add smaller arrays elements
			for (int i = 0; i < noteSample.length; i++) {
				newChordSample[i] += noteSample[i];
			}
		}
		// Else if noteSample is longer than chordSample and neither are null
		else if (!(chordSample == null || noteSample == null)
				&& (noteSample.length > chordSample.length)) {
			// Create with size equal to bigger array
			newChordSample = new double[noteSample.length];

			// Initialize with bigger array
			for (int i = 0; i < noteSample.length; i++) {
				newChordSample[i] = noteSample[i];
			}

			// Add smaller array elements
			for (int i = 0; i < chordSample.length; i++) {
				newChordSample[i] += chordSample[i];
			}
		} else if (chordSample == null) {
			// newChordSample = noteSample;
			newChordSample = noteSample;
		} else if (noteSample == null) {
			// newChordSample = chordSample
			newChordSample = chordSample;
		}

		return newChordSample;
	}

	/*
	 * SAMPLE LENGTH FUNCTIONS
	 */

	/**
	 * Calculates the sample length of a note
	 * 
	 * @param n
	 *            - type of note
	 * @param t
	 *            - time signature in sheet
	 * @return
	 */
	public int getSampleLengthOfNote(NoteType n, EnumTimeSignature t, int tempo) {
		double beats, noteDurationInSeconds;
		int beatNote, minNoteSampleSize;

		beatNote = getMeasureBeatNote(t);
		beats = getBeatLengthOfNote(n, beatNote);

		noteDurationInSeconds = beats * (tempo / 60);

		minNoteSampleSize = (int) Math.floor(noteDurationInSeconds * SAMPLE_RATE);

		return minNoteSampleSize;
	}

	/*
	 * BEAT FUNCTIONS
	 */

	/**
	 * Returns the number of beats in a measure
	 * 
	 * @param t
	 * @return
	 */
	public int getBeatsPerMeasure(EnumTimeSignature t) {
		int beats = 0;
		switch (t) {
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
	 * 
	 * @param n
	 * @param beatNote
	 * @return
	 */
	public double getBeatLengthOfNote(NoteType n, int beatNote) {
		double beatSize = 0;
		switch (n) {
		case EIGHTH_NOTE:
			beatSize = (4.0 / 32.0) * beatNote;
			break;
		case EIGTH_REST:
			beatSize = (4.0 / 32.0) * beatNote;
			break;
		case HALF_NOTE:
			beatSize = (16.0 / 32.0) * beatNote;
			break;
		case HALF_REST:
			beatSize = (16.0 / 32.0) * beatNote;
			break;
		case NOTANOTE:
			// No length associated with it
			beatSize = 0;
			break;
		case QUARTER_NOTE:
			beatSize = (8.0 / 32.0) * beatNote;
			break;
		case QUARTER_REST:
			beatSize = (8.0 / 32.0) * beatNote;
			break;
		case SIXTEENTH_NOTE:
			beatSize = (2.0 / 32.0) * beatNote;
			break;
		case SIXTEENTH_REST:
			beatSize = (2.0 / 32.0) * beatNote;
			break;
		case THIRTYSECOND_NOTE:
			beatSize = (1.0 / 32.0) * beatNote;
			break;
		case THIRTYSECOND_REST:
			beatSize = (1.0 / 32.0) * beatNote;
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
	 * 
	 * @param t
	 * @return
	 */
	public int getMeasureBeatNote(EnumTimeSignature t) {
		int beat = 0;
		switch (t) {
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
