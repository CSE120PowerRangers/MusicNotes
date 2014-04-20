package com.example.musicnotes;

import Listeners.EditorDragListener;
import Listeners.EditorTouchListener;
import MusicSheet.*;
import Player.MidiPlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

public class EditorActivity extends Activity{

	Sheet sheet;
	Spinner spinner, measureSpinner;
	public static MidiPlayer player;
	public Context context;
	String[] measureArray;
	enum EditorVal{NOTES, RESTS, ACCIDENTALS};
	EditorVal currentVal;
	int currentMeasure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);
		sheet = new Sheet();
		//Set Spinner and Default Value for Spinner
		currentVal = EditorVal.NOTES;
		measureSpinner = (Spinner) findViewById(R.id.currentMeasure);
		spinner = (Spinner) findViewById(R.id.toolbarSpinner);


		measureArray = new String[sheet.getStaff(0).getSignature(0).getSize()];
		for(int i = 0; i < sheet.getStaff(0).getSignature(0).getSize(); i++) {
			measureArray[i] = "" +i;
		}

		// Insert Options into Spinners
		ArrayAdapter<String> adapterMeasure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,measureArray);
		adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		measureSpinner.setAdapter(adapterMeasure);

		measureSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				currentMeasure = position;
				updateMeasures(currentMeasure);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolbarSpinnerArray, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		//Add Selection Listener to Spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if(position == 0)
				{
					currentVal = EditorVal.NOTES;
				}
				else if(position == 1)
				{
					currentVal = EditorVal.RESTS;
				}
				else
				{
					currentVal = EditorVal.ACCIDENTALS;
				}
				updateToolBar();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		//Update the ToolBar with default Items
	}

	@Override
	public void onStart() {
		super.onStart();
		updateToolBar();
		updateMeasures(currentMeasure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.main_menu:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);            	
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void updateToolBar() {
		LinearLayout toolbar = (LinearLayout) findViewById(R.id.notesToolBar);
		// Make sure there is nothing in there
		toolbar.removeAllViews();

		ImageButton[] toolbarButtons = new ImageButton[2];
		for(int i = 0; i < toolbarButtons.length; i++) {
			toolbarButtons[i] = new ImageButton(this);
			switch(currentVal) {
			case NOTES:
				toolbarButtons[i].setImageResource(R.drawable.fillednote);
				break;
			case RESTS:
				toolbarButtons[i].setImageResource(R.drawable.halfnote);
				break;
			case ACCIDENTALS:
				toolbarButtons[i].setImageResource(R.drawable.four);
				break;
			}
			toolbarButtons[i].setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
			toolbar.addView(toolbarButtons[i]);
		}

	}

	public void updateMeasures(int start)
	{
		RelativeLayout measureLayout = (RelativeLayout) findViewById(R.id.measureLayout);


		RelativeLayout selChord;



		//**Drawing the measure
		for(int chords = 0; chords < measureLayout.getChildCount(); chords++) {
			ImageView selNote;
			selChord = (RelativeLayout)measureLayout.getChildAt(chords);
			for(int notes = 0; notes < selChord.getChildCount(); notes++) {
				selNote = (ImageView) selChord.getChildAt(notes);
				if(notes >= 3 && notes <=11 && notes%2 == 1) {
					selNote.setImageResource(R.drawable.line);
					selNote.setScaleType(ScaleType.FIT_XY);
				}
			}
		}

		RelativeLayout noteLayout = (RelativeLayout) findViewById(R.id.NoteLayout);

		//Add Listener and Draw Notes
		for(int chords = 0; chords < noteLayout.getChildCount(); chords++) {
			ImageView selNote;
			selChord = (RelativeLayout)noteLayout.getChildAt(chords);
			Chord c = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chords);
			
			for(int notes = 0; notes < selChord.getChildCount(); notes++) {
				selNote = (ImageView) selChord.getChildAt(notes);

				EditorTouchListener touchListener = new EditorTouchListener(sheet, currentMeasure);
				selNote.setOnTouchListener(touchListener);

				EditorDragListener dragListener = new EditorDragListener(sheet, currentMeasure);
				selNote.setOnDragListener(dragListener);

				if(c != null) {
					Note searchNote = NoteToScreen.findNote(sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chords), notes);
					if(searchNote != null) {
						selNote.setImageResource(R.drawable.fillednotespace);
					} else {
						selNote.setImageResource(0);
					}
				} else {
					selNote.setImageResource(0);
				}
			}
		}

		/*TextView myText = (TextView) findViewById(R.id.currentMeasure);
		myText.setText(currentMeasure);*/
	}

	public void playButtonTouch(View v) {
		context = getApplicationContext();

		if(context != null && sheet != null) {
			player = new MidiPlayer(sheet, context);
			player.play();
		}
	}

	public void nextMeasure(View v){
		//**** If null, create a new measure****
		if(currentMeasure == sheet.getStaff(0).getSignature(0).getSize() - 1) {
			sheet.getStaff(0).getSignature(0).addMeasure(new Measure());
			currentMeasure++;
			
			measureArray = new String[sheet.getStaff(0).getSignature(0).getSize()];
			for(int i = 0; i < sheet.getStaff(0).getSignature(0).getSize(); i++) {
				measureArray[i] = "" +i;
			}

			// Insert Options into Spinners
			ArrayAdapter<String> adapterMeasure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,measureArray);
			adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			measureSpinner.setAdapter(adapterMeasure);

			measureSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					currentMeasure = position;
					updateMeasures(currentMeasure);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {

				}

			});
			
		} else {
			//**** Increment the current Measure by one. ****
			currentMeasure++;
		}

		measureSpinner.setSelection(currentMeasure);
		updateMeasures(currentMeasure);
	}
}
