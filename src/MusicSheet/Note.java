/**
 * The note class contains all of the relevant information about a particular note on a sheet of music.
 * 
 * The note takes in three parameters. The first two specify what note is being played and how long it's being played for.
 * The sample rate is for generating the sound sample of the note based on its frequency and duration.
 * 
 * Contents are subject to change as we iterate through the project
 * @author Robert Wang
 */

package MusicSheet;

public class Note {
	private NoteName name;
	private NoteType type;
	private int octave;
	
	
	public Note() {
		
	}
	
	/**
	 * Note() is a constructor that creates a note with a given tone, duration, and sample rate
	 * @param name is an enumerated note name referring to the musical name for the note
	 * @param type is an enumerated note type referring to the duration of the note
	 * @param octave is the octave number of the note
	 */
	public Note(NoteName name, NoteType type, int octave) {
		this.name = name;
		this.type = type;
		this.octave = octave;
	}
	
	/**
	 * A copy constructor for the note class. 
	 * Since enums are immutable, it's fine to just copy them over 
	 * Ints are okay to leave as is since they are primitive types
	 * @param toCopy is the note to be copied
	 */
	public Note(Note toCopy) {
		this.name = toCopy.name;
		this.type = toCopy.type;
		this.octave = toCopy.octave;
	}
	
	/**
	 * Changes the musical name of the note
	 * @param name
	 */
	public void setName(NoteName name) {
		this.name = name;
	}
	
	/**
	 * Changes the note duration
	 * @param type
	 */
	public void setType(NoteType type) {
		this.type = type;
	}
	
	/**
	 * Changes the octave of the note
	 * @param octave
	 */
	public void setOctave(int octave) {
		this.octave = octave;
	}
	
	/**
	 * Retrieves the name of the note
	 * @return name
	 */
	public NoteName getName() {
		return name;
	}
	
	/**
	 * Retrieves the duration/type of the note
	 * @return type
	 */
	public NoteType getType() {
		return type;
	}
	
	/**
	 * Retrieves the octave of the note
	 * @return octave
	 */
	public int getOctave() {
		return octave;
	}
	
	/**
	 * Simple hash could be the number mapping of the actual name
	 * e.g. C0 == 0, CSHARP0 == 2, C1 == 21, CSHARP1 == 23, ... BSHARP8 == 188
	 * Used in note lookup table for determining frequencies
	 * @return returns the unique identifier of the note based on the name of the note and its octave
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
	 * Simple equals method. Notes are equal if they have both the same name and octave 
	 * @param obj is the note being compared
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
		return (this.getName() == ((Note) obj).getName() && this.getOctave() == ((Note) obj).getOctave());
	}

	public int getMidiPitch() {
		int numberName = 0;
		switch(name) {
		case BSHARP:
			numberName = 0;
			break;
		case C:
			numberName = 0;
			break;
		case CSHARP:
			numberName = 1;
			break;
		case DFLAT:
			numberName = 1;
			break;
		case D:
			numberName = 2;
			break;
		case DSHARP:
			numberName = 3;
			break;
		case EFLAT:
			numberName = 3;
			break;
		case E:
			numberName = 4;
			break;
		case ESHARP:
			numberName = 5;
			break;
		case FFLAT:
			numberName = 4;
			break;
		case F:
			numberName = 5;
			break;
		case FSHARP:
			numberName = 6;
			break;
		case GFLAT:
			numberName = 6;
			break;
		case G:
			numberName = 7;
			break;
		case GSHARP:
			numberName = 8;
			break;
		case AFLAT:
			numberName = 8;
			break;	
		case A:
			numberName = 9;
			break;
		case ASHARP:
			numberName = 10;
			break;
		case BFLAT:
			numberName = 10;
			break;
		case B:
			numberName = 11;
			break;
		case CFLAT:
			numberName = 11;
			break;
		default:
			numberName = -1;
			break;
		}
		
		return (12 * this.octave) + numberName;
	}

	public long getNoteDurationInTicks(int PPQ) {
		long duration = 0;
		int modifier = 0;
		
		// Duration should be calculated as PPQ * (modifier / 8)
		switch(this.getType()){
		case EIGHTH_NOTE:
			modifier = 4;
			break;
		case EIGTH_REST:
			modifier = 4;
			break;
		case HALF_NOTE:
			modifier = 16;
			break;
		case HALF_REST:
			modifier = 16;
			break;
		case NOTANOTE:
			modifier = 0;
			break;
		case QUARTER_NOTE:
			modifier = 8;
			break;
		case QUARTER_REST:
			modifier = 8;
			break;
		case SIXTEENTH_NOTE:
			modifier = 2;
			break;
		case SIXTEENTH_REST:
			modifier = 2;
			break;
		case THIRTYSECOND_NOTE:
			modifier = 1;
			break;
		case THIRTYSECOND_REST:
			modifier = 1;
			break;
		case WHOLE_NOTE:
			modifier = 32;
			break;
		case WHOLE_REST:
			modifier = 32;
			break;
		default:
			break;
		}
		
		duration = (PPQ * modifier) / 8;
		
		return duration;
	}
}
