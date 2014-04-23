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
	int myID;
	public ToolButtonListener(EditorActivity currentActivity, NoteTool currentTool, int ID)
	{
		this.currentActivity = currentActivity;
		this.myTool = currentTool;
		myID = ID;
	}
	
	@Override
	public void onClick(View v) {
		currentActivity.setCurrentTool(myTool);
		currentActivity.setActiveToolID(myID);
		currentActivity.updateToolBar();
		currentActivity.updateMeasures(currentActivity.getCurrentMeasure());
	}
	
	public int getID()
	{
		return myID;
	}
}
