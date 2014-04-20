package Listeners;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import com.example.musicnotes.R;

import MusicSheet.Chord;
import MusicSheet.Measure;
import MusicSheet.Note;
import MusicSheet.NoteName;
import MusicSheet.NoteType;
import MusicSheet.Sheet;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;


public class EditorDragListener implements OnDragListener {


	int currentMeasure;
	Sheet sheet;
	enum DeleteFlag {FIRST, DELETE, NODELETE};
	DeleteFlag myDelete;
	public EditorDragListener(Sheet sheet, int currentMeasure)
	{
		this.currentMeasure =currentMeasure;
		this.sheet = sheet;
		myDelete = DeleteFlag.NODELETE;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		ImageView noteView = (ImageView)v;
		// Get the chord and measure views that the note is located in
		RelativeLayout chordParent = (RelativeLayout)noteView.getParent();
		RelativeLayout measureParent = (RelativeLayout) chordParent.getParent();
		int chordsPos = -1, notesPos = -1;

		// Get the chord position within the measure
		for(int chords = 0; chords<measureParent.getChildCount(); chords++)
		{
			if(chordParent == measureParent.getChildAt(chords))
			{
				chordsPos = chords;
				break;
			}
		}

		// Get the note position within the chord
		for(int notes = 0; notes < chordParent.getChildCount(); notes++)
		{
			if(noteView == chordParent.getChildAt(notes))
			{
				notesPos = notes;
				System.out.println(notesPos);
				break;
			}
		}

		// Get the selected chord and add a new chord
		sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).addChord(chordsPos);
		Chord chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);

		switch(event.getAction())
		{
		case DragEvent.ACTION_DRAG_STARTED:
			/*noteView.setBackgroundResource(R.drawable.background);
			noteView.setImageResource(R.drawable.fillednotespace);
			noteView.setScaleType(ScaleType.CENTER_INSIDE);*/
			
			myDelete = DeleteFlag.FIRST;
			return true;

		case DragEvent.ACTION_DRAG_ENTERED:
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
			
			if(searchNote == null )
			{
				noteView.setBackgroundResource(R.drawable.background);
				noteView.setImageResource(R.drawable.fillednotespace);
				noteView.setScaleType(ScaleType.CENTER_INSIDE);
				myDelete = DeleteFlag.DELETE;
			}
			else if(searchNote != null && myDelete == DeleteFlag.FIRST)
			{
				
				myDelete = DeleteFlag.DELETE;
			}
			else
			{
				myDelete = DeleteFlag.NODELETE;
				
			}
			/*
			updateMeasures(currentMeasure);*/
			return true;

		case DragEvent.ACTION_DRAG_EXITED:
			if(myDelete != DeleteFlag.NODELETE)
			{
				noteView.setBackgroundResource(R.drawable.nobackground);
				noteView.setImageResource(0);
				switch(notesPos)
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

			return true;

		case DragEvent.ACTION_DROP:
			noteView.setBackgroundResource(R.drawable.nobackground);
			switch(notesPos)
			{
			case 0:
				chordSel.addNote(NoteName.B, NoteType.EIGHTH_NOTE, 5);
				break;
			case 1:
				chordSel.addNote(NoteName.A, NoteType.EIGHTH_NOTE, 5);
				break;
			case 2:
				chordSel.addNote(NoteName.G, NoteType.EIGHTH_NOTE, 5);
				break;
			case 3:
				chordSel.addNote(NoteName.F, NoteType.EIGHTH_NOTE, 5);
				break;
			case 4:
				chordSel.addNote(NoteName.E, NoteType.EIGHTH_NOTE, 5);
				break;
			case 5:
				chordSel.addNote(NoteName.D, NoteType.EIGHTH_NOTE, 5);
				break;
			case 6:
				chordSel.addNote(NoteName.C, NoteType.EIGHTH_NOTE, 5);
				break;
			case 7:
				chordSel.addNote(NoteName.B, NoteType.EIGHTH_NOTE, 4);
				break;
			case 8:
				chordSel.addNote(NoteName.A, NoteType.EIGHTH_NOTE, 4);
				break;
			case 9:
				chordSel.addNote(NoteName.G, NoteType.EIGHTH_NOTE, 4);
				break;
			case 10:
				chordSel.addNote(NoteName.F, NoteType.EIGHTH_NOTE, 4);
				break;
			case 11:
				chordSel.addNote(NoteName.E, NoteType.EIGHTH_NOTE, 4);
				break;
			case 12:
				chordSel.addNote(NoteName.D, NoteType.EIGHTH_NOTE, 4);
				break;
			case 13:
				chordSel.addNote(NoteName.C, NoteType.EIGHTH_NOTE, 4);
				break;
			case 14:
				chordSel.addNote(NoteName.B, NoteType.EIGHTH_NOTE, 3);
				break;
			}
			
			myDelete = DeleteFlag.NODELETE;
			return true;

		}

		return false;
	}

}
