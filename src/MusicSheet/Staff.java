package MusicSheet;

import java.util.ArrayList;

import MusicUtil.EnumClef;


public class Staff {
	private ArrayList<Signature> signatures;
	private EnumClef clef;
	
	/**
	 * Default constructor creates a staff with a treble clef and a default signature
	 */
	public Staff() {
		clef = EnumClef.TREBLE;
		signatures = new ArrayList<Signature>();
		signatures.add(new Signature());
	}
	
	/**
	 * Creates a staff with a given clef and a default signature
	 * @param clef
	 */
	public Staff(EnumClef clef) {
		this.clef = clef;
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
}
