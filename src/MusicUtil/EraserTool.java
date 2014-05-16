package MusicUtil;

import MusicSheet.Chord;
import android.content.ClipData;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;

public class EraserTool extends Tool{

	
	public EraserTool()
	{
		toolName = ToolNames.ERASER;
	}
	@Override
	public void touchUse(EditorActivity myActivity, View v) {
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
		myActivity.getCurrentMeasure().add(chordsPos);
		
			NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
			noteView.setImageResource(0);
			myActivity.setHeldTool(myActivity.getCurrentTool());
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
			return;
		
	}
	
	@Override
	public void dragUse(EditorActivity myActivity, View v) {
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
		myActivity.getCurrentMeasure().add(chordsPos);
		
			NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
			noteView.setImageResource(0);
			
	}
}
