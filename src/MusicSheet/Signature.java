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
	private int tempo, numerator, denominator; //Beats per minute
	private EnumTimeSignature timeSignature;
	private EnumKeySignature keySignature;
	private int[] flats;
	private int[] sharps;
	private ArrayList<Staff> staffs;
	
	/**
	 * Signature() is a constructor for a signature which creates a new section with its own time/key signature and tempo
	 * Creates a default time signature, key signature, and tempo
	 */
	public Signature() {
		tempo = 120;
		timeSignature = EnumTimeSignature.FOUR_FOUR;
		keySignature = EnumKeySignature.C_MAJOR;
		flats = new int[21];
		sharps = new int[21];
		
		/*
		 * Every signature has a different time signature, so the measures inside the signature
		 * need to readjust how many divisions it has in accordance to the time signature of the
		 * signature that the measure resides in. This is only modified once a signature is made
		 * so it shouldn't cause problems with measures that already exist.
		 * 
		 * In addition, the denominator of the time signature cannot be larger than the division type
		 * that we currently support. This causes issues with the calculations and doesn't give us
		 * whole numbers
		 */
		
		numerator = EnumTimeSignature.getNumerator(timeSignature);
		denominator = EnumTimeSignature.getDenom(timeSignature);

		if(EnumTimeSignature.getDenom(timeSignature) > Measure.divisionType()) {
			//Not allowed. Reset to default time signature 4/4
			timeSignature = EnumTimeSignature.FOUR_FOUR;
			numerator = 4;
			denominator = 4;
		}

		int numDivs = (int) ( ((float)(numerator) / denominator) * Measure.divisionType());
		Measure.setDivisionNumber(numDivs);

		staffs = new ArrayList<Staff>();
		staffs.add(new Staff());
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
		flats = new int[21];
		sharps = new int[21];

		setKeySignature(keySig);
		setTimeSignature(timeSig);
		
		numerator = EnumTimeSignature.getNumerator(timeSignature);
		denominator = EnumTimeSignature.getDenom(timeSignature);

		if(EnumTimeSignature.getDenom(timeSignature) > Measure.divisionType()) {
			//Not allowed. Reset to default time signature 4/4
			timeSignature = EnumTimeSignature.FOUR_FOUR;
			numerator = 4;
			denominator = 4;
		}
		
		int numDivs = (int) ( ((float)(numerator) / denominator) * Measure.divisionType());
		Measure.setDivisionNumber(numDivs);
		
		staffs = new ArrayList<Staff>();
		staffs.add(new Staff());
	}

	/**
	 * Copy constructor for the signature
	 * @param toCopy
	 */
	public Signature(Signature toCopy) {
		this.tempo = toCopy.tempo;
		this.timeSignature = toCopy.timeSignature;
		this.keySignature = toCopy.keySignature;
		flats = new int[21];
		System.arraycopy(toCopy.flats, 0, this.flats, 0, toCopy.flats.length);
		sharps = new int[21];
		System.arraycopy(toCopy.sharps, 0, this.sharps, 0, toCopy.sharps.length);		
		this.staffs = new ArrayList<Staff>(toCopy.staffs);
	}

	
	/**
	 * Adds a staff to the signature
	 * @param newSignature
	 */
	public void add(Staff newStaff) {
		staffs.add(newStaff);
	}
	
	/**
	 * Deletes the given staff from the list
	 * @param oldStaff
	 */
	public void delete(Staff oldStaff) {
		for(int i = 0; i < staffs.size(); i++) {
			if(staffs.get(i).equals(oldStaff)) {
				staffs.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Returns the staff at the given index
	 * @param sigNumber
	 * @return
	 */
	public Staff get(int sigNumber) {
		if(sigNumber < 0 || sigNumber > staffs.size()) {
			return null;
		} else {
			return staffs.get(sigNumber);			
		}
	}
	
	/**
	 * Returns the number of staffs in the staff
	 * @return
	 */
	public int size() {
		return staffs.size();
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
	 * Checks to see if note should be a sharp
	 * @param noteOrdinal Call NoteName.(A-E).ordinal()
	 * @return
	 */
	public boolean getSharp(int noteOrdinal)
	{
		if(sharps[noteOrdinal] == 1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if note should be a sharp
	 * @param noteOrdinal Call NoteName.(A-E).ordinal()
	 * @return
	 */
	public boolean getFlat(int noteOrdinal)
	{
		if(flats[noteOrdinal] == 1)
		{
			return true;
		}
		return false;
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
	public EnumKeySignature keySignature() {
		return keySignature;
	}

	/**
	 * Gets the time signature for this signature
	 * @return timeSignature is the time signature enum
	 */
	public EnumTimeSignature timeSignature() {
		return timeSignature;
	}

	/**
	 * Gets the tempo for this signature
	 * @return tempo is the beats per minute (bpm) of this signature
	 */
	public int tempo() {
		return tempo;
	}
}
