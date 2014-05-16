package com.example.musicnotes;

import MusicSheet.*;
import MusicUtil.EnumClef;
import MusicUtil.NoteName;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;

public class NoteToScreen {


	public static Note findNote(EditorActivity myActivity, Chord chordSel, int notePos)
	{
		Note searchNote = null;
		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.scale();
		currentScale = convertScale(myActivity, currentScale);
		if(currentStaff.clef() == EnumClef.TENOR || currentStaff.clef() == EnumClef.TREBLE)
		{
			if(notePos <= 6)
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave()+1);
			}
			else if(notePos == 14)
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave()-1);
			}
			else
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave());
			}
		}
		else
		{
			if(notePos<=1)
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave()+1);
			}
			else if(notePos <= 8)
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave());
			}
			else
			{
				searchNote = chordSel.get(currentScale[notePos], currentStaff.octave()-1);
			}
		}
		return searchNote;
	}

	public static void addNote(EditorActivity myActivity, Chord chordSel, int notePos, NoteTool noteTool) {

		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.scale();
		currentScale = convertScale(myActivity, currentScale);
		if(currentStaff.clef() == EnumClef.TENOR || currentStaff.clef() == EnumClef.TREBLE)
		{
			if(notePos <= 6)
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave()+1);
			}
			else if(notePos == 14)
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave()-1);
			}
			else
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave());
			}
		}
		else
		{
			if(notePos<=1)
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave()+1);
			}
			else if(notePos <= 8)
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave());
			}
			else
			{
				chordSel.add(currentScale[notePos], noteTool.getType(), currentStaff.octave()-1);
			}
		}
	}

	public static void deleteNote(EditorActivity myActivity, Chord chordSel, int notePos) {

		Staff currentStaff = myActivity.getCurrentStaff();
		NoteName[] currentScale = currentStaff.scale();
		currentScale = convertScale(myActivity, currentScale);

		if(currentStaff.clef() == EnumClef.TENOR || currentStaff.clef() == EnumClef.TREBLE)
		{
			if(notePos <= 6)
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave()+1);
			}
			else if(notePos == 14)
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave()-1);
			}
			else
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave());
			}
		}
		else
		{
			if(notePos<=1)
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave()+1);
			}
			else if(notePos <= 8)
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave());
			}
			else
			{
				chordSel.delete(currentScale[notePos], currentStaff.octave()-1);
			}
		}
	}


	public static NoteTool notetoTool(Note myNote)
	{
		Note selectedNote = myNote;
		int resourceID = 0;
		if(selectedNote != null)
		{
			switch(myNote.accidental())
			{
			case NONE:
				switch(myNote.type())
				{
				case EIGHTH_NOTE:
					resourceID = R.drawable.eigthnote;
					break;
				case DOTTED_EIGHTH_NOTE:
					resourceID = R.drawable.eigthnotedot;
					break;
				case QUARTER_NOTE:
					resourceID = R.drawable.quarternote;
					break;
				case DOTTED_QUARTER_NOTE:
					resourceID = R.drawable.quarternotedot;
					break;
				case HALF_NOTE:
					resourceID = R.drawable.halfnotes;
					break;
				case DOTTED_HALF_NOTE:
					resourceID = R.drawable.halfnotedot;
				case WHOLE_NOTE:
					resourceID = R.drawable.wholenote;
					break;
				default:
					resourceID = R.drawable.eigthnote;
					break;
				}
				break;
			case SHARP:
				switch(myNote.type())
				{
				case EIGHTH_NOTE:
					resourceID = R.drawable.sharpeigthnote;
					break;
				case DOTTED_EIGHTH_NOTE:
					resourceID = R.drawable.sharpeigthnotedot;
					break;
				case QUARTER_NOTE:
					resourceID = R.drawable.sharpquarternote;
					break;
				case DOTTED_QUARTER_NOTE:
					resourceID = R.drawable.sharpquarternotedot;
					break;
				case HALF_NOTE:
					resourceID = R.drawable.sharphalfnote;
					break;
				case DOTTED_HALF_NOTE:
					resourceID = R.drawable.sharphalfnotedot;
				case WHOLE_NOTE:
					resourceID = R.drawable.sharpwholenote;
					break;
				default:
					resourceID = R.drawable.sharpeigthnote;
					break;
				}
				break;
			case FLAT:
				switch(myNote.type())
				{
				case EIGHTH_NOTE:
					resourceID = R.drawable.flateigthnote;
					break;
				case DOTTED_EIGHTH_NOTE:
					resourceID = R.drawable.flateigthnotedot;
					break;
				case QUARTER_NOTE:
					resourceID = R.drawable.flatquarternote;
					break;
				case DOTTED_QUARTER_NOTE:
					resourceID = R.drawable.flatquarternotedot;
					break;
				case HALF_NOTE:
					resourceID = R.drawable.flathalfnote;
					break;
				case DOTTED_HALF_NOTE:
					resourceID = R.drawable.flathalfnotedot;
				case WHOLE_NOTE:
					resourceID = R.drawable.flatwholenote;
					break;
				default:
					resourceID = R.drawable.flateigthnote;
					break;
				}
				break;
			case NATURAL:
				switch(myNote.type())
				{
				case EIGHTH_NOTE:
					resourceID = R.drawable.naturaleigthnote;
					break;
				case DOTTED_EIGHTH_NOTE:
					resourceID = R.drawable.naturaleigthnotedot;
					break;
				case QUARTER_NOTE:
					resourceID = R.drawable.naturalquarternote;
					break;
				case DOTTED_QUARTER_NOTE:
					resourceID = R.drawable.naturalquarternotedot;
					break;
				case HALF_NOTE:
					resourceID = R.drawable.naturalhalfnote;
					break;
				case DOTTED_HALF_NOTE:
					resourceID = R.drawable.naturalhalfnotedot;
				case WHOLE_NOTE:
					resourceID = R.drawable.naturalwholenote;
					break;
				default:
					resourceID = R.drawable.naturaleigthnote;
					break;
				}
				break;

			}
			return new NoteTool(myNote.type(), resourceID);
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