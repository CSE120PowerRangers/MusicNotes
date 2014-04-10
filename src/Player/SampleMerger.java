package Player;

import MusicSheet.NoteType;
import MusicSheet.TimeSignature;

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
	public double[] mergeSignatureToStaff(double[] signatureSample,
			double[] staffSample) {
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
	public double[] mergeMeasureToSignature(double[] measureSample,
			double[] signatureSample) {
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
	public double[] mergeChordToMeasure(double[] chordSample,
			double[] measureSample, TimeSignature timeSig, int tempo,
			int chordPositionInMeasure) {
		int beatsPerMeasure, beatNote, sampleLengthOfMeasure, chordPositionInSample, sampleLengthOfMeasureDivision;
		double[] newMeasureSample = null;

		beatsPerMeasure = this.getBeatsPerMeasure(timeSig);
		beatNote = this.getMeasureBeatNote(timeSig); // Either quarter(4) or
		// eighth(8);

		// Calculate the sample length for a measure division
		if (beatNote == 4) {
			// Quarter notes get the beat
			sampleLengthOfMeasureDivision = this.getSampleLengthOfNote(
					NoteType.QUARTER_NOTE, timeSig, tempo);
			// Multiply by beats per measure
			sampleLengthOfMeasure = sampleLengthOfMeasureDivision
					* beatsPerMeasure;
		} else {
			// Eighth notes get the beat
			sampleLengthOfMeasureDivision = this.getSampleLengthOfNote(
					NoteType.EIGHTH_NOTE, timeSig, tempo);
			// Multiply by beats per measure
			sampleLengthOfMeasure = sampleLengthOfMeasureDivision
					* beatsPerMeasure;
		}

		// Determine if we need to create the measure
		if (measureSample == null) {
			// Create the new measure sample with the calculated length
			newMeasureSample = new double[sampleLengthOfMeasure];
		} else {
			newMeasureSample = measureSample;
		}
		// Determine how to place the chordSample
		if (chordSample != null) {
			// Determine position in sample
			chordPositionInSample = chordPositionInMeasure
					* sampleLengthOfMeasureDivision;

			// Combine sample information
			for (int i = 0; i < chordSample.length; i++) {
				newMeasureSample[i + chordPositionInSample] += chordSample[i];
			}
		}
		else {
			// chordSample is null, nothing to be done
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
	public int getSampleLengthOfNote(NoteType n, TimeSignature t, int tempo) {
		double beats, noteDurationInSeconds;
		int beatNote, minNoteSampleSize;

		beatNote = getMeasureBeatNote(t);
		beats = getBeatLengthOfNote(n, beatNote);

		noteDurationInSeconds = beats * (tempo / 60);

		minNoteSampleSize = (int) Math.floor(noteDurationInSeconds
				* this.SAMPLE_RATE);

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
	public int getBeatsPerMeasure(TimeSignature t) {
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
	public int getMeasureBeatNote(TimeSignature t) {
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
