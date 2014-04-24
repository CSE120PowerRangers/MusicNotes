package com.example.musicnotes;

import MusicSheet.*;
import MusicUtil.NoteName;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;

public class NoteToScreen {


	public static Note findNote(EditorActivity myActivity, Chord chordSel, int notePos)
	{
		Note searchNote = null;
		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.getScale();
		currentScale = convertScale(myActivity, currentScale);

		if(notePos <= 6)
		{
			searchNote = chordSel.getNote(currentScale[notePos], currentStaff.getOctave()+1);
		}
		else if(notePos == 14)
		{
			searchNote = chordSel.getNote(currentScale[notePos], currentStaff.getOctave()-1);
		}
		else
		{
			searchNote = chordSel.getNote(currentScale[notePos], currentStaff.getOctave());
		}
		return searchNote;
	}

	public static void addNote(EditorActivity myActivity, Chord chordSel, int notePos, NoteTool noteTool) {

		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.getScale();
		currentScale = convertScale(myActivity, currentScale);

		if(notePos <= 6)
		{
			chordSel.addNote(currentScale[notePos], noteTool.getType(), currentStaff.getOctave()+1);
		}
		else if(notePos == 14)
		{
			chordSel.addNote(currentScale[notePos], noteTool.getType(), currentStaff.getOctave()-1);
		}
		else
		{
			chordSel.addNote(currentScale[notePos], noteTool.getType(), currentStaff.getOctave());
		}
	}

	public static void deleteNote(EditorActivity myActivity, Chord chordSel, int notePos) {

		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.getScale();
		currentScale = convertScale(myActivity, currentScale);

		if(notePos <= 6)
		{
			chordSel.deleteNote(currentScale[notePos], currentStaff.getOctave()+1);
		}
		else if(notePos == 14)
		{
			chordSel.deleteNote(currentScale[notePos], currentStaff.getOctave()-1);
		}
		else
		{
			chordSel.deleteNote(currentScale[notePos], currentStaff.getOctave());
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
			default:
				resourceID = R.drawable.eigthnote;
				break;
			}
			return new NoteTool(myNote.getType(), resourceID);
		}
		else
		{
			return null;
		}

	}

	public static NoteName[] convertScale(EditorActivity myActivity, NoteName[] oldScale)
	{
		NoteName[] newScale = oldScale;
		for(int i = 0; i < newScale.length; i++)
		{
			if(myActivity.getCurrentSignature().getSharp(newScale[i].ordinal()))
			{
				switch(newScale[i])
				{
				case A:
					newScale[i] = NoteName.ASHARP;
					break;
				case B:
					newScale[i] = NoteName.BSHARP;
					break;
				case C:
					newScale[i] = NoteName.CSHARP;
					break;
				case D:
					newScale[i] = NoteName.DSHARP;
					break;
				case E:
					newScale[i] = NoteName.ESHARP;
					break;
				case F:
					newScale[i] = NoteName.FSHARP;
					break;
				case G:
					newScale[i] = NoteName.GSHARP;
					break;
				default:
					newScale[i] = null;
					break;
				}
			}
			else if(myActivity.getCurrentSignature().getFlat(newScale[i].ordinal()))
			{
				switch(newScale[i])
				{
				case A:
					newScale[i] = NoteName.AFLAT;
					break;
				case B:
					newScale[i] = NoteName.BFLAT;
					break;
				case C:
					newScale[i] = NoteName.CFLAT;
					break;
				case D:
					newScale[i] = NoteName.DFLAT;
					break;
				case E:
					newScale[i] = NoteName.EFLAT;
					break;
				case F:
					newScale[i] = NoteName.FFLAT;
					break;
				case G:
					newScale[i] = NoteName.GFLAT;
					break;
				default:
					newScale[i] = null;
					break;
				}
			}
		}

		return newScale;
	}
}