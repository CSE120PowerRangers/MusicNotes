import java.util.ArrayList;

public class Chord {
	private ArrayList<Note> notes;
	
	public void addNote(Note note) {
		notes.add(note);
	}
	
	public void removeNote(Note note) {
		int toRemove = 0;
		
		for(int i = 0; i < notes.size(); i++) {
			if(note.equals(notes.get(i))) {
				toRemove = i;
			}
		}
		notes.remove(toRemove);
	}
}
