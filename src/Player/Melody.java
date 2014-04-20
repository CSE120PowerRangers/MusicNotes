package Player;

import MusicSheet.*;
import MusicUtil.NoteName;
import MusicUtil.NoteType;

public class Melody {
	// Just a test class while working on Player
	public Sheet music;
	
	public Melody(){
		/*
		 * MARY HAD A LITTLE LAMB
		 * 
		 * E4 D4 C4 D4 | E4 E4 E4 | D4 D4 D4 | E4 G4 G4
		 */
		
		Chord E4Q= new Chord();
		Chord D4Q = new Chord();
		Chord C4Q = new Chord();
		Chord G4Q = new Chord();
		Chord E4H = new Chord();
		Chord D4H = new Chord();
		Chord G4H = new Chord();
		
		E4Q.addNote(NoteName.E, NoteType.QUARTER_NOTE, 4);
		D4Q.addNote(NoteName.D, NoteType.QUARTER_NOTE, 4);
		C4Q.addNote(NoteName.C, NoteType.QUARTER_NOTE, 4);
		G4Q.addNote(NoteName.G, NoteType.QUARTER_NOTE, 4);
		
		E4H.addNote(NoteName.E, NoteType.HALF_NOTE, 4);
		D4H.addNote(NoteName.D, NoteType.HALF_NOTE, 4);
		G4H.addNote(NoteName.G, NoteType.HALF_NOTE, 4);
		
		Measure measure1 = new Measure();
		Measure measure2 = new Measure();
		Measure measure3 = new Measure();
		Measure measure4 = new Measure();
		
		measure1.addChord(0, new Chord(E4Q));
		measure1.addChord(2, new Chord(D4Q));
		measure1.addChord(4, new Chord(C4Q));
		measure1.addChord(6, new Chord(D4Q));
		
		measure2.addChord(0, new Chord(E4Q));
		measure2.addChord(2, new Chord(E4Q));
		measure2.addChord(4, new Chord(E4H));
		
		measure3.addChord(0, new Chord(D4Q));
		measure3.addChord(2, new Chord(D4Q));
		measure3.addChord(4, new Chord(D4H));
		
		measure4.addChord(0, new Chord(E4Q));
		measure4.addChord(2, new Chord(G4Q));
		measure4.addChord(4, new Chord(G4H));
		
		Signature sig1 = new Signature();
		
		sig1.addMeasure(measure1);
		sig1.addMeasure(measure2);
		sig1.addMeasure(measure3);
		sig1.addMeasure(measure4);
		
		Staff treble = new Staff();
		
		treble.deleteSignature(new Signature());
		treble.addSignature(sig1);
		
		this.music = new Sheet();
		this.music.deleteStaff(0);
		this.music.addStaff(treble);		
	}
}
