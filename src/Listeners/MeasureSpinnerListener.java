package Listeners;

import com.example.musicnotes.EditorActivity;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MeasureSpinnerListener implements OnItemSelectedListener {

	private EditorActivity myActivity;
	
	public MeasureSpinnerListener(Context context)
	{
		myActivity = (EditorActivity)context;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		myActivity.setCurrentMeasure(position);
		myActivity.updateMeasures(myActivity.getCurrentMeasure());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}

}
