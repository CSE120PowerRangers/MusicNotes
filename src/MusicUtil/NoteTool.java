package MusicUtil;

import MusicSheet.Chord;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;

public class NoteTool extends Tool{

	NoteType myType;
	int imageID;

	public NoteTool( NoteType currentType, int imageID)
	{
		toolName = ToolNames.NOTE;
		this.myType = currentType;
		this.imageID = imageID;
	}
	public int getID()
	{
		return imageID;
	}
	
	public NoteType getType()
	{
		return myType;
	}

	@Override
	public void touchUse(EditorActivity myActivity, View v) {
		ImageView noteView = (ImageView)v;
		NoteTool myTool = (NoteTool)myActivity.getCurrentTool();
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
		Chord chordSel = myActivity.getCurrentMeasure().get(chordsPos);

		if(NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos) != null)
		{
			myActivity.setHeldTool(NoteToScreen.notetoTool(NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos)));
			Vibrator vib = (Vibrator) myActivity.getSystemService(Context.VIBRATOR_SERVICE);
			// Delete the note
			// Vibrate on long click if there's a note to pick up
			System.out.println("Found a note");
			vib.vibrate(75);
			// Start the Drag
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
			NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
			noteView.setImageResource(0);
			return;
		}
		else
		{
			noteView.setImageResource(myTool.getID());
			noteView.setScaleType(ScaleType.CENTER_INSIDE);
			if(NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos) != null)
			{
				NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
			}
			NoteToScreen.addNote(myActivity, chordSel, notePos, myTool);
			return;
		}
	}

	@Override
	public void dragUse(EditorActivity myActivity, View v) {
		ImageView noteView = (ImageView)v;
		NoteTool heldTool = (NoteTool)myActivity.getHeldTool();
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
		Chord chordSel = myActivity.getCurrentMeasure().get(chordsPos);

		if(NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos) != null)
		{
			NoteToScreen.deleteNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
		}
		noteView.setImageResource(heldTool.getID());
		noteView.setScaleType(ScaleType.CENTER_INSIDE);
		NoteToScreen.addNote(myActivity, chordSel, notePos, heldTool);
	}



}
