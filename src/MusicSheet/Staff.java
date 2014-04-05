package MusicSheet;

import java.util.ArrayList;


public class Staff {
	private ArrayList<Signature> signatures;
	private Clef clef;
	
	/**
	 * Default constructor creates a staff with a treble cleff and a default signature
	 */
	public Staff() {
		clef = Clef.TREBLE;
		signatures.add(new Signature());
	}
	
	/**
	 * Creates a staff with a given clef and a default signature
	 * @param clef
	 */
	public Staff(Clef clef) {
		this.clef = clef;
		signatures.add(new Signature());
	}
	
	/**
	 * Creates a staff with a given clef and signature
	 * @param clef
	 * @param signature
	 */
	public Staff(Clef clef, Signature signature) {
		this.clef = clef;
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
	public void setClef(Clef newClef) {
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
	public Clef getClef() {
		return clef;
	}
}
