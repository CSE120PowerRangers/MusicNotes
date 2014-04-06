package Player;

import MusicSheet.*;

public class SampleGenerator {
	private static final NoteTable FREQUENCIES = new NoteTable();

	private final int SAMPLE_RATE;
	private byte[] activeSample;
	private Sheet activeSheet;

	SampleGenerator(Sheet s, int sampleRate) {
		activeSheet = new Sheet(s);
		this.SAMPLE_RATE = sampleRate;
	}

	public void createSample() {
		int sampleSize = calculateLengthOfSheetSample();

		activeSample = new byte[sampleSize];

		// WARNING -- SPAGHETTI CODE -- REFACTOR LATER

		// Set up some helper vars -- loop limits

		// Signature settings to keep later
		int tempo;
		KeySignature keySig;
		TimeSignature timeSig;

		// Somewhere to store the double samples before combination and
		// conversion
		double[] sheetSample, staffSample, signatureSample, measureSample, chordSample, noteSample;

		// Initialize all to null
		sheetSample = null;
		staffSample = null;
		signatureSample = null;
		measureSample = null;
		chordSample = null;
		noteSample = null;

		// Determine the staffs in the sheet
		int numStaffs = activeSheet.getStaffSize();

		// Start reading the sheet -- For each staff
		for (int staffInd = 0; staffInd < numStaffs; staffInd++) {
			// Determine the signature in the staff
			Staff iStaff = activeSheet.getStaff(staffInd);
			int numSignatures = iStaff.getSize();

			// For each signature in the staff
			for (int signatureInd = 0; signatureInd < numSignatures; signatureInd++) {
				// Determine the measures in the signature
				Signature iSignature = iStaff.getSignature(signatureInd);
				int numMeasures = iSignature.getSize();
				// As well as the settings of the signature
				tempo = iSignature.getTempo();
				timeSig = iSignature.getTimeSignature();
				
				// For each measure in the signature
				for (int measureInd = 0; measureInd < numMeasures; measureInd++) {
					// Determine the number of chords in the measure
					Measure iMeasure = iSignature.getMeasure(measureInd);
					int numChords = iMeasure.getSize();

					// For each chord in the measure
					for (int chordInd = 0; chordInd < numChords; chordInd++) {
						// Need this when we find a chord
						int numNotes = 0;
						int chordPosition = -1;
						Chord iChord = iMeasure.getChord(chordInd);
						
						// If the chord exists
						if (!iChord.equals(null)) {
							// Record the number of notes in the chord and its position
							numNotes= iChord.getSize();
							chordPosition = chordInd;
						} else {
							// Null chord, don't look for notes
							numNotes = 0;
						}

						// For each note in the chord
						for (int noteInd = 0; noteInd < numNotes; noteInd++) {
							// Generate double SAMPLE!
							Note currentNote = iChord.getNote(noteInd);

							// Generate a double sample
							noteSample = generateNoteSample(timeSig, tempo, currentNote);

							// Combine it with the current chord sample
							chordSample = combineNoteSampleIntoChordSample(noteSample, chordSample);
						}
						
						// Determine where in the sample the chord should be placed
						if (chordPosition > -1) {
							// Place chord samples correctly
							measureSample = mergeChordToMeasure(chordSample, measureSample, timeSig, tempo, chordPosition);
						}
					}

					// Append Measure samples to Signature Samples
					signatureSample = mergeMeasureToSignature(measureSample, signatureSample);
				}

				// Append Signature samples to Staff Samples
				staffSample = mergeSignatureToStaff(signatureSample, staffSample);
			}

			// Combine Staff samples into Sheet samples
			sheetSample = mergeStaffToSheet(staffSample,sheetSample);
		}

		// Convert the sheet into PCM 16-bit array -- SAMPLE READY
		activeSample = convertToPCMArray16(sheetSample);
	}

	// Normalizes double samples into pure tone samples (Range between -1 and 1)
	public double[] normalizeDoubleSample(double[] sample) {
		// Just look for abs(max/min) and divide by that
		double absoluteMax = 0;
		double[] newSample = new double[sample.length];

		for (int i = 0; i < sample.length; i++) {
			if (Math.abs(sample[i]) > absoluteMax) {
				absoluteMax = Math.abs(sample[i]);
			}
		}

		// Divide each value by absolute max
		for (int i = 0; i < sample.length; i++) {
			newSample[i] = sample[i] / absoluteMax;
		}

		return newSample;
	}

	// Converts double sample into PCM 16-bit array for playback
	public byte[] convertToPCMArray16(double[] sample) {

		if (!sample.equals(null)) {
			int idx = 0;
			byte[] newSamplePCM = new byte[2 * sample.length];
			double[] normalizedSample = normalizeDoubleSample(sample);

			for (final double dVal : normalizedSample) {
				// Scale to max amplitude -- LOOK AT THIS LATER
				final short val = (short) ((dVal * 32767));

				// In 16 bit wav PCM, first byte is the low order byte
				newSamplePCM[idx++] = (byte) (val & 0x00ff);
				newSamplePCM[idx++] = (byte) ((val & 0xff00) >>> 8);
			}

			return newSamplePCM;
		} else if (sample.equals(null)) {
			return null;
		} else {
			return null;
		}
	}

	// Combines Staff Samples to form Sheet Samples
	private double[] mergeStaffToSheet(double[] staffSample,
			double[] sheetSample) {
		double[] newSheetSample = null;

		// If noteSample is shorter than sheetSample and neither are null
		if ((staffSample.length <= sheetSample.length)
				&& !(sheetSample.equals(null) || staffSample.equals(null))) {
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
		else if ((staffSample.length > sheetSample.length)
				&& !(sheetSample.equals(null) || staffSample.equals(null))) {
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
		} else if (sheetSample.equals(null)) {
			// newsheetSample = staffSample;
			return staffSample;
		} else if (staffSample.equals(null)) {
			// newsheetSample = sheetSample
			return sheetSample;
		} else {
			// Not sure what would happen here
		}

		return newSheetSample;
	}

	// Appends Signature samples into Staff Samples
	private double[] mergeSignatureToStaff(
			double[] signatureSample, double[] staffSample) {
		double[] newStaffSample;
		int sampleLength;

		// Combining in this case is simple -- just append them
		if (!(staffSample.equals(null) || signatureSample.equals(null))) {
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

			return newStaffSample;
		}
		// Either Staff is null
		else if (staffSample.equals(null)) {

			return signatureSample;
		}
		// Or Signature is null
		else if (signatureSample.equals(null)) {
			return staffSample;
		}
		// Or it's all broken
		else {
			return null;
		}
	}

	// Appends Measure samples to a Signature Samples
	private double[] mergeMeasureToSignature(
			double[] measureSample, double[] signatureSample) {
		double[] newSignatureSample;
		int sampleLength;

		// Combining in this case is simple -- just append them
		if (!(signatureSample.equals(null) || measureSample.equals(null))) {
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

			return newSignatureSample;
		}
		// Either Signature is null
		else if (signatureSample.equals(null)) {

			return measureSample;
		}
		// Or measure is null
		else if (measureSample.equals(null)) {
			return signatureSample;
		}
		// Or it's all broken
		else {
			return null;
		}
	}

	// Appends Chord Samples hopefully correctly
	private double[] mergeChordToMeasure(double[] chordSample,
			double[] measureSample, TimeSignature timeSig, int tempo,
			int chordPositionInMeasure) {
		int beatsPerMeasure, beatNote, sampleLengthOfMeasure, chordPositionInSample, sampleLengthOfMeasureDivision;
		double[] newMeasureSample = null;

		beatsPerMeasure = this.getBeatsPerMeasure(timeSig);
		beatNote = this.getMeasureBeatNote(timeSig); // Either quarter(4) or
												// eighth(8);

		// Determine if we measureSample needs to be initialized first
		if (measureSample.equals(null)) {
			if (beatNote == 4) {
				sampleLengthOfMeasure = this.getSampleLengthOfNote(
						NoteType.QUARTER_NOTE, timeSig, tempo) * beatsPerMeasure;
			} else {
				sampleLengthOfMeasure = this.getSampleLengthOfNote(
						NoteType.EIGHTH_NOTE, timeSig, tempo) * beatsPerMeasure;
			}

			newMeasureSample = new double[sampleLengthOfMeasure];
		}
		
		// Get the sample length of a division in a measure
		sampleLengthOfMeasureDivision = this.getSampleLengthOfNote(NoteType.EIGHTH_NOTE, timeSig, tempo);
		
		
		if(!chordSample.equals(null)){
			// Determine position in sample
			chordPositionInSample = chordPositionInMeasure * sampleLengthOfMeasureDivision;
			
			// Combine sample information
			for(int i = 0; i < chordSample.length; i++) {
				newMeasureSample[i + chordPositionInSample] += chordSample[i];
			}
		}
		// chordSample is null, nothing to be done
		else if(chordSample.equals(null)) {
			return measureSample;
		}
		// Shouldn't happen but return null anyways.
		return null;
	}

	// Adds a note sample to a chord sample
	private double[] combineNoteSampleIntoChordSample(double[] noteSample,
			double[] chordSample) {
		double[] newChordSample = null;

		// If noteSample is shorter than chordSample and neither are null
		if ((noteSample.length <= chordSample.length)
				&& !(chordSample.equals(null) || noteSample.equals(null))) {
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
		else if ((noteSample.length > chordSample.length)
				&& !(chordSample.equals(null) || noteSample.equals(null))) {
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
		} else if (chordSample.equals(null)) {
			// newChordSample = noteSample;
			return noteSample;
		} else if (noteSample.equals(null)) {
			// newChordSample = chordSample
			return chordSample;
		} else {
			// Not sure what would happen here
		}

		return newChordSample;
	}

	// Generates a note sample -- return null for invalid note
	public double[] generateNoteSample(TimeSignature timeSig, int tempo, Note n) {

		int sampleLength = getSampleLengthOfNote(n.getType(), timeSig, tempo);
		double frequencyOfNote = FREQUENCIES.getNoteFrequency(n.getName(),
				n.getOctave());

		if (sampleLength > 0 && frequencyOfNote > 0) {

			// Create the sample
			double[] noteSample = new double[sampleLength];

			// Fill it out.
			for (int i = 0; i < sampleLength; i++) {
				noteSample[i] = Math.sin(2 * Math.PI * i
						/ (this.SAMPLE_RATE / frequencyOfNote));
			}

			return noteSample;
		} else {
			// Invalid note, return null.
			return null;
		}
	}

	// Should be accessed in small intervals
	public byte[] getActiveSampleChunk(int startIndex, int length) {
		byte[] newSample = new byte[length];
		System.arraycopy(activeSample, startIndex, newSample, 0, length);
		return newSample;
	}

	/*
	 * SAMPLE LENGTH FUNCTIONS
	 */

	/**
	 * Calculates the number of samples required for the whole Sheet
	 * 
	 * @return
	 */
	private int calculateLengthOfSheetSample() {
		/*
		 * Tempo = beats per minute; -----------------------------------------
		 * Duration of a Signature in seconds = ((Total beats / Tempo) * 60);
		 * 
		 * Duration of whole Sheet in seconds = Sum(Signatures in a staff);
		 * 
		 * Beats per Signature = Beats per Measure * NumMeasures; ------------
		 * Beats per Measure = f(TimeSignature);
		 * 
		 * SampleRate = Samples/Second;
		 * 
		 * SampleLength = Sum(Signature Duration) * SampleRate; ---------------
		 * SampleLength = Sum((Total beats / Tempo) * 60)) * SampleRate;
		 */

		// Calculate the number of beats on the Sheet

		int numSignatures = activeSheet.getStaff(0).getSize();
		int numMeasures, beatsPerMeasure, beatsPerSignature, tempo, minSampleSize, finalSampleSize;
		double totalDuration = 0;
		TimeSignature t;

		for (int i = 0; i < numSignatures; i++) {
			numMeasures = activeSheet.getStaff(0).getSize();

			t = activeSheet.getStaff(0).getSignature(i).getTimeSignature();
			beatsPerMeasure = getBeatsPerMeasure(t);

			beatsPerSignature = beatsPerMeasure * numMeasures;
			tempo = activeSheet.getStaff(0).getSignature(i).getTempo();

			totalDuration += ((beatsPerSignature / (double) tempo) * 60);
		}

		// Calculate the minimum length
		minSampleSize = (int) Math.floor(totalDuration * this.SAMPLE_RATE);

		// Get the biggest number greater than the minSampleSize that's a
		// multiple of the SAMPLE_RATE
		finalSampleSize = 0;
		while (finalSampleSize < minSampleSize) {
			finalSampleSize += this.SAMPLE_RATE;
		}
		return finalSampleSize;
	}

	/**
	 * Calculates the sample length of a note
	 * 
	 * @param n
	 *            - type of note
	 * @param t
	 *            - time signature in sheet
	 * @return
	 */
	private int getSampleLengthOfNote(NoteType n, TimeSignature t, int tempo) {
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
	private int getBeatsPerMeasure(TimeSignature t) {
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
	private double getBeatLengthOfNote(NoteType n, int beatNote) {
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
	private int getMeasureBeatNote(TimeSignature t) {
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
