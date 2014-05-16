package Listeners;

import android.content.*;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;

import MusicSheet.Chord;
import MusicSheet.Note;
import MusicUtil.NoteTool;
import MusicUtil.Tool;
import MusicUtil.Tool.ToolNames;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


public class EditorDragListener implements OnDragListener {


	int currentMeasure;
	EditorActivity myActivity;

	
	public EditorDragListener(Context myActivity) {
		this.myActivity = (EditorActivity)myActivity;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
			switch(event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:	
				if(myActivity.getCurrentTool().getToolName()==ToolNames.ERASER)
				{
					myActivity.getCurrentTool().dragUse(myActivity, v);
				}
				return true;

			case DragEvent.ACTION_DRAG_EXITED:
				return true;
			case DragEvent.ACTION_DROP:
				if(myActivity.getCurrentTool().getToolName()==ToolNames.NOTE && myActivity.getHeldNote()!= null)
				{
					myActivity.getCurrentTool().dragUse(myActivity, v);
				}
				return true;
			
		}
		return false;
	}

}
