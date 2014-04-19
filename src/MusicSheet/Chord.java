package MusicSheet;

import java.util.ArrayList;

public class Chord {
	private ArrayList<Note> noteList;
	
	/**
	 * Chord() is a constructor that creates a chord object which contains a list of notes in the chord.
	 * The chord keeps an unordered list of notes, which may or may not have the same durations
	 * Chords cannot contain duplicate frequencies
	 */
	public Chord() {
		noteList = new ArrayList<Note>();
	}

	public Chord(Chord toCopy) {
		this.noteList = new ArrayList<Note>(toCopy.noteList);
	}
	
	/**
	 * Finds a note with the name and octave
	 * @param name is the enumerated name of the note
	 * @param octave is the octave of the note
	 * @return note if found, null if not
	 */
	private Note findNote(NoteName name, int octave) {
		for(Note n : noteList) {
			if(n.getName() == name && n.getOctave() == octave) {
				return n;
			}
		}
		return null;
	}
	
	/**
	 * Adds a note to the chord if an existing note doesn't have the same name and octave
	 * @param name is the enumerated name of the note
	 * @param type is the enumerated type of the note duration
	 * @param octave is the octave of the note
	 */
	public void addNote(NoteName name, NoteType type, int octave) {
		if(findNote(name, octave) != null) {
			//Note already exists with that frequency
			return;
		} else if(octave < 0 || octave > 8) {
			//Invalid octave
			return;
		}
		
		Note newNote = new Note(name, type, octave);
		noteList.add(newNote);
	}
	
	/**
	
	 * Deletes the note from the note list 
	 * @param targetName
	 * @param targetOctave
	 */
	public void deleteNote(NoteName targetName, int targetOctave) {
		if(targetOctave < 0 || targetOctave > 8) {
			return;
		}
		
		Note targetNote = findNote(targetName, targetOctave);
		if(targetNote != null) {
			noteList.remove(targetNote);
		}
	}
	
	/**
	 * @param targetName is the enumerated name of the note
	 * @param targetOctave is the octave of the note
	 * @return note with the target frequency or null if it wasn't found
	 */
	public Note getNote(NoteName targetName, int targetOctave) {
		if(targetOctave < 0 || targetOctave > 8) {
			return null;
		}
		
		return findNote(targetName, targetOctave);
	}
	
	/**
	 * Used in generating samples
	 * @param index - index in noteList
	 * @return note at desired index
	 */
	public Note getNote(int index) {
		return noteList.get(index);
	}
	
	/**
	 * @return number of notes in the chord
	 */
	public int getSize() {
		return noteList.size();
	}
}
