package Listeners;

import com.example.musicnotes.R;

import MusicSheet.Sheet;
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

	public EditorTouchListener(Sheet sheet, int currentMeasure) {
		this.currentMeasure =currentMeasure;
		this.sheet = sheet;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView noteView = (ImageView)v;
		// Get the chord and measure views that the note is located in

		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
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
