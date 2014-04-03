package MusicSheet;

import java.util.Map;
import java.util.HashMap;

public class NoteTable {
	/*
	 * NoteTable is a wrapper class for a lookup table of note frequencies.
	 * Requires note name and octave.
	 */

	private static final int STEP_DIFFERENCE = 57; // half steps from C0 to A4
	private static final int MAX_OCTAVES = 9; // octaves from C0 to C8
	private static final double baseFrequency = 440; // Hz
	private static final double a = Math.pow(2.0, (1.0 / 12.0));

	private static Map<Note, Double> noteFrequency;

	/**
	 * Constructor for the lookup table, fills up the table
	 */
	public NoteTable() {

		noteFrequency = new HashMap<Note, Double>(); // Double is frequency

		double frequency = 0.0; // Stores the current frequency about to
										// be put in the table

		// Fill in the frequencies one octave at a time
		for (int octave = 0; octave < MAX_OCTAVES; octave++) {
			// C
			// Steps away from A4
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE); 
			// Throw it in the table
			noteFrequency.put(new Note(NoteName.C, NoteType.NOTANOTE, octave), frequency); 


			// CSHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 1);
			noteFrequency.put(new Note(NoteName.CSHARP, NoteType.NOTANOTE, octave), frequency);

			// DFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 1);
			noteFrequency.put(new Note(NoteName.DFLAT, NoteType.NOTANOTE, octave), frequency);

			// D
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 2);
			noteFrequency.put(new Note(NoteName.D, NoteType.NOTANOTE, octave), frequency);

			// DSHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 3);
			noteFrequency.put(new Note(NoteName.DSHARP, NoteType.NOTANOTE, octave), frequency);

			// EFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 3);
			noteFrequency.put(new Note(NoteName.EFLAT, NoteType.NOTANOTE, octave), frequency);

			// E
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 4);
			noteFrequency.put(new Note(NoteName.E, NoteType.NOTANOTE, octave), frequency);

			// ESHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 5);
			noteFrequency.put(new Note(NoteName.ESHARP, NoteType.NOTANOTE, octave), frequency);

			// FFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 4);
			noteFrequency.put(new Note(NoteName.FFLAT, NoteType.NOTANOTE, octave), frequency);

			// F
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 5);
			noteFrequency.put(new Note(NoteName.F, NoteType.NOTANOTE, octave), frequency);

			// FSHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 6);
			noteFrequency.put(new Note(NoteName.FSHARP, NoteType.NOTANOTE, octave), frequency);

			// GFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 6);
			noteFrequency.put(new Note(NoteName.GFLAT, NoteType.NOTANOTE, octave), frequency);

			// G
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 7);
			noteFrequency.put(new Note(NoteName.G, NoteType.NOTANOTE, octave), frequency);

			// GSHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 8);
			noteFrequency.put(new Note(NoteName.GSHARP, NoteType.NOTANOTE, octave), frequency);

			// AFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 8);
			noteFrequency.put(new Note(NoteName.AFLAT, NoteType.NOTANOTE, octave), frequency);

			// A
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 9);
			noteFrequency.put(new Note(NoteName.A, NoteType.NOTANOTE, octave), frequency);

			// ASHARP
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 10);
			noteFrequency.put(new Note(NoteName.ASHARP, NoteType.NOTANOTE, octave), frequency);

			// BFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 10);
			noteFrequency.put(new Note(NoteName.BFLAT, NoteType.NOTANOTE, octave), frequency);

			// B
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 11);
			noteFrequency.put(new Note(NoteName.B, NoteType.NOTANOTE, octave), frequency);

			// CFLAT
			frequency = calculateFrequency((12 * octave) - STEP_DIFFERENCE + 11);
			noteFrequency.put(new Note(NoteName.CFLAT, NoteType.NOTANOTE, octave), frequency);
		}

		//?? Why create a copy of the map? - Robert
		noteFrequency = new HashMap<Note, Double>(noteFrequency);
	}

	private static double calculateFrequency(int halfStepDistance) {
		return baseFrequency * Math.pow(a, halfStepDistance);
	}

	/**
	 * Contains equivalences for notes. Also known as enharmonic tones.
	 * 
	 * Note: Only here because I thought I needed it before. - Andrew
	 * 
	 * @param note1
	 * @param note2
	 * @return true if note1 and note2 are enharmonic tones
	 */
	private static boolean isNoteEnharmonic(Note note1, Note note2) {
		/*
		 * ================= Note equivalences ================= Different
		 * "spelling" for note of the same frequency Probably better to make
		 * this into another table
		 * 
		 * True only if in same octave and same frequency Length not important
		 */

		if (note1.getOctave() == note2.getOctave()) {
			switch (note1.getName()) {

			// CSHARP == DFLAT
			case CSHARP:
				if (note2.getName() == NoteName.DFLAT) {
					return true;
				}
				break;
			case DFLAT:
				if (note2.getName() == NoteName.CSHARP) {
					return true;
				}
				break;

			// DSHARP == EFLAT
			case DSHARP:
				if (note2.getName() == NoteName.EFLAT) {
					return true;
				}
				break;
			case EFLAT:
				if (note2.getName() == NoteName.DSHARP) {
					return true;
				}
				break;

			// E == FFLAT
			case E:
				if (note2.getName() == NoteName.FFLAT) {
					return true;
				}
				break;
			case FFLAT:
				if (note2.getName() == NoteName.E) {
					return true;
				}
				break;

			// ESHARP == F
			case ESHARP:
				if (note2.getName() == NoteName.F) {
					return true;
				}
				break;
			case F:
				if (note2.getName() == NoteName.ESHARP) {
					return true;
				}
				break;

			// FSHARP == GFLAT
			case FSHARP:
				if (note2.getName() == NoteName.GFLAT) {
					return true;
				}
				break;
			case GFLAT:
				if (note2.getName() == NoteName.FSHARP) {
					return true;
				}
				break;

			// GSHARP == AFLAT
			case GSHARP:
				if (note2.getName() == NoteName.AFLAT) {
					return true;
				}
				break;
			case AFLAT:
				if (note2.getName() == NoteName.GSHARP) {
					return true;
				}
				break;

			// ASHARP == BFLAT
			case ASHARP:
				if (note2.getName() == NoteName.BFLAT) {
					return true;
				}
				break;
			case BFLAT:
				if (note2.getName() == NoteName.ASHARP) {
					return true;
				}
				break;

			// B == CFLAT
			case B:
				if (note2.getName() == NoteName.CFLAT) {
					return true;
				}
				break;
			case CFLAT:
				if (note2.getName() == NoteName.B) {
					return true;
				}
				break;

			// BSHARP == C
			case BSHARP:
				if (note2.getName() == NoteName.C) {
					return true;
				}
				break;
			case C:
				if (note2.getName() == NoteName.BSHARP) {
					return true;
				}
				break;

			// End of equalities -- Shouldn't reach here
			default:
				return false;
			}
		} else {
			// Octaves are different, can't be same note
			return false;
		}

		// Notes aren't equivalent
		return false;
	}

	/**
	 * Interface for the NoteTable. Returns frequency of requested note.
	 * 
	 * @param name
	 * @param octave
	 * @return
	 */
	public double getNoteFrequency(NoteName name, int octave) {
		Note requestedNote = new Note(name, NoteType.NOTANOTE, octave);

		boolean foundNote = noteFrequency.containsKey(requestedNote);
		if (foundNote) {
			return noteFrequency.get(requestedNote);
		} else {
			// Negative if not in table
			return -1.0;
		}
	}
}
