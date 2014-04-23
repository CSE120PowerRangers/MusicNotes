package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.EditorActivity.ToolFamily;

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
		}
		else if(position == 1)
		{
			myActivity.setToolFamily(ToolFamily.RESTS);
		}
		else
		{
			myActivity.setToolFamily(ToolFamily.ACCIDENTALS);
		}
		myActivity.updateToolBar();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}
}
