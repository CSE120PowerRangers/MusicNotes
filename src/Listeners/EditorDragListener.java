package Listeners;

import android.content.*;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.NoteToScreen;

import MusicSheet.Chord;
import MusicUtil.NoteTool;
import MusicUtil.Tool;
import MusicUtil.Tool.ToolNames;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


public class EditorDragListener implements OnDragListener {


	int currentMeasure;
	EditorActivity myActivity;
	Tool heldTool;

	public EditorDragListener(Context myActivity) {
		this.myActivity = (EditorActivity)myActivity;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		heldTool = myActivity.getHeldTool();
		if(heldTool!= null)
		{
			switch(event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:	
				if(heldTool.getToolName()==ToolNames.ERASER)
				{
					heldTool.dragUse(myActivity, v);
				}
				return true;

			case DragEvent.ACTION_DRAG_EXITED:
				return true;
			case DragEvent.ACTION_DROP:
				if(heldTool.getToolName()==ToolNames.NOTE)
				{
					heldTool.dragUse(myActivity, v);
				}
				return true;
			}
		}
		return false;
	}

}
