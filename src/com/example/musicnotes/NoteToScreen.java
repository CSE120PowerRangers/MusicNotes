package com.example.musicnotes;

import MusicSheet.Chord;
import MusicSheet.Note;
import MusicUtil.NoteName;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;

public class NoteToScreen {


	public static Note findNote(Chord chordSel, int notePos)
	{
		Note searchNote = null;
		switch(notePos)
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

	public static void addNote(Chord chordSel, int notePos, NoteTool noteTool) {
		switch(notePos)
		{
		case 0:
			chordSel.addNote(NoteName.B, noteTool.getType(), 5);
			break;
		case 1:
			chordSel.addNote(NoteName.A, noteTool.getType(), 5);
			break;
		case 2:
			chordSel.addNote(NoteName.G, noteTool.getType(), 5);
			break;
		case 3:
			chordSel.addNote(NoteName.F, noteTool.getType(), 5);
			break;
		case 4:
			chordSel.addNote(NoteName.E, noteTool.getType(), 5);
			break;
		case 5:
			chordSel.addNote(NoteName.D, noteTool.getType(), 5);
			break;
		case 6:
			chordSel.addNote(NoteName.C, noteTool.getType(), 5);
			break;
		case 7:
			chordSel.addNote(NoteName.B,noteTool.getType(), 4);
			break;
		case 8:
			chordSel.addNote(NoteName.A, noteTool.getType(), 4);
			break;
		case 9:
			chordSel.addNote(NoteName.G,noteTool.getType(), 4);
			break;
		case 10:
			chordSel.addNote(NoteName.F, noteTool.getType(), 4);
			break;
		case 11:
			chordSel.addNote(NoteName.E, noteTool.getType(), 4);
			break;
		case 12:
			chordSel.addNote(NoteName.D, noteTool.getType(), 4);
			break;
		case 13:
			chordSel.addNote(NoteName.C, noteTool.getType(), 4);
			break;
		case 14:
			chordSel.addNote(NoteName.B, noteTool.getType(), 3);
			break;
		}	
	}

	public static void deleteNote(Chord chordSel, int notePos) {
		switch(notePos)
		{
		case 0:
			chordSel.deleteNote(NoteName.B, 5);
			break;
		case 1:
			chordSel.deleteNote(NoteName.A, 5);
			break;
		case 2:
			chordSel.deleteNote(NoteName.G, 5);
			break;
		case 3:
			chordSel.deleteNote(NoteName.F, 5);
			break;
		case 4:
			chordSel.deleteNote(NoteName.E, 5);
			break;
		case 5:
			chordSel.deleteNote(NoteName.D, 5);
			break;
		case 6:
			chordSel.deleteNote(NoteName.C, 5);
			break;
		case 7:
			chordSel.deleteNote(NoteName.B, 4);
			break;
		case 8:
			chordSel.deleteNote(NoteName.A, 4);
			break;
		case 9:
			chordSel.deleteNote(NoteName.G, 4);
			break;
		case 10:
			chordSel.deleteNote(NoteName.F, 4);
			break;
		case 11:
			chordSel.deleteNote(NoteName.E, 4);
			break;
		case 12:
			chordSel.deleteNote(NoteName.D, 4);
			break;
		case 13:
			chordSel.deleteNote(NoteName.C, 4);
			break;
		case 14:
			chordSel.deleteNote(NoteName.B, 3);
			break;
		}
	}
	public static NoteTool notetoTool(Note myNote)
	{
		Note selectedNote = myNote;
		int resourceID = 0;
		if(selectedNote != null)
		{
			NoteType selectedType = selectedNote.getType();

			switch(selectedType)
			{
			case EIGHTH_NOTE:
				resourceID = R.drawable.eigthnote;
				break;
			case QUARTER_NOTE:
				resourceID = R.drawable.quarternote;
				break;
			case HALF_NOTE:
				resourceID = R.drawable.halfnotes;
				break;
			case WHOLE_NOTE:
				resourceID = R.drawable.wholenote;
				break;
			}
			return new NoteTool(myNote.getType(), resourceID);
		}
		else
		{
			return null;
		}
		
	}
}