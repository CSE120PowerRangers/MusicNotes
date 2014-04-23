package com.example.musicnotes;

import java.util.ArrayList;

import File.FileMaker;
import Listeners.*;
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

	// Activity Objects
	public static MidiPlayer player;
	private final Melody melody = new Melody();
	public Context context;
	int screenWidth, screenHeight;
	float percentageTop, percentageSide;
	ToolBar tools;
	// Layout Objects
	Spinner familySpinner, measureSpinner;
	String[] measureArray;
	public static enum ToolFamily{NOTES, RESTS, ACCIDENTALS, PLAYBACK};
	int measureWidth, measureHeight, chordWidth, chordHeight, noteWidth, noteHeight;
	int numChords, numNotes;

	// Editor Values
	Sheet sheet;
	ToolFamily currentFamily;
	NoteTool currentTool;
	int currentMeasure, currentStaff, currentSignature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Create Activity Objects
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);
		
		//Static Values
		numNotes = 15;
		percentageTop = 0.15f;
		percentageSide = 0.15f;
		
		//Initialize Tools MUST GO BEFORE VIEW IS INITIALIZED
		tools = new ToolBar(this);
		currentTool = new NoteTool(NoteType.EIGHTH_NOTE, R.drawable.fillednotespace);
		currentFamily = ToolFamily.NOTES;
		updateToolBar();
		
		// Calculate Screen Size
		calcScreenSize();

		// Initialize View
		initializeView();

		// Load in sheet and initialize values and tools
		sheet = new Sheet();
		currentMeasure = currentStaff = currentSignature = 0;
		numChords = sheet.getStaff(currentStaff).getSignature(currentSignature).getMeasure(currentMeasure).getSize();
		
		//Initialize Measure Spinner
		updateMeasureSpinner();

		// Initialize Tool Family Spinner
		familySpinner = (Spinner) findViewById(R.id.toolbarSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolbarSpinnerArray, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		familySpinner.setAdapter(adapter);
		familySpinner.setOnItemSelectedListener(new ToolSpinnerListener(this));
	}

	@Override
	public void onStart() {
		super.onStart();
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
		case R.id.save:
			saveFile();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void updateToolBar() {
		// Get the Toolbar
		LinearLayout toolbar = (LinearLayout) findViewById(R.id.notesToolBar);
		// Make sure there is nothing in there
		toolbar.removeAllViews();

		ArrayList<ImageButton> toolbarButtons = tools.getFamily(currentFamily);

		for(int i = 0; i < toolbarButtons.size(); i++) {
			toolbar.addView(toolbarButtons.get(i));
		}

	}

	public void updateMeasures(int start)
	{

		LinearLayout selChord;
		ImageView selNote;
		Chord c;
		EditorTouchListener touchListener;
		EditorDragListener dragListener;
		LinearLayout noteLayout = (LinearLayout)findViewById(R.id.noteLayout);

		//Add Listener and Draw Notes
		for(int chords = 0; chords < numChords; chords++) {

			// Get chord layout and chord in sheet
			selChord = (LinearLayout)noteLayout.getChildAt(chords);
			c = sheet.getStaff(currentStaff).getSignature(currentSignature).getMeasure(currentMeasure).getChord(chords);

			for(int notes = 0; notes < numNotes; notes++) {
				//Find Note ImageView to set
				selNote = (ImageView) selChord.getChildAt(notes);

				//Set Touch and Drag Listeners
				touchListener = new EditorTouchListener(sheet, currentMeasure, currentTool);
				selNote.setOnTouchListener(touchListener);
				dragListener = new EditorDragListener(sheet, currentMeasure, currentTool);
				selNote.setOnDragListener(dragListener);

				//Draw Notes according to sheet
				selNote.setScaleType(ScaleType.CENTER_INSIDE);
				if(c != null) {
					Note searchNote = NoteToScreen.findNote(c, notes);
					if(searchNote != null) {
						switch(searchNote.getType())
						{
						case EIGHTH_NOTE:
							selNote.setImageResource(R.drawable.four);
							break;
						case QUARTER_NOTE:
							selNote.setImageResource(R.drawable.fillednotespace);
							break;
						case HALF_NOTE:
							selNote.setImageResource(R.drawable.halfnote);
							break;
						case WHOLE_NOTE:
							selNote.setImageResource(R.drawable.treble);
						default:
							selNote.setImageResource(0);
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

	private void calcScreenSize()
	{
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = (int)(size.y * 0.90);

		// Calculate Measure Size
		measureWidth = (int)(screenWidth*(1.0f-percentageSide));
		measureHeight = (int)(screenHeight*(1.0f-percentageTop));

		// Calculate Chord and Note
		chordWidth = (int)(measureWidth/(float)numChords);
		chordHeight = measureHeight;
		noteWidth = chordWidth;
		noteHeight = (int)(chordHeight/(float)numNotes);
	}

	public void initializeView()
	{

		//Top Panel
		LinearLayout topToolbar = (LinearLayout)findViewById(R.id.topToolbar);
		topToolbar.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth,(int)(screenHeight*percentageTop)));
		
		ImageView backMeasure = (ImageView)findViewById(R.id.backwardMeasure);
		backMeasure.setLayoutParams(new LinearLayout.LayoutParams((int)(screenWidth/30.0f) ,LayoutParams.MATCH_PARENT));
		backMeasure.setScaleType(ScaleType.FIT_XY);
		ImageView forwardMeasure = (ImageView)findViewById(R.id.forwardMeasure);
		forwardMeasure.setLayoutParams(new LinearLayout.LayoutParams((int)(screenWidth/30.0f) ,LayoutParams.MATCH_PARENT));
		forwardMeasure.setScaleType(ScaleType.FIT_XY);
		
		//Side Panel
		LinearLayout sidePanel = (LinearLayout)findViewById(R.id.leftPanel);
		RelativeLayout.LayoutParams sidePanelParams = new RelativeLayout.LayoutParams(screenWidth/10,(int)(percentageSide*screenHeight));
		sidePanelParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		sidePanel.setLayoutParams(sidePanelParams);

		// Add Measure Lines
		LinearLayout measureLayout = (LinearLayout) findViewById(R.id.measureLayout);
		RelativeLayout.LayoutParams measureParams = new RelativeLayout.LayoutParams(measureWidth,measureHeight);
		measureParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		measureParams.addRule(RelativeLayout.RIGHT_OF, R.id.leftPanel);
		measureLayout.setOrientation(LinearLayout.HORIZONTAL);
		measureLayout.setLayoutParams(measureParams);

		for(int chords = 0; chords < numChords; chords++)
		{
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setOrientation(LinearLayout.VERTICAL);
			chordLayout.setLayoutParams(chordParams);
			for(int notes = 0; notes < numNotes; notes++)
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
		for(int chords = 0; chords < 8; chords++)
		{
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setLayoutParams(chordParams);
			chordLayout.setOrientation(LinearLayout.VERTICAL);
			for(int notes = 0; notes < numNotes; notes++)
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

	public void saveFile() {
		context = getApplicationContext();
		String filename = "TESTFILE.mid";
		FileMaker.writeSheetToMidi(sheet, context, filename);
	}

	public void nextMeasure(View v){
		//**** If null, create a new measure****
		if(currentMeasure == sheet.getStaff(0).getSignature(0).getSize() - 1) {
			sheet.getStaff(0).getSignature(0).addMeasure(new Measure());
			currentMeasure++;
			updateMeasureSpinner();
			
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
	
	public void setToolFamily(ToolFamily newFamily)
	{
		currentFamily = newFamily;
	}

	public int getCurrentMeasure()
	{
		return currentMeasure;
	}
	
	public void setCurrentMeasure(int measure)
	{
		currentMeasure = measure;
	}

	private void updateMeasureSpinner()
	{
		//Initialize Measure Spinner
				measureSpinner = (Spinner) findViewById(R.id.currentMeasure);

				measureArray = new String[sheet.getStaff(currentStaff).getSignature(currentSignature).getSize()];
				for(int i = 0; i < sheet.getStaff(currentStaff).getSignature(currentSignature).getSize(); i++) {
					measureArray[i] = "" +(i+1);

				}
				ArrayAdapter<String> adapterMeasure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,measureArray);
				adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				measureSpinner.setAdapter(adapterMeasure);
				measureSpinner.setOnItemSelectedListener(new MeasureSpinnerListener(this));
	}
}
