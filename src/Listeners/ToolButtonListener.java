package Listeners;
import com.example.musicnotes.EditorActivity;

import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;


public class ToolButtonListener implements OnClickListener {

	EditorActivity currentActivity;
	NoteTool myTool;
	
	public ToolButtonListener(EditorActivity currentActivity, NoteTool currentTool)
	{
		this.currentActivity = currentActivity;
		this.myTool = currentTool;
	}
	
	@Override
	public void onClick(View v) {
		currentActivity.setTool(myTool);
		currentActivity.updateMeasures(currentActivity.getCurrentMeasure());
	}
}
