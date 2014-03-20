package MusicSheet;

import java.util.ArrayList;

public class Signature {
	private int tempo;
	private String timeSignature;
	private String keySignature;
	private int[] flats;
	private int[] sharps;
	private ArrayList<Measure> measures;

	/**
	 * Signature() is a constructor for a signature which creates a new section with its own time/key signature and tempo
	 * 
	 */
	public Signature() {
		tempo = 0;
		timeSignature = "";
		keySignature = "";
		flats = new int[8];
		sharps = new int[8];
	}

	public Signature(String keySig, String timeSig, int newTempo) {
		tempo = newTempo;
		timeSignature = "";
		keySignature = "";
		flats = new int[8];
		sharps = new int[8];
		
		setKeySignature(keySig);
		setTimeSignature(timeSig);
	}

	public void addMeasure(Measure newMeasure) {
		measures.add(newMeasure);
	}
	
	
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
	 * @param newKey is a key signature enum value converted to its name
	 */
	public void setKeySignature(String newKey) {
		keySignature = newKey;
		KeySignature whichKey = KeySignature.valueOf(newKey);
		
		/*
		 * Quick note: Case statements are allowed to fall through on purpose
		 * The rationale is that for sharp/flat keys, each major scale has a 
		 * natural progression in which the sharps and flats are added
		 * https://en.wikipedia.org/wiki/Major_scale
		 */
		
		switch(whichKey) {
		case C_MAJOR:
			for(int i = 0; i < 8; i++) {
				sharps[i] = 0;
				flats[i] = 0;
			}
			break;
		//Sharp keys
		case CSHARP_MAJOR:
			sharps[NoteNames.B.ordinal()] = 1;
		case FSHARP_MAJOR:
			sharps[NoteNames.E.ordinal()] = 1;
		case B_MAJOR:
			sharps[NoteNames.A.ordinal()] = 1;
		case E_MAJOR:
			sharps[NoteNames.D.ordinal()] = 1;
		case A_MAJOR:
			sharps[NoteNames.G.ordinal()] = 1;
		case D_MAJOR:
			sharps[NoteNames.C.ordinal()] = 1;
		case G_MAJOR:
			sharps[NoteNames.F.ordinal()] = 1;
			for(int i = 0; i < 8; i++) {
				flats[i] = 0;
			}
			break;
			
		//Flat keys
		case CFLAT_MAJOR:
			flats[NoteNames.F.ordinal()] = 1;
		case GFLAT_MAJOR:
			flats[NoteNames.C.ordinal()] = 1;
		case DFLAT_MAJOR:
			flats[NoteNames.G.ordinal()] = 1;
		case AFLAT_MAJOR:
			flats[NoteNames.D.ordinal()] = 1;
		case EFLAT_MAJOR:
			flats[NoteNames.A.ordinal()] = 1;
		case BFLAT_MAJOR:
			flats[NoteNames.E.ordinal()] = 1;
		case F_MAJOR:
			flats[NoteNames.B.ordinal()] = 1;
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
	 * @param newTime is a TimeSignature enum value converted to its name
	 */
	public void setTimeSignature(String newTime) {
		timeSignature = newTime;
	}
	
	/**
	 * Sets the tempo for this signature
	 * @param newTempo is the new tempo in beats per minute (bpm)
	 */
	public void setTempo(int newTempo) {
		tempo = newTempo;
	}
	
	/**
	 * Gets the key signature for this signature
	 * @return keySignature is the string value of the key signature enum
	 */
	public String getKeySignature() {
		return keySignature;
	}
	
	/**
	 * Gets the time signature for this signature
	 * @return timeSignature is the string value of the time signature enum
	 */
	public String getTimeSignature() {
		return timeSignature;
	}
	
	/**
	 * Gets the tempo for this signature
	 * @return tempo is the beats per minute (bpm) of this signature
	 */
	public int getTempo() {
		return tempo;
	}
}
