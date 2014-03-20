package MusicSheet;

import java.util.ArrayList;

/*
 * TODO:
 * Finish up staff & sheet classes
 * Implement marker classes using divisions & do calculations for position
 */

public class Staff {
	private ArrayList<Signature> signatures;
	
	public Staff() {
		
	}
	
	public void addSignature(Signature newSignature) {
		signatures.add(newSignature);
	}
	
	public void deleteSignature(Signature oldSignature) {
		for(int i = 0; i < signatures.size(); i++) {
			if(signatures.get(i).equals(oldSignature)) {
				signatures.remove(i);
				break;
			}
		}
	}
	
	//Staff modifies its signatures, needs to know which signature it is
	public void modifySignature(int sigNumber) {
		
	}
	
	public Signature getSignature(int sigNumber) {
		
		return null;
	}
	
	
}
