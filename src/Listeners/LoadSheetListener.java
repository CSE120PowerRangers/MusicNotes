package Listeners;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.example.musicnotes.EditorActivity;
import com.example.musicnotes.MainActivity;

import MusicSheet.Sheet;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoadSheetListener implements OnClickListener {

	MainActivity activity;
	
	public LoadSheetListener(Context c)
	{
		activity = (MainActivity)c;
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(activity, EditorActivity.class);
		Button b = (Button)v;
		String filename = b.getText().toString();

		 
		FileInputStream fis = null;
		 ObjectInputStream in = null;
		 Sheet s = new Sheet();
		    try {
		      fis = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/MusicNotes/Sheets/" + filename);
		      in = new ObjectInputStream(fis);
		      s = (Sheet) in.readObject();
		      in.close();
		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
		    
		intent.putExtra("Sheet", s );
		activity.startActivity(intent);
	}

}
