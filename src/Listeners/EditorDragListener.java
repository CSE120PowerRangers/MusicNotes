package Listeners;

import android.content.*;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;
import com.example.musicnotes.R;

import MusicSheet.Chord;
import MusicSheet.Note;
import MusicSheet.Sheet;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class EditorDragListener implements OnDragListener {


	int currentMeasure;
	EditorActivity myActivity;
	NoteTool heldTool;

	public EditorDragListener(Context myActivity) {
		this.myActivity = (EditorActivity)myActivity;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		ImageView noteView = (ImageView)v;
		// Get the chord and measure views that the note is located in
		LinearLayout chordParent = (LinearLayout)noteView.getParent();
		LinearLayout measureParent = (LinearLayout) chordParent.getParent();
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
		myActivity.getCurrentMeasure().addChord(chordsPos);
		Chord chordSel = myActivity.getCurrentMeasure().getChord(chordsPos);

		heldTool = myActivity.getHeldTool();
		if(heldTool!= null)

		{
			switch(event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:	
				return true;

			case DragEvent.ACTION_DRAG_EXITED:
				return true;

			case DragEvent.ACTION_DROP:
				if(NoteToScreen.findNote(myActivity.getCurrentMeasure().getChord(chordsPos), notePos) != null)
				{
					NoteToScreen.deleteNote(myActivity.getCurrentMeasure().getChord(chordsPos), notePos);
				}
				noteView.setImageResource(heldTool.getID());
				noteView.setScaleType(ScaleType.CENTER_INSIDE);
				NoteToScreen.addNote(chordSel, notePos, heldTool);
				return true;
			}
		}

		return false;
	}

}
