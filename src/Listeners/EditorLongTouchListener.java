package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;
import com.example.musicnotes.R;

import MusicSheet.Note;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EditorLongTouchListener implements OnLongClickListener {

	EditorActivity myActivity;

	public EditorLongTouchListener(Context myActivity)
	{
		this.myActivity = (EditorActivity) myActivity;
	}

	@Override
	public boolean onLongClick(View v) {
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

		// Create a new tool from the old note

		myActivity.setHeldTool(NoteToScreen.notetoTool(NoteToScreen.findNote(myActivity.getCurrentMeasure().getChord(chordsPos), notePos)));
		Vibrator vib = (Vibrator) myActivity.getSystemService(Context.VIBRATOR_SERVICE);

		// Delete the note
		
		if(NoteToScreen.findNote(myActivity.getCurrentMeasure().getChord(chordsPos), notePos) != null) {
			// Vibrate on long click if there's a note to pick up
			System.out.println("Found a note");
			vib.vibrate(75);
			NoteToScreen.deleteNote(myActivity.getCurrentMeasure().getChord(chordsPos), notePos);
			noteView.setImageResource(0);
			
			// Start the Drag
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
		}

		return true;
	}
}
