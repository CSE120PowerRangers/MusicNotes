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
		
		E4Q.add(NoteName.E, NoteType.QUARTER_NOTE, 4);
		D4Q.add(NoteName.D, NoteType.QUARTER_NOTE, 4);
		C4Q.add(NoteName.C, NoteType.QUARTER_NOTE, 4);
		G4Q.add(NoteName.G, NoteType.QUARTER_NOTE, 4);
		
		E4H.add(NoteName.E, NoteType.HALF_NOTE, 4);
		D4H.add(NoteName.D, NoteType.HALF_NOTE, 4);
		G4H.add(NoteName.G, NoteType.HALF_NOTE, 4);
		
		Measure measure1 = new Measure();
		Measure measure2 = new Measure();
		Measure measure3 = new Measure();
		Measure measure4 = new Measure();
		
		measure1.add(0, new Chord(E4Q));
		measure1.add(2, new Chord(D4Q));
		measure1.add(4, new Chord(C4Q));
		measure1.add(6, new Chord(D4Q));
		
		measure2.add(0, new Chord(E4Q));
		measure2.add(2, new Chord(E4Q));
		measure2.add(4, new Chord(E4H));
		
		measure3.add(0, new Chord(D4Q));
		measure3.add(2, new Chord(D4Q));
		measure3.add(4, new Chord(D4H));
		
		measure4.add(0, new Chord(E4Q));
		measure4.add(2, new Chord(G4Q));
		measure4.add(4, new Chord(G4H));
		
		Staff treble = new Staff();
		
		treble.add(measure1);
		treble.add(measure2);
		treble.add(measure3);
		treble.add(measure4);
		
		Signature sig = new Signature();
		
		sig.delete(new Staff());
		sig.add(treble);
		
		this.music = new Sheet();
		this.music.delete(0);
		this.music.add(sig);		
	}
}
