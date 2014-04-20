package Listeners;

import com.example.musicnotes.R;

import MusicSheet.Sheet;
import MusicUtil.NoteTool;
import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class EditorTouchListener implements OnTouchListener {

	int currentMeasure;
	Sheet sheet;
	NoteTool currentTool;

	public EditorTouchListener(Sheet sheet, int currentMeasure, NoteTool currentTool) {
		this.currentMeasure =currentMeasure;
		this.sheet = sheet;
		this.currentTool = currentTool;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView noteView = (ImageView)v;
		// Get the chord and measure views that the note is located in

		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			noteView.setBackgroundResource(R.drawable.background);
			noteView.setImageResource(currentTool.getID());
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
