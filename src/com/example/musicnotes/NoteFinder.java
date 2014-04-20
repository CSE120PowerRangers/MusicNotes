package com.example.musicnotes;

import MusicSheet.Chord;
import MusicSheet.Note;
import MusicSheet.NoteName;

public class NoteFinder {

	
	public static Note findNote(Chord chordSel, int notesPos)
	{
		Note searchNote = null;
		switch(notesPos)
		{
		case 0:
			searchNote = chordSel.getNote(NoteName.B, 5);
			break;
		case 1:
			searchNote = chordSel.getNote(NoteName.A, 5);
			break;
		case 2:
			searchNote = chordSel.getNote(NoteName.G, 5);
			break;
		case 3:
			searchNote = chordSel.getNote(NoteName.F, 5);
			break;
		case 4:
			searchNote = chordSel.getNote(NoteName.E, 5);
			break;
		case 5:
			searchNote = chordSel.getNote(NoteName.D, 5);
			break;
		case 6:
			searchNote = chordSel.getNote(NoteName.C, 5);
			break;
		case 7:
			searchNote = chordSel.getNote(NoteName.B, 4);
			break;
		case 8:
			searchNote = chordSel.getNote(NoteName.A, 4);
			break;
		case 9:
			searchNote = chordSel.getNote(NoteName.G, 4);
			break;
		case 10:
			searchNote = chordSel.getNote(NoteName.F, 4);
			break;
		case 11:
			searchNote = chordSel.getNote(NoteName.E, 4);
			break;
		case 12:
			searchNote = chordSel.getNote(NoteName.D, 4);
			break;
		case 13:
			searchNote = chordSel.getNote(NoteName.C, 4);
			break;
		case 14:
			searchNote = chordSel.getNote(NoteName.B, 3);
			break;
		}
		return searchNote;
	}
}
