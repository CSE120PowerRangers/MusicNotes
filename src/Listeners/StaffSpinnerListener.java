package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.R;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;

public class StaffSpinnerListener implements OnItemSelectedListener {
	private EditorActivity myActivity;
	
	public StaffSpinnerListener(Context context)
	{
		myActivity = (EditorActivity)context;
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		myActivity.setCurrentStaff(position);
		switch(myActivity.getCurrentStaff().clef())
		{
		case TREBLE:
			((ImageButton) myActivity.findViewById(R.id.staffclef)).setImageResource(R.drawable.treble);
			break;
		case TENOR:
			((ImageButton) myActivity.findViewById(R.id.staffclef)).setImageResource(R.drawable.tenor);
			break;
		case BASS:
			((ImageButton) myActivity.findViewById(R.id.staffclef)).setImageResource(R.drawable.bass);
			break;
		}
		myActivity.updateMeasures(myActivity.getCurrentMeasure());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parentView) {

	}
}
