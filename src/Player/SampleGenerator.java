package Player;

import MusicSheet.*;

public class SampleGenerator {
	private static final NoteTable FREQUENCIES = new NoteTable();
	private final SampleMerger Merger;

	private final int SAMPLE_RATE;
	private byte[] activeSample;
	private Sheet activeSheet;

	SampleGenerator(Sheet s, int sampleRate) {
		activeSheet = new Sheet(s);
		this.SAMPLE_RATE = sampleRate;
		Merger = new SampleMerger(sampleRate);
	}

	public void createSample() {
		int sampleSize = calculateLengthOfSheetSample();

		activeSample = new byte[sampleSize];

		// WARNING -- SPAGHETTI CODE -- REFACTOR LATER

		double[] sheetSample;

		//This next line was how I reorganized the generation
		sheetSample = generateSheetSample(activeSheet);

		// Convert the sheet into PCM 16-bit array -- SAMPLE READY
		activeSample = convertToPCMArray16(sheetSample);
	}

	// Normalizes double samples into pure tone samples (Range between -1 and 1)
	public double[] normalizeDoubleSample(double[] sample) {
		// Just look for abs(max/min) and divide by that
		double absoluteMax = Math.abs(sample[0]);
		double[] newSample = new double[sample.length];

		for (int i = 1; i < sample.length; i++) {
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
		if (sample != null) {
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
		} else {
			return null;
		}
	}
	
	// Generates a note sample -- return null for invalid note
	public double[] generateNoteSample(TimeSignature timeSig, int tempo, Note n) {

		int sampleLength = getSampleLengthOfNote(n.getType(), timeSig, tempo);
		double frequencyOfNote = FREQUENCIES.getNoteFrequency(n.getName(), n.getOctave());


		if (sampleLength > 0 && frequencyOfNote > 0) {
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
	
	public double[] generateChordSample(Chord toGenerate, TimeSignature timeSig, int tempo) {
		// Determine the number of chords in the measure
		int numNotes = toGenerate.getSize();
		double[] chordSample = null;
		
		for(int i = 0; i < numNotes; i++) {
			Note currentNote = toGenerate.getNote(i);
			double[] noteSample = generateNoteSample(timeSig, tempo, currentNote);
			chordSample = Merger.mergeNoteToChord(noteSample, chordSample);
		}
		
		return chordSample;
	}
	
	public double[] generateMeasureSample(Measure toGenerate, TimeSignature timeSig, int tempo) {
		int numChords = toGenerate.getSize();
		int chordPosition = -1;
		double[] measureSample = null;
		
		for(int i = 0; i < numChords; i++) {
			Chord c = toGenerate.getChord(i);
			
			if(c != null) {
				chordPosition = i;
				double[] chordSample = generateChordSample(c, timeSig, tempo);
				measureSample = Merger.mergeChordToMeasure(chordSample, measureSample, timeSig, tempo, chordPosition);
			}
		}
		
		return measureSample;
	}
	
	public double[] generateSignatureSample(Signature toGenerate) {
		TimeSignature timeSig = toGenerate.getTimeSignature();
		int numMeasures = toGenerate.getSize();
		int tempo = toGenerate.getTempo();
		double[] signatureSample = null;

		for(int i = 0; i < numMeasures; i++) {
			Measure m = toGenerate.getMeasure(i);
			double[] measureSample = generateMeasureSample(m, timeSig, tempo);
			signatureSample = Merger.mergeMeasureToSignature(measureSample, signatureSample);
		}
		
		return signatureSample;
	}
	
	public double[] generateStaffSample(Staff toGenerate) {
		int numSignatures = toGenerate.getSize();
		double[] staffSample = null;

		for(int i = 0; i < numSignatures; i++) {
			Signature s = toGenerate.getSignature(i);
			double[] signatureSample = generateSignatureSample(s);
			staffSample = Merger.mergeSignatureToStaff(signatureSample, staffSample);
		}
		
		return staffSample;
	}

	public double[] generateSheetSample(Sheet toGenerate) {
		int numStaffs = toGenerate.getStaffSize();
		double[] sheetSample = null;
		
		for(int i = 0; i < numStaffs; i++) {
			Staff s = toGenerate.getStaff(i);
			double[] staffSample = generateStaffSample(s);
			sheetSample = Merger.mergeStaffToSheet(staffSample, sheetSample);
		}
		
		return sheetSample;
	}
	
	// Should be accessed in small intervals
	public byte[] getActiveSampleChunk(int startIndex, int length) {
		byte[] newSample = null;

		// Check the boundaries
		if (startIndex + length < activeSample.length) {
			// Safe to do straight copy
			newSample = new byte[length];
			System.arraycopy(activeSample, startIndex, newSample, 0,
					length);
		} else if (startIndex + length >= activeSample.length) {
			// Init the new sample
			newSample = new byte[length];

			// Copy the remainder of the active sample
			for (int i = 0; i < length; i++) {
				if (startIndex + i < activeSample.length) {
					newSample[i] = activeSample[startIndex + i];
				} else {
					// Fill the buffer with 0's
					newSample[i] = 0;
				}
			}

		} else {
			return null;
		}
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
			numMeasures = this.activeSheet.getStaff(0).getSignature(i)
					.getSize();

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
