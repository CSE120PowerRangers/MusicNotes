package MusicUtil;

import android.view.View;

import com.example.musicnotes.EditorActivity;

public class Tool {
	public enum ToolNames{NOTE, ERASER, ACCIDENTALS};
	protected ToolNames toolName;
	public void touchUse(EditorActivity myActivity, View v){}
	public void dragUse(EditorActivity myActivity, View v){}
	public ToolNames getToolName(){return toolName;}
	
}
