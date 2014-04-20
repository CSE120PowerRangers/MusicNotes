package MusicSheet;

import java.util.ArrayList;

import MusicUtil.EnumKeySignature;
import MusicUtil.EnumTimeSignature;
import MusicUtil.NoteName;

/**
 * TODO: Allow for adding measures at a particular index instead of just appending
 * See if "copying" measures can be done
 *
 */

public class Signature {
	private int tempo; //Beats per minute
	private EnumTimeSignature timeSignature;
	private EnumKeySignature keySignature;
	private int[] flats;
	private int[] sharps;
	private ArrayList<Measure> measures;

	/**
	 * Signature() is a constructor for a signature which creates a new section with its own time/key signature and tempo
	 * Creates a default time signature, key signature, and tempo
	 */
	public Signature() {
		tempo = 120;
		timeSignature = EnumTimeSignature.FOUR_FOUR;
		keySignature = EnumKeySignature.C_MAJOR;
		flats = new int[8];
		sharps = new int[8];
		measures = new ArrayList<Measure>();
		measures.add(new Measure());
		measures.add(new Measure());
	}

	/**
	 * A signature constructor that initializes the signature
	 * with the given key signature, time signature, and tempo
	 * @param keySig
	 * @param timeSig
	 * @param newTempo
	 */
	public Signature(EnumKeySignature keySig, EnumTimeSignature timeSig, int newTempo) {
		tempo = newTempo;
		flats = new int[8];
		sharps = new int[8];

		setKeySignature(keySig);
		setTimeSignature(timeSig);
		measures = new ArrayList<Measure>();
		measures.add(new Measure());
	}

	/**
	 * Copy constructor for the signature
	 * @param toCopy
	 */
	public Signature(Signature toCopy) {
		this.tempo = toCopy.tempo;
		this.timeSignature = toCopy.timeSignature;
		this.keySignature = toCopy.keySignature;
		System.arraycopy(toCopy.flats, 0, this.flats, 0, toCopy.flats.length);
		System.arraycopy(toCopy.sharps, 0, this.sharps, 0, toCopy.sharps.length);		
		this.measures = new ArrayList<Measure>(toCopy.measures);
	}

	/**
	 * Adds a given measure to the end of the list
	 * @param newMeasure
	 */
	public void addMeasure(Measure newMeasure) {
		measures.add(newMeasure);
	}

	/**
	 * Inserts a given measure into the given index
	 * @param index
	 * @param newMeasure
	 */
	public void addMeasure(int index, Measure newMeasure) {
		if(index >= 0 && index < measures.size()) {
			//Force creation of new copy of measure
			newMeasure = new Measure(newMeasure);
			measures.add(index, newMeasure);
		}
	}

	/**
	 * Deletes a given measure from the list
	 * @param oldMeasure
	 */
	public void deleteMeasure(Measure oldMeasure) {
		for(int i = 0; i < measures.size(); i++) {
			if(measures.get(i).equals(oldMeasure)) {
				measures.remove(i);
				break;
			}
		}
	}

	/**
	 * Sets the key signature of this signature
	 * The key signature determines what flats or sharps are in the signature
	 * Only deals with major scales since minor scales can just be converted to the appropriate major scale
	 * @param newKey is a key signature enum value converted to its name
	 */
	public void setKeySignature(EnumKeySignature newKey) {
		keySignature = newKey;

		/*
		 * Quick note: Case statements are allowed to fall through on purpose
		 * The rationale is that for sharp/flat keys, each major scale has a 
		 * natural progression in which the sharps and flats are added
		 * https://en.wikipedia.org/wiki/Major_scale
		 */

		switch(newKey) {
		case C_MAJOR:
			for(int i = 0; i < 8; i++) {
				sharps[i] = 0;
				flats[i] = 0;
			}
			break;
			//Sharp keys
		case CSHARP_MAJOR:
			sharps[NoteName.B.ordinal()] = 1;
		case FSHARP_MAJOR:
			sharps[NoteName.E.ordinal()] = 1;
		case B_MAJOR:
			sharps[NoteName.A.ordinal()] = 1;
		case E_MAJOR:
			sharps[NoteName.D.ordinal()] = 1;
		case A_MAJOR:
			sharps[NoteName.G.ordinal()] = 1;
		case D_MAJOR:
			sharps[NoteName.C.ordinal()] = 1;
		case G_MAJOR:
			sharps[NoteName.F.ordinal()] = 1;
			for(int i = 0; i < 8; i++) {
				flats[i] = 0;
			}
			break;

			//Flat keys
		case CFLAT_MAJOR:
			flats[NoteName.F.ordinal()] = 1;
		case GFLAT_MAJOR:
			flats[NoteName.C.ordinal()] = 1;
		case DFLAT_MAJOR:
			flats[NoteName.G.ordinal()] = 1;
		case AFLAT_MAJOR:
			flats[NoteName.D.ordinal()] = 1;
		case EFLAT_MAJOR:
			flats[NoteName.A.ordinal()] = 1;
		case BFLAT_MAJOR:
			flats[NoteName.E.ordinal()] = 1;
		case F_MAJOR:
			flats[NoteName.B.ordinal()] = 1;
			for(int i = 0; i < 8; i++) {
				sharps[i] = 0;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Sets the time signature for this signature
	 * @param newTime is a TimeSignature enum value
	 */
	public void setTimeSignature(EnumTimeSignature newTime) {
		timeSignature = newTime;
	}

	/**
	 * Sets the tempo for this signature
	 * @param newTempo is the new tempo in beats per minute (bpm)
	 */
	public void setTempo(int newTempo) {
		if(newTempo >= 0 || newTempo < 500) {
			tempo = newTempo;
		}
	}

	/**
	 * Gets the key signature for this signature
	 * @return keySignature is the key signature enum
	 */
	public EnumKeySignature getKeySignature() {
		return keySignature;
	}

	/**
	 * Gets the time signature for this signature
	 * @return timeSignature is the time signature enum
	 */
	public EnumTimeSignature getTimeSignature() {
		return timeSignature;
	}

	/**
	 * Gets the tempo for this signature
	 * @return tempo is the beats per minute (bpm) of this signature
	 */
	public int getTempo() {
		return tempo;
	}

	/**
	 * Gets the measure from the given index
	 * @param index
	 * @return
	 */
	public Measure getMeasure(int index) {
		if(index >= 0 && index < measures.size()) {
			return measures.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Returns the number of measures in the signature
	 * @return
	 */
	public int getSize() {
		return measures.size();
	}
}
