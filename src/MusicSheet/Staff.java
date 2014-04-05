package MusicSheet;

import java.util.ArrayList;


public class Staff {
	private ArrayList<Signature> signatures;
	private Clef clef;
	
	public Staff() {
		
	}
	
	public Staff(Clef clef) {
		this.clef = clef;
	}
	
	public void setClef(Clef newClef) {
		clef = newClef;
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
	
	public Signature getSignature(int sigNumber) {
		return signatures.get(sigNumber);
	}
	
	public Clef getClef() {
		return clef;
	}
}
