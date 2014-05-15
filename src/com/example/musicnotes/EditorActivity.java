package com.example.musicnotes;

import java.util.ArrayList;

import File.FileMaker;
import Listeners.*;
import MusicSheet.*;
import MusicUtil.EnumClef;
import MusicUtil.EnumKeySignature;
import MusicUtil.EnumTimeSignature;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
//import Player.Melody;
import Player.MidiPlayer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.ImageView.ScaleType;

public class EditorActivity extends Activity{

	// Activity Objects
	private final MidiPlayer player = new MidiPlayer();
	//private final Melody melody = new Melody();
	public Context context;
	int screenWidth, screenHeight;
	float percentageTop, percentageSide;
	ToolBar tools;
	// Layout Objects
	Spinner familySpinner, measureSpinner, staffSpinner;
	String[] measureArray, staffArray;
	public static enum ToolFamily{NOTES, RESTS, ACCIDENTALS, PLAYBACK};
	int measureWidth, measureHeight, chordWidth, chordHeight, noteWidth, noteHeight;
	int numChords, numNotes;

	// Editor Values
	Sheet sheet;
	ToolFamily currentFamily;
	NoteTool currentTool, heldTool;
	int currentMeasure, currentStaff, currentSignature, activeTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Create Activity Objects
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);

		//Static Values
		currentMeasure = currentStaff = currentSignature = 0;
		numNotes = 15;
		numChords = 8;
		percentageTop = 0.15f;
		percentageSide = 0.10f;
		heldTool = null;

		//Initialize Tools MUST GO BEFORE VIEW IS INITIALIZED
		tools = new ToolBar(this);
		currentTool = new NoteTool(NoteType.EIGHTH_NOTE, R.drawable.eigthnote);
		activeTool = 0;
		currentFamily = ToolFamily.NOTES;
		updateToolBar();

		// Load in sheet and initialize values and tools
		sheet = new Sheet();
		Intent mainIntent = getIntent();
		sheet.setName(mainIntent.getStringExtra("nameofSheet"));

		this.setTitle(sheet.name());
		calcScreenSize();
		initializeView();
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
		updateMeasures(getCurrentMeasure());
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
			if(i == activeTool) {
				toolbarButtons.get(i).setBackgroundResource(R.drawable.background);
			} else {
				toolbarButtons.get(i).setBackgroundResource(0);
			}
		}



	}

	public void updateMeasures(Measure currentMeasure) {
		LinearLayout selChord;
		ImageView selNote;
		Chord c;
		EditorTouchListener touchListener;
		EditorDragListener dragListener;
		EditorLongTouchListener longTouchListener;
		LinearLayout noteLayout = (LinearLayout)findViewById(R.id.noteLayout);

		//Add Listener and Draw Notes
		for(int chords = 0; chords < numChords; chords++) {

			// Get chord layout and chord in sheet
			selChord = (LinearLayout)noteLayout.getChildAt(chords);
			c = currentMeasure.get(chords);

			for(int notes = 0; notes < numNotes; notes++) {
				//Find Note ImageView to set
				selNote = (ImageView) selChord.getChildAt(notes);

				//Set Touch and Drag Listeners
				touchListener = new EditorTouchListener(this);
				selNote.setOnTouchListener(touchListener);
				//longTouchListener = new EditorLongTouchListener(this);
				//selNote.setOnLongClickListener(longTouchListener);
				dragListener = new EditorDragListener(this);
				selNote.setOnDragListener(dragListener);

				//Draw Notes according to sheet
				//selNote.setScaleType(ScaleType.FIT_XY);
				if(c != null) {
					Note searchNote = NoteToScreen.findNote(this, c, notes);

					NoteTool searchNoteTool = NoteToScreen.notetoTool(searchNote);
					if(searchNoteTool != null) {
						selNote.setImageResource(searchNoteTool.getID());
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
		screenHeight = (int)(size.y * 0.85);

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

		// Measure Navigation Buttons
		ImageView backMeasure = (ImageView)findViewById(R.id.backwardMeasure);
		backMeasure.setLayoutParams(new LinearLayout.LayoutParams((int)(screenWidth/25.0f) ,LayoutParams.MATCH_PARENT));
		backMeasure.setScaleType(ScaleType.FIT_XY);
		backMeasure.setPadding(5, 0, 5, 0);

		ImageView forwardMeasure = (ImageView)findViewById(R.id.forwardMeasure);
		forwardMeasure.setLayoutParams(new LinearLayout.LayoutParams((int)(screenWidth/25.0f) ,LayoutParams.MATCH_PARENT));
		forwardMeasure.setScaleType(ScaleType.FIT_XY);
		forwardMeasure.setPadding(5, 0, 5, 0);

		//Side Panel
		LinearLayout sidePanel = (LinearLayout)findViewById(R.id.leftPanel);
		RelativeLayout.LayoutParams sidePanelParams = new RelativeLayout.LayoutParams((int)(screenWidth*percentageSide),(int)((1.0f-percentageTop)*screenHeight));
		sidePanelParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		sidePanel.setLayoutParams(sidePanelParams);

		ImageButton keySig = (ImageButton)findViewById(R.id.keysignaturemeasure);
		keySig.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(measureHeight/4.0f)));
		keySig.setScaleType(ScaleType.FIT_XY);
		LinearLayout timeSigLayout = (LinearLayout)findViewById(R.id.timeSignatureLayout);
		timeSigLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(measureHeight/4.0f)));
		ImageView topTime = (ImageView)findViewById(R.id.timesigtop);
		topTime.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, measureHeight/8));
		//topTime.setScaleType(ScaleType.FIT_XY);
		ImageView botTime = (ImageView)findViewById(R.id.timesigbot);
		botTime.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, measureHeight/8));
		//botTime.setScaleType(ScaleType.FIT_XY);
		ImageButton staffClef = (ImageButton)findViewById(R.id.staffclef);
		staffClef.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(measureHeight/4.0f)));
		staffClef.setScaleType(ScaleType.CENTER_INSIDE);

		// Add Measure Lines
		LinearLayout measureLayout = (LinearLayout) findViewById(R.id.measureLayout);
		RelativeLayout.LayoutParams measureParams = new RelativeLayout.LayoutParams(measureWidth,measureHeight);
		measureParams.addRule(RelativeLayout.BELOW, R.id.topToolbar);
		measureParams.addRule(RelativeLayout.RIGHT_OF, R.id.leftPanel);
		measureLayout.setOrientation(LinearLayout.HORIZONTAL);
		measureLayout.setLayoutParams(measureParams);

		for(int chords = 0; chords < numChords; chords++) {
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setOrientation(LinearLayout.VERTICAL);
			chordLayout.setLayoutParams(chordParams);

			for(int notes = 0; notes < numNotes; notes++) {
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

		for(int chords = 0; chords < 8; chords++) {
			LinearLayout.LayoutParams chordParams = new LinearLayout.LayoutParams(chordWidth, chordHeight);
			LinearLayout chordLayout = new LinearLayout(this);
			chordLayout.setLayoutParams(chordParams);
			chordLayout.setOrientation(LinearLayout.VERTICAL);

			for(int notes = 0; notes < numNotes; notes++) {
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


		if(context != null && sheet != null && !player.isPlaying()) {
			player.initSheet(sheet, this);
			player.play();
		}
	}

	public void loadFile(View v) {

	}

	public void saveFile() {
		FileMaker.writeSheetToMidiInternal(sheet, this);
	}

	public void nextMeasure(View v){
		//**** If null, create a new measure****
		if(currentMeasure == sheet.get(currentSignature).get(currentStaff).size() - 1) {
			for(int sigs = 0; sigs < sheet.size(); sigs++)
			{
				for(int staves = 0; staves < sheet.get(sigs).size(); staves++)
				{
					sheet.get(sigs).get(staves).add(new Measure());
				}
			}
			currentMeasure++;
			updateMeasureSpinner();

		} else {
			//**** Increment the current Measure by one. ****
			currentMeasure++;
		}
		measureSpinner.setSelection(currentMeasure);
		updateMeasures(getCurrentMeasure());
	}

	public void previousMeasure(View v) {
		if(currentMeasure > 0) {
			currentMeasure--;
			measureSpinner.setSelection(currentMeasure);
			updateMeasures(getCurrentMeasure());
		}
	}

	public NoteTool getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(NoteTool newTool) {
		currentTool = newTool;
	}

	public void setActiveToolID(int tool) {
		activeTool = tool;
	}

	public void setToolFamily(ToolFamily newFamily) {
		currentFamily = newFamily;
	}

	public NoteTool getHeldTool() {
		return heldTool;
	}

	public void setHeldTool(NoteTool heldTool) {
		this.heldTool = heldTool;
	}

	private void updateMeasureSpinner()
	{
		//Initialize Measure Spinner
		measureSpinner = (Spinner) findViewById(R.id.currentMeasure);
		measureArray = new String[sheet.get(currentSignature).get(currentStaff).size()];

		for(int i = 0; i < sheet.get(currentSignature).get(currentStaff).size(); i++) {
			measureArray[i] = "" +(i+1);
		}

		ArrayAdapter<String> adapterMeasure = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,measureArray);
		adapterMeasure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		measureSpinner.setAdapter(adapterMeasure);
		measureSpinner.setOnItemSelectedListener(new MeasureSpinnerListener(this));
	}



	public Sheet getSheet() {
		return sheet;
	}

	public void setCurrentSignature(int newSig) {
		currentSignature = newSig;
	}

	public Staff getCurrentStaff() {
		return sheet.get(currentSignature).get(currentStaff);
	}

	public void setCurrentStaff(int newStaff) {
		currentStaff = newStaff;
	}

	public Signature getCurrentSignature() {
		return sheet.get(currentSignature);
	}

	public Measure getCurrentMeasure() {
		return getCurrentStaff().get(currentMeasure);
	}

	public void setCurrentMeasure(int measure) {
		currentMeasure = measure;
	}

	public void alertStaff(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.staffoptions_layout, null))
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
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

	public void changeStaff(View v) 
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout staffLayout = (LinearLayout) inflater.inflate(R.layout.changestaff_layout, null);
		staffSpinner = (Spinner) staffLayout.findViewById(R.id.changestaffspinner);
		String[] changeStaffArray = new String[sheet.get(currentSignature).size()];
		for(int i = 0; i < sheet.get(currentSignature).size(); i++) {
			changeStaffArray[i] = "" +(i+1);
		}
		ArrayAdapter<String> adapterStaff = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, changeStaffArray);
		adapterStaff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		staffSpinner.setAdapter(adapterStaff);
		staffSpinner.setOnItemSelectedListener(new StaffSpinnerListener(this));
		staffSpinner.setSelection(currentStaff);

		builder.setView(staffLayout)
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
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

	public void addStaff(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.addstaff_layout, null))
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Dialog d = (Dialog) dialog;
				RadioGroup clefRadio = (RadioGroup) d.findViewById(R.id.addstaffradio);
				ImageButton staffButton = (ImageButton)findViewById(R.id.staffclef);
				switch(clefRadio.getCheckedRadioButtonId())
				{
				case R.id.addstaffTreble:
					for(int sig = 0; sig < sheet.size(); sig++)
					{
						sheet.get(sig).add(new Staff(EnumClef.TREBLE));
					}
					break;
				case R.id.addstaffTenor:
					for(int sig = 0; sig < sheet.size(); sig++)
					{
						sheet.get(sig).add(new Staff(EnumClef.TENOR));
					}
					break;
				case R.id.addstaffBass:
					for(int sig = 0; sig < sheet.size(); sig++)
					{
						sheet.get(sig).add(new Staff(EnumClef.BASS));
					}
					break;
				}
				setCurrentStaff(getCurrentSignature().size()-1);
				updateStaffButton();
				updateMeasures(getCurrentMeasure());
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

	public void deleteStaff(View v)
	{
		if(getCurrentSignature().size()>1)
		{
			for(int sig = 0; sig < sheet.size(); sig++)
			{
				sheet.get(sig).delete(sheet.get(sig).get(currentStaff));
			}
			if(currentStaff==0)
			{

			}
			else
			{
				currentStaff--;
			}
			setCurrentStaff(currentStaff);
			updateStaffButton();
			updateMeasures(getCurrentMeasure());
		}
	}

	public void alertSignature(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.signatureoptions_layout, null))
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
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

	public void changeSignature(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout staffLayout = (LinearLayout) inflater.inflate(R.layout.changesignature_layout, null);
		Spinner sigSpinner = (Spinner) staffLayout.findViewById(R.id.changesignaturespinner);
		String[] changeSignatureArray = new String[sheet.size()];
		for(int i = 0; i < sheet.size(); i++) {
			changeSignatureArray[i] = "" +(i+1);
		}
		ArrayAdapter<String> adapterSignature = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, changeSignatureArray);
		adapterSignature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sigSpinner.setAdapter(adapterSignature);
		sigSpinner.setOnItemSelectedListener(new SignatureSpinnerListener(this));
		sigSpinner.setSelection(currentSignature);

		builder.setView(staffLayout)
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
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
	
	public void addSignature(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		LinearLayout sigLayout = (LinearLayout) inflater.inflate(R.layout.addsignature_layout, null);
		Spinner signatureSpinner = (Spinner) sigLayout.findViewById(R.id.keysignaturespinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.keySignatures, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		signatureSpinner.setAdapter(adapter);
		signatureSpinner.setSelection(0);

		builder.setView(sigLayout)
		.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Dialog d = (Dialog) dialog;
				EnumKeySignature newKey = EnumKeySignature.C_MAJOR;
				switch(((Spinner)d.findViewById(R.id.keysignaturespinner)).getSelectedItemPosition())
				{
				case 0:
					//"B Major/G# Minor"
					newKey = EnumKeySignature.B_MAJOR;
					break;
				case 1:
					//"E Major/C# Minor"
					newKey = EnumKeySignature.E_MAJOR;
					break;

				case 2:
					//"A Major/F# Minor"
					newKey = EnumKeySignature.A_MAJOR;
					break;
				case 3:
					//"D Major/B Minor"
					newKey = EnumKeySignature.D_MAJOR;
					break;

				case 4:
					//"G Major/E Minor"
					newKey = EnumKeySignature.G_MAJOR;
					break;

				case 5:
					//"C Major/A Minor"
					newKey = EnumKeySignature.C_MAJOR;
					break;
				}
				sheet.add(new Signature(newKey,EnumTimeSignature.FOUR_FOUR,120));
				for(int staves = 0; staves < getCurrentSignature().size(); staves++)
				{
					sheet.get(sheet.size()-1).add(new Staff(getCurrentSignature().get(staves).clef()));
				}
				setCurrentSignature(sheet.size()-1);
				getCurrentSignature().setKeySignature(newKey);
				setCurrentMeasure(0);
				updateSignatureButton();
				updateMeasures(getCurrentMeasure());
				
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

	public void deleteSignature(View v)
	{
		if(sheet.size()>1)
		{
			sheet.delete(getCurrentSignature());
			if(currentSignature==0)
			{
			
			}
			else
			{
				currentSignature--;
			}
			setCurrentSignature(currentSignature);
			//updateStaffButton();
		}
		updateSignatureButton();
		updateMeasures(getCurrentMeasure());
	}


	public void updateSignatureButton()
	{
		switch(getCurrentStaff().clef())
		{
		case TREBLE:
		case TENOR:
			switch(getCurrentSignature().keySignature())
			{
			case B_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.tbsignature);
				break;
			case E_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.tesignature);
				break;
			case A_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.tasignature);
				break;
			case D_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.tdsignature);
				break;
			case G_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.tgsignature);
				break;
			case C_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.csignature);
				break;	
			}
			break;
		case BASS:	
			switch(getCurrentSignature().keySignature())
			{
			case B_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.bbsignature);
				break;
			case E_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.besignature);
				break;
			case A_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.basignature);
				break;
			case D_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.bdsignature);
				break;
			case G_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.bgsignature);
				break;
			case C_MAJOR:
				((ImageButton) findViewById(R.id.keysignaturemeasure)).setImageResource(R.drawable.csignature);
				break;	
			}
			break;
		}
	}
	
	public void updateStaffButton()
	{
		switch(getCurrentStaff().clef())
		{
		case TREBLE:
			((ImageButton) findViewById(R.id.staffclef)).setImageResource(R.drawable.treble);
			break;
		case TENOR:
			((ImageButton) findViewById(R.id.staffclef)).setImageResource(R.drawable.tenor);
			break;
		case BASS:
			((ImageButton) findViewById(R.id.staffclef)).setImageResource(R.drawable.bass);
			break;
		}	
	}
}