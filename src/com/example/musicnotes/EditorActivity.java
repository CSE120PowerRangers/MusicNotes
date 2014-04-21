package com.example.musicnotes;

import File.FileMaker;
import Listeners.EditorDragListener;
import Listeners.EditorTouchListener;
import Listeners.ToolButtonListener;
import MusicSheet.*;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
import Player.Melody;
import Player.MidiPlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Path.FillType;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Display;
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
	public static enum EditorVal{NOTES, RESTS, ACCIDENTALS};
	EditorVal currentVal;
	NoteTool currentTool;
	int currentMeasure;
	int screenWidth, screenHeight;
	int measureWidth, measureHeight, chordWidth, chordHeight, noteWidth, noteHeight;

	private final Melody melody = new Melody();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		// Get Screen Size
		screenWidth = size.x;
		screenHeight = size.y;
		// Calculate Measure Size
		measureWidth = (int)(screenWidth*(9.0/10.0));
		measureHeight = (int)(screenHeight*(9.0/10.0));
		// Calculate Chord and Note
		chordWidth = (int)(measureWidth/8.0);
		chordHeight = measureHeight;
		noteWidth = chordWidth;
		noteHeight = (int)(chordHeight/16.0);
		initializeView();
		sheet = new Sheet();
		currentTool = new NoteTool(NoteType.EIGHTH_NOTE, R.drawable.fillednotespace);
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
		NoteType myType = NoteType.QUARTER_NOTE;
		int imageID = 0;
		for(int i = 0; i < toolbarButtons.length; i++) {
			toolbarButtons[i] = new ImageButton(this);
			switch(currentVal) {
			case NOTES:
				switch(i)
				{
				case 0:
					toolbarButtons[i].setImageResource(R.drawable.fillednote);
					myType = NoteType.QUARTER_NOTE;
					imageID = R.drawable.fillednotespace;
					break;
				case 1:
					toolbarButtons[i].setImageResource(R.drawable.halfnote);
					myType = NoteType.HALF_NOTE;
					imageID = R.drawable.halfnote;
					break;
				}
				break;
			case RESTS:
				toolbarButtons[i].setImageResource(R.drawable.halfnote);
				break;
			case ACCIDENTALS:
				toolbarButtons[i].setImageResource(R.drawable.four);
				break;
			}
			NoteTool myTool = new NoteTool(myType, imageID);
			ToolButtonListener myListener = new ToolButtonListener(this, myTool);
			toolbarButtons[i].setOnClickListener(myListener);
			toolbarButtons[i].setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
			toolbar.addView(toolbarButtons[i]);
		}

	}

	public void updateMeasures(int start)
	{
	
		LinearLayout selChord;



		LinearLayout noteLayout = (LinearLayout)findViewById(R.id.noteLayout);

		//Add Listener and Draw Notes
		for(int chords = 0; chords < noteLayout.getChildCount(); chords++) {
			ImageView selNote;
			selChord = (LinearLayout)noteLayout.getChildAt(chords);
			Chord c = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chords);

			for(int notes = 0; notes < selChord.getChildCount(); notes++) {
				selNote = (ImageView) selChord.getChildAt(notes);

				EditorTouchListener touchListener = new EditorTouchListener(sheet, currentMeasure, currentTool);
				selNote.setOnTouchListener(touchListener);

				EditorDragListener dragListener = new EditorDragListener(sheet, currentMeasure, currentTool);
				selNote.setOnDragListener(dragListener);
				selNote.setScaleType(ScaleType.CENTER_INSIDE);
				if(c != null) {
					Note searchNote = NoteToScreen.findNote(sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chords), notes);
					if(searchNote != null) {
						switch(searchNote.getType())
						{
						case EIGHTH_NOTE:
							selNote.setImageResource(R.drawable.fillednotespace);
							break;
						case QUARTER_NOTE:
							selNote.setImageResource(R.drawable.fillednotespace);
							break;
						case HALF_NOTE:
							selNote.setImageResource(R.drawable.halfnote);
							break;
						default:
							selNote.setImageResource(R.drawable.fillednotespace);
							break;
						}
					} else {
						selNote.setImageResource(0);
					}
				} else {
					selNote.setImageResource(0);
				}
			}
		}

	}
	
	public void initializeView()
	{

		LinearLayout topToolbar = (LinearLayout)findViewById(R.id.topToolbar);
		topToolbar.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth,screenHeight/10));
		for(int i = 0; i < topToolbar.getChildCount(); i++)
		{
			topToolbar.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(screenWidth/topToolbar.getChildCount(),LayoutParams.MATCH_PARENT));
		}
		
		LinearLayout sidePanel = (LinearLayout)findViewById(R.id.leftPanel);
		RelativeLayout.LayoutParams sidePanelParams = new RelativeLayout.LayoutParams(screenWidth/10,(int)((9.0/10.0)*screenHeight));
		sidePanelParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		sidePanel.setLayoutParams(sidePanelParams);

		// Add Measure Lines
		LinearLayout measureLayout = (LinearLayout) findViewById(R.id.measureLayout);
		
		RelativeLayout.LayoutParams measureParams = new RelativeLayout.LayoutParams(measureWidth,measureHeight);
		System.out.println("measure params " + measureParams.height + ", " + measureParams.width);
		
		measureParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		measureParams.addRule(RelativeLayout.RIGHT_OF, R.id.leftPanel);
		measureLayout.setOrientation(LinearLayout.HORIZONTAL);
		measureLayout.setLayoutParams(measureParams);

		for(int chords = 0; chords < 8; chords++)
		{
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setOrientation(LinearLayout.VERTICAL);
			chordLayout.setLayoutParams(chordParams);
			for(int notes = 0; notes < 16; notes++)
			{
				LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(noteWidth,noteHeight);
				ImageView noteView = new ImageView(this);
				noteView.setLayoutParams(noteParams);
				if(notes >= 3 && notes <=11 && notes%2 == 1) {
					noteView.setImageResource(R.drawable.line);
					noteView.setScaleType(ScaleType.FIT_XY);
				}
				chordLayout.addView(noteView);
			}
			measureLayout.addView(chordLayout);
		}

		// Add Note Holders
		LinearLayout noteLayout = (LinearLayout)findViewById(R.id.noteLayout);
		RelativeLayout.LayoutParams noteLayoutParams = new RelativeLayout.LayoutParams(measureWidth,measureHeight);
		
		noteLayoutParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		noteLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.leftPanel);
		noteLayout.setOrientation(LinearLayout.HORIZONTAL);
		noteLayout.setLayoutParams(noteLayoutParams);
		
		System.out.println(noteLayout.getHeight() + ", " + noteLayout.getWidth());
		for(int chords = 0; chords < 8; chords++)
		{
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setLayoutParams(chordParams);
			chordLayout.setOrientation(LinearLayout.VERTICAL);
			for(int notes = 0; notes < 16; notes++)
			{
				LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(noteWidth, noteHeight);
				ImageView noteView = new ImageView(this);
				noteView.setLayoutParams(noteParams);
				chordLayout.addView(noteView);
			}
			noteLayout.addView(chordLayout);	
		}
	}

	
	
	
	public void playButtonTouch(View v) {
		context = getApplicationContext();

		if(context != null && sheet != null) {
			player = new MidiPlayer(sheet, context);
			player.play();
		}
	}

	public void loadFile(View v) {
		context = getApplicationContext();

	}

	public void saveFile(View v) {
		context = getApplicationContext();
		String filename = "TESTFILE.mid";
		FileMaker.writeSheetToMidiExternal(sheet, context, filename);
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

	public void previousMeasure(View v){
		if(currentMeasure > 0)
		{
			currentMeasure--;
			measureSpinner.setSelection(currentMeasure);
			updateMeasures(currentMeasure);
		}
	}

	public void setTool(NoteTool newTool)
	{
		currentTool = newTool;
	}
	public void updateTools()
	{
		updateMeasures(currentMeasure);
	}
}
