package Listeners;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.R;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class SignatureSpinnerListener implements OnItemSelectedListener {
private EditorActivity myActivity;
	
	public SignatureSpinnerListener(Context context)
	{
		myActivity = (EditorActivity)context;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		myActivity.setCurrentSignature(position);
		myActivity.setCurrentMeasure(0);
		myActivity.updateSignatureButton();
		myActivity.updateMeasureSpinner();
		myActivity.updateMeasures(myActivity.getCurrentMeasure());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
