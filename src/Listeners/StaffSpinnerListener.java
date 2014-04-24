package Listeners;

import com.example.musicnotes.EditorActivity;

import MusicSheet.Staff;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class StaffSpinnerListener implements OnItemSelectedListener {
	private EditorActivity myActivity;
	
	public StaffSpinnerListener(Context context)
	{
		myActivity = (EditorActivity)context;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		myActivity.setCurrentStaff(position);
		myActivity.updateMeasures(myActivity.getCurrentMeasure());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}
}
