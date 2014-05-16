package MusicUtil;

import MusicSheet.Chord;
import MusicSheet.Note;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;
import com.example.musicnotes.R;


public class AccidentalTool extends Tool{

	public enum AccidentalType{NONE, SHARP, FLAT, NATURAL, DOT}
	AccidentalType myType;
	public AccidentalTool(AccidentalType type)
	{
		toolName = ToolNames.ACCIDENTALS;
		myType = type;
	}

	@Override
	public void touchUse(EditorActivity myActivity, View v) {
		ImageView noteView = (ImageView)v;
		AccidentalTool myTool = (AccidentalTool)myActivity.getCurrentTool();
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
			Note noteSel = NoteToScreen.findNote(myActivity, myActivity.getCurrentMeasure().get(chordsPos), notePos);
			System.out.println("Found a note");
			// Start the Drag

			switch(myType)
			{
			case SHARP:
				noteSel.setAccidental(AccidentalType.SHARP);
				break;
			case FLAT:
				noteSel.setAccidental(AccidentalType.FLAT);
				break;
			case NATURAL:
				noteSel.setAccidental(AccidentalType.NATURAL);
				break;
			case DOT:
				noteSel.setType(dotConvert(noteSel));
			}
			myActivity.updateMeasures(myActivity.getCurrentMeasure());
			return;
		}
	}
	
	private NoteType dotConvert(Note n)
	{
		switch(n.type())
		{
		case EIGHTH_NOTE:
			return NoteType.DOTTED_EIGHTH_NOTE;
		case QUARTER_NOTE:
			return NoteType.DOTTED_QUARTER_NOTE;
		case HALF_NOTE:
			return NoteType.DOTTED_HALF_NOTE;
		}
		return n.type();
	}
}
