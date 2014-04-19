package Listeners;

import com.example.musicnotes.R;

import MusicSheet.Chord;
import MusicSheet.Measure;
import MusicSheet.NoteName;
import MusicSheet.NoteType;
import MusicSheet.Sheet;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class EditorTouchListener implements OnTouchListener{

	int currentMeasure;
	Sheet sheet;

	public EditorTouchListener(Sheet sheet, int currentMeasure)
	{
		this.currentMeasure =currentMeasure;
		this.sheet = sheet;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
				break;
			}
		}

		// Get the selected chord and add a new chord
		sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).addChord(chordsPos);
		Chord chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);
	
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			noteView.setBackgroundResource(R.drawable.background);
			noteView.setImageResource(R.drawable.fillednotespace);
			noteView.setScaleType(ScaleType.CENTER_INSIDE);
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
			/*
			updateMeasures(currentMeasure);*/
			return true;
		}


		return false;
	}

}
