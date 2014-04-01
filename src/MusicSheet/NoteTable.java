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

	private static Map<MusicNote, Double> noteFrequency;

	/**
	 * Constructor for the lookup table, fills up the table
	 */
	public NoteTable() {

		noteFrequency = new HashMap<MusicNote, Double>(); // Double is frequency

		double returnedFrequency = 0.0; // Stores the current frequency about to
										// be put in the table

		// Fill in the frequencies one octave at a time
		for (int i = 0; i < MAX_OCTAVES; i++) {
			// C
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE); // Steps
																				// away
																				// from
																				// A4
			noteFrequency.put(
					new MusicNote(NoteNames.C, NoteTypes.NOTANOTE, i),
					returnedFrequency); // Throw it in
			// the table

			// CSHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 1);
			noteFrequency.put(new MusicNote(NoteNames.CSHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// DFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 1);
			noteFrequency.put(new MusicNote(NoteNames.DFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// D
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 2);
			noteFrequency.put(
					new MusicNote(NoteNames.D, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// DSHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 3);
			noteFrequency.put(new MusicNote(NoteNames.DSHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// EFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 3);
			noteFrequency.put(new MusicNote(NoteNames.EFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// E
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 4);
			noteFrequency.put(
					new MusicNote(NoteNames.E, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// ESHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 5);
			noteFrequency.put(new MusicNote(NoteNames.ESHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// FFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 4);
			noteFrequency.put(new MusicNote(NoteNames.FFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// F
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 5);
			noteFrequency.put(
					new MusicNote(NoteNames.F, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// FSHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 6);
			noteFrequency.put(new MusicNote(NoteNames.FSHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// GFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 6);
			noteFrequency.put(new MusicNote(NoteNames.GFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// G
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 7);
			noteFrequency.put(
					new MusicNote(NoteNames.G, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// GSHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 8);
			noteFrequency.put(new MusicNote(NoteNames.GSHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// AFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 8);
			noteFrequency.put(new MusicNote(NoteNames.AFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// A
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 9);
			noteFrequency.put(
					new MusicNote(NoteNames.A, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// ASHARP
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 10);
			noteFrequency.put(new MusicNote(NoteNames.ASHARP,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// BFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 10);
			noteFrequency.put(new MusicNote(NoteNames.BFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);

			// B
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 11);
			noteFrequency.put(
					new MusicNote(NoteNames.B, NoteTypes.NOTANOTE, i),
					returnedFrequency);

			// CFLAT
			returnedFrequency = calculateFrequency((12 * i) - STEP_DIFFERENCE
					+ 11);
			noteFrequency.put(new MusicNote(NoteNames.CFLAT,
					NoteTypes.NOTANOTE, i), returnedFrequency);
		}

		noteFrequency = new HashMap<MusicNote, Double>(noteFrequency);
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
	private static boolean is_note_enharmonic(MusicNote note1, MusicNote note2) {
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
				if (note2.getName() == NoteNames.DFLAT) {
					return true;
				}
				break;
			case DFLAT:
				if (note2.getName() == NoteNames.CSHARP) {
					return true;
				}
				break;

			// DSHARP == EFLAT
			case DSHARP:
				if (note2.getName() == NoteNames.EFLAT) {
					return true;
				}
				break;
			case EFLAT:
				if (note2.getName() == NoteNames.DSHARP) {
					return true;
				}
				break;

			// E == FFLAT
			case E:
				if (note2.getName() == NoteNames.FFLAT) {
					return true;
				}
				break;
			case FFLAT:
				if (note2.getName() == NoteNames.E) {
					return true;
				}
				break;

			// ESHARP == F
			case ESHARP:
				if (note2.getName() == NoteNames.F) {
					return true;
				}
				break;
			case F:
				if (note2.getName() == NoteNames.ESHARP) {
					return true;
				}
				break;

			// FSHARP == GFLAT
			case FSHARP:
				if (note2.getName() == NoteNames.GFLAT) {
					return true;
				}
				break;
			case GFLAT:
				if (note2.getName() == NoteNames.FSHARP) {
					return true;
				}
				break;

			// GSHARP == AFLAT
			case GSHARP:
				if (note2.getName() == NoteNames.AFLAT) {
					return true;
				}
				break;
			case AFLAT:
				if (note2.getName() == NoteNames.GSHARP) {
					return true;
				}
				break;

			// ASHARP == BFLAT
			case ASHARP:
				if (note2.getName() == NoteNames.BFLAT) {
					return true;
				}
				break;
			case BFLAT:
				if (note2.getName() == NoteNames.ASHARP) {
					return true;
				}
				break;

			// B == CFLAT
			case B:
				if (note2.getName() == NoteNames.CFLAT) {
					return true;
				}
				break;
			case CFLAT:
				if (note2.getName() == NoteNames.B) {
					return true;
				}
				break;

			// BSHARP == C
			case BSHARP:
				if (note2.getName() == NoteNames.C) {
					return true;
				}
				break;
			case C:
				if (note2.getName() == NoteNames.BSHARP) {
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
	public double look_up_noteFrequency(NoteNames name, int octave) {
		MusicNote requestedNote = new MusicNote(name, NoteTypes.NOTANOTE,
				octave);

		boolean foundNote = noteFrequency.containsKey(requestedNote);
		if (foundNote) {
			return noteFrequency.get(requestedNote);
		} else {
			// Negative if not in table
			return -1.0;
		}
	}
}
