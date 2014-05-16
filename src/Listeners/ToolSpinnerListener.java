package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.R;
import com.example.musicnotes.EditorActivity.ToolFamily;

import MusicUtil.AccidentalTool;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import MusicUtil.AccidentalTool.AccidentalType;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ToolSpinnerListener implements OnItemSelectedListener {
	
	private EditorActivity myActivity;
	
	public ToolSpinnerListener(Context context)
	{
		myActivity = (EditorActivity) context;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		if(position == 0)
		{
			 myActivity.setToolFamily(ToolFamily.NOTES);
			 myActivity.setCurrentTool(new NoteTool(NoteType.EIGHTH_NOTE, R.drawable.eigthnote));
		}
		else if(position == 1)
		{
			myActivity.setToolFamily(ToolFamily.RESTS);
		}
		else if(position == 2)
		{
			myActivity.setToolFamily(ToolFamily.ACCIDENTALS);
			myActivity.setCurrentTool(new AccidentalTool(AccidentalType.SHARP));
		}
		else
		{
			myActivity.setToolFamily(ToolFamily.PLAYBACK);
		}
		myActivity.updateToolBar();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}
}
