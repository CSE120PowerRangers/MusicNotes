package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;

import MusicSheet.Chord;
import MusicUtil.NoteType;
import android.content.ClipData;
import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class EditorTouchListener implements OnTouchListener {


	EditorActivity myActivity;

	public EditorTouchListener(Context myActivity) {
		this.myActivity = (EditorActivity) myActivity;
	}





	@Override
	public boolean onTouch(View v, MotionEvent e) {
		if(e.getAction() == MotionEvent.ACTION_DOWN)
		{
			myActivity.getCurrentTool().touchUse(myActivity,v);
		}
		return false;
	}
}
