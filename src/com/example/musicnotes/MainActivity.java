package com.example.musicnotes;

import java.io.File;

import Listeners.MeasureSpinnerListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity{

	private String sheetName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		LinearLayout fileSpinner = (LinearLayout) findViewById(R.id.listofSheets);
		File musicNotesFile = new File(Environment.getExternalStorageDirectory().toString() + "/MusicNotes");
		String[] fileArray = musicNotesFile.list();
		for(int i = 0; i < fileArray.length; i++)
		{
			Button myFile = new Button(this);
			myFile.setText(fileArray[i]);
			fileSpinner.addView(myFile);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void newSheet(View v)
	{
		showDialog();
		
	}


	private void showDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	   	 LayoutInflater inflater = this.getLayoutInflater();

	   	    // Inflate and set the layout for the dialog
	   	    // Pass null as the parent view because its going in the dialog layout
	   	    builder.setView(inflater.inflate(R.layout.newsheet_layout, null))
	   	    // Add action buttons
	   	           .setPositiveButton("Create", new DialogInterface.OnClickListener() {
	   	               @Override
	   	               public void onClick(DialogInterface dialog, int id) {
	   	            	   Dialog d = (Dialog) dialog;
	   	                   EditText textView = (EditText)d.findViewById(R.id.newname);
	   	            	   sheetName = textView.getText().toString();
	   	            	   sheetName = sheetName.trim();
	   	            	   
	   	            	   if(sheetName.isEmpty())
	   	            	   {
	   	            		   sheetName = "Default";
	   	            	   }
	   	            	   System.out.println(sheetName);
	   	            	   startActivity();
	   	               }
	   	           })
	   	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	   	               public void onClick(DialogInterface dialog, int id) {
	   	                   dialog.cancel();
	   	               }
	   	           });      

	    	AlertDialog myAlert = builder.create();
	    	myAlert.show();
	}
	
	private void startActivity()
	{
		Intent intent = new Intent(this, EditorActivity.class);
		intent.putExtra("nameofSheet", sheetName);
		startActivity(intent);
	}
}
