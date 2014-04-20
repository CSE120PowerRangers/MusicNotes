package Listeners;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.example.musicnotes.NoteToScreen;
import com.example.musicnotes.R;

import MusicSheet.Chord;
import MusicSheet.Note;
import MusicSheet.Sheet;
import MusicUtil.NoteTool;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;


public class EditorDragListener implements OnDragListener {


	int currentMeasure;
	Sheet sheet;
	NoteTool currentTool;
	enum DeleteFlag {FIRST, DELETE, NODELETE};
	DeleteFlag myDelete;
	public EditorDragListener(Sheet sheet, int currentMeasure, NoteTool currentTool) {
		this.currentMeasure =currentMeasure;
		this.sheet = sheet;
		this.currentTool = currentTool;
		myDelete = DeleteFlag.NODELETE;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		ImageView noteView = (ImageView)v;
		// Get the chord and measure views that the note is located in
		RelativeLayout chordParent = (RelativeLayout)noteView.getParent();
		RelativeLayout measureParent = (RelativeLayout) chordParent.getParent();
		int chordsPos = -1, notePos = -1;

		// Get the chord position within the measure
		for(int chords = 0; chords<measureParent.getChildCount(); chords++) {
			if(chordParent == measureParent.getChildAt(chords)) {
				chordsPos = chords;
				break;
			}
		}

		// Get the note position within the chord
		for(int notes = 0; notes < chordParent.getChildCount(); notes++) {
			if(noteView == chordParent.getChildAt(notes)) {
				notePos = notes;
				break;
			}
		}

		// Get the selected chord and add a new chord
		sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).addChord(chordsPos);
		Chord chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);

		switch(event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			/*noteView.setBackgroundResource(R.drawable.background);
			noteView.setImageResource(R.drawable.fillednotespace);
			noteView.setScaleType(ScaleType.CENTER_INSIDE);*/

			myDelete = DeleteFlag.FIRST;
			return true;

		case DragEvent.ACTION_DRAG_ENTERED:
			Note searchNote = NoteToScreen.findNote(chordSel, notePos);

			if(searchNote == null ) {
				noteView.setBackgroundResource(R.drawable.background);
				noteView.setImageResource(currentTool.getID());
				noteView.setScaleType(ScaleType.CENTER_INSIDE);
				myDelete = DeleteFlag.DELETE;
			} else if(searchNote != null && myDelete == DeleteFlag.FIRST) {
				myDelete = DeleteFlag.DELETE;
			} else {
				myDelete = DeleteFlag.NODELETE;

			}
			return true;

		case DragEvent.ACTION_DRAG_EXITED:
			if(myDelete != DeleteFlag.NODELETE) {
				noteView.setBackgroundResource(R.drawable.nobackground);
				noteView.setImageResource(0);
				NoteToScreen.deleteNote(chordSel, notePos);
			}
			return true;

		case DragEvent.ACTION_DROP:
			noteView.setBackgroundResource(R.drawable.nobackground);
			NoteToScreen.addNote(chordSel, notePos, currentTool);
			myDelete = DeleteFlag.NODELETE;
			return true;

		}

		return false;
	}

}
