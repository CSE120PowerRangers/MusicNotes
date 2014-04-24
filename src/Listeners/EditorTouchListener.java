package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;
import com.example.musicnotes.R;

import MusicSheet.Chord;
import MusicSheet.Sheet;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import android.content.ClipData;
import android.content.Context;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class EditorTouchListener implements OnClickListener {


	EditorActivity myActivity;

	public EditorTouchListener(Context myActivity) {
		this.myActivity = (EditorActivity) myActivity;
	}


	@Override
	public void onClick(View v) {
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
		if(myActivity.getCurrentTool().getType() == NoteType.NOTANOTE)
		{
			NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().getChord(chordsPos), notePos);
			noteView.setImageResource(0);
		}
		else
		{
			noteView.setImageResource(myActivity.getCurrentTool().getID());
			noteView.setScaleType(ScaleType.CENTER_INSIDE);
			if(NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().getChord(chordsPos), notePos) != null)
			{
				NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().getChord(chordsPos), notePos);
			}
			NoteToScreen.addNote(myActivity, chordSel, notePos, myActivity.getCurrentTool());
		}
	}

}
