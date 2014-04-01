package MusicSheet;


public class MusicNote {
	private final NoteNames name;
	private final NoteTypes length;
	private final int octave;
	
	public MusicNote(final NoteNames n,final NoteTypes l,final int o) {
		this.name = n;
		this.length = l;
		this.octave = o;
	}
	
	public NoteNames getName(){
		return name;
	}
	
	public NoteTypes getLength(){
		return length;
	}
	
	public int getOctave() {
		return octave;
	}
	
	/* 
	 * NoteTable uses MusicNote as keys for a hashmap of frequencies, need to implement both hashCode() and equals()
	 * 
	 * Not intended for other use currently.
	*/
	
	/**
	 * Simple hash could be the number mapping of the actual name
	 * e.g. C0 == 0, CSHARP0 == 2, C1 == 21, CSHARP1 == 23, ... BSHARP8 == 188
	 */
	public int hashCode() {
		int numberName;
		switch(name) {

		case C:
			numberName = 0;
			break;
		case CFLAT:
			numberName = 1;
			break;
		case CSHARP:
			numberName = 2;
			break;
		case D:
			numberName = 3;
			break;
		case DFLAT:
			numberName = 4;
			break;
		case DSHARP:
			numberName = 5;
			break;
		case E:
			numberName = 6;
			break;
		case EFLAT:
			numberName = 7;
			break;
		case ESHARP:
			numberName = 8;
			break;
		case F:
			numberName = 9;
			break;
		case FFLAT:
			numberName = 10;
			break;
		case FSHARP:
			numberName = 11;
			break;
		case G:
			numberName = 12;
			break;
		case GFLAT:
			numberName = 13;
			break;
		case GSHARP:
			numberName = 14;
			break;
		case A:
			numberName = 15;
			break;
		case AFLAT:
			numberName = 16;
			break;
		case ASHARP:
			numberName = 17;
			break;
		case B:
			numberName = 18;
			break;
		case BFLAT:
			numberName = 19;
			break;
		case BSHARP:
			numberName = 20;
			break;
		default:
			// Should NEVER happen
			numberName = -1000000;
			break;
		
		}
		return (21 * octave) + numberName;
	}
	
	/**
	 * Simple equals method. MusicNotes are equal if they have both a 
	 * @param obj
	 */
	public boolean equals(Object obj) {
		// Trivial Cases
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		
		// Examine note name and octave
		return (this.getName() == ((MusicNote) obj).getName() && this.getOctave() == ((MusicNote) obj).getOctave());
	}
	
	
}
