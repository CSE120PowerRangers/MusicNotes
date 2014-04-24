package MusicSheet;

import java.util.ArrayList;

import MusicUtil.EnumClef;
import MusicUtil.NoteName;


public class Staff {
	private ArrayList<Signature> signatures;
	private EnumClef clef;
	private NoteName[] notes;
	private int octave;
	/**
	 * Default constructor creates a staff with a treble clef and a default signature
	 */
	public Staff() {
		clef = EnumClef.TREBLE;
		setOctave();
		setScale();
		signatures = new ArrayList<Signature>();
		signatures.add(new Signature());
	}
	
	/**
	 * Creates a staff with a given clef and a default signature
	 * @param clef
	 */
	public Staff(EnumClef clef) {
		this.clef = clef;
		setOctave();
		setScale();
		signatures = new ArrayList<Signature>();
		signatures.add(new Signature());
	}
	
	/**
	 * Creates a staff with a given clef and signature
	 * @param clef
	 * @param signature
	 */
	public Staff(EnumClef clef, Signature signature) {
		this.clef = clef;
		setOctave();
		setScale();
		signatures = new ArrayList<Signature>();
		signatures.add(signature);
	}
	
	/**
	 * Copy constructor for the staff
	 * @param clef
	 * @param signature
	 */
	public Staff(Staff toCopy) {
		this.clef = toCopy.clef;
		this.signatures = new ArrayList<Signature>(toCopy.signatures);
	}
	
	/**
	 * Sets the clef of the staff
	 * @param newClef
	 */
	public void setClef(EnumClef newClef) {
		clef = newClef;
		setOctave();
		setScale();
	}
	
	/**
	 * Adds a signature to the staff
	 * @param newSignature
	 */
	public void addSignature(Signature newSignature) {
		signatures.add(newSignature);
	}
	
	/**
	 * Deletes the given signature from the list
	 * @param oldSignature
	 */
	public void deleteSignature(Signature oldSignature) {
		for(int i = 0; i < signatures.size(); i++) {
			if(signatures.get(i).equals(oldSignature)) {
				signatures.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Returns the signature at the given index
	 * @param sigNumber
	 * @return
	 */
	public Signature getSignature(int sigNumber) {
		if(sigNumber < 0 || sigNumber > signatures.size()) {
			return null;
		} else {
			return signatures.get(sigNumber);			
		}
	}
	
	/**
	 * Returns the number of signatures in the staff
	 * @return
	 */
	public int getSize() {
		return signatures.size();
	}
	
	/**
	 * Returns the clef of the staff
	 * @return
	 */
	public EnumClef getClef() {
		return clef;
	}
	
	/**
	 * Returns the octave
	 */
	public int getOctave()
	{
		return octave;
	}
	
	/**
	 * Sets the appropriate octave for the Clef
	 */
	private void setOctave()
	{
		switch(clef)
		{
		case TREBLE:
			octave = 5;
			break;
		case TENOR:
			octave = 4;
			break;
		case BASS:
			octave = 3;
			break;
		}
	}
	
	public NoteName[] getScale()
	{
		return notes;
	}
	
	/**
	 * Sets the appropriate notes for the Clef
	 */
	private void setScale()
	{
		notes = new NoteName[15];
		switch(clef)
		{
		// TREBLE AND TENOR ARE THE SAME
		case TREBLE:
		case TENOR:
			notes[0] = NoteName.B;
			notes[1] = NoteName.A;
			notes[2] = NoteName.G;
			notes[3] = NoteName.F;
			notes[4] = NoteName.E;
			notes[5] = NoteName.D;
			notes[6] = NoteName.C;
			notes[7] = NoteName.B;
			notes[8] = NoteName.A;
			notes[9] = NoteName.G;
			notes[10] = NoteName.F;
			notes[11] = NoteName.E;
			notes[12] = NoteName.D;
			notes[13] = NoteName.C;
			notes[14] = NoteName.B;
			break;
		case BASS:
			notes[0] = NoteName.D;
			notes[1] = NoteName.C;
			notes[2] = NoteName.B;
			notes[3] = NoteName.A;
			notes[4] = NoteName.G;
			notes[5] = NoteName.F;
			notes[6] = NoteName.E;
			notes[7] = NoteName.D;
			notes[8] = NoteName.C;
			notes[9] = NoteName.B;
			notes[10] = NoteName.A;
			notes[11] = NoteName.G;
			notes[12] = NoteName.F;
			notes[13] = NoteName.E;
			notes[14] = NoteName.D;
			break;
		}
	}
}
