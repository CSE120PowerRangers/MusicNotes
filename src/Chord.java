import java.util.ArrayList;



public class Chord {
	private ArrayList<Note> noteList;
	
	/**
	 * Chord() is a constructor that creates a chord object which contains a list of noteList in the given chord.
	 */
	public Chord() {

	}
	
	/**
	 * Adds a note to the chord if an existing note doesn't have the same frequency
	 * @param frequency of the note
	 * @param duration of the note in milliseconds
	 * @param sampleRate of the note for playback
	 */
	public void addNote(int frequency, int duration, int sampleRate) {
		for(Note n : noteList) {
			if(n.getFrequency() == frequency) {
				//This frequency already exists
				return;
			}
		}
		Note newNote = new Note(frequency, duration, sampleRate);
		noteList.add(newNote);
	}
	
	/**
	 * Deletes the note from the note list 
	 * @param targetFrequency 
	 */
	public void deleteNote(int targetFrequency) {
		for(Note n : noteList) {
			if(n.getFrequency() == targetFrequency) {
				noteList.remove(n);
				break;
			}
		}
	}
	
	/**
	 * @param targetFrequency of the note
	 * @return note with the target frequency or null if it wasn't found
	 */
	public Note getNote(int targetFrequency) {
		for(Note n : noteList) {
			if(n.getFrequency() == targetFrequency) {
				return n;
			}
		}
		return null;
	}
	
	/**
	 * @return number of noteList in the chord
	 */
	public int getNumbernoteList() {
		return noteList.size();
	}
}
