package com.example.musicnotes;

import MusicSheet.*;
import Player.Player;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditorActivity extends Activity{

	Sheet sheet;
	Spinner spinner;
	Player player;
	enum EditorVal{NOTES, RESTS, ACCIDENTALS};
	EditorVal currentVal;
	int currentMeasure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor_layout);

		//Set Spinner and Default Value for Spinner
		currentVal = EditorVal.NOTES;
		currentMeasure = 0;
		spinner = (Spinner) findViewById(R.id.toolbarSpinner);

		// Insert Options into Spinner
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



		sheet = new Sheet();
		player = new Player(sheet);



	}

	@Override
	public void onStart()
	{
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


	public void updateToolBar()
	{
		LinearLayout toolbar = (LinearLayout) findViewById(R.id.notesToolBar);
		// Make sure there is nothing in there
		toolbar.removeAllViews();

		ImageButton[] toolbarButtons = new ImageButton[2];
		for(int i = 0; i < toolbarButtons.length; i++)
		{
			toolbarButtons[i] = new ImageButton(this);
			switch(currentVal)
			{
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
		LinearLayout measureLayout = (LinearLayout) findViewById(R.id.measureLayout);
		Measure measureLookUp = sheet.getStaff(0).getSignature(0).getMeasure(start);
		LinearLayout selChord;
		ImageView selNote;
		
	
		for(int chords = 0; chords < measureLayout.getChildCount(); chords++)
		{
			selChord = (LinearLayout)measureLayout.getChildAt(chords);
			for(int notes = 0; notes < selChord.getChildCount(); notes++)
			{
				selNote = (ImageView) selChord.getChildAt(notes);
				
				if(notes >= 3 && notes <=11 && notes%2 == 1)
				{
					selNote.setImageResource(R.drawable.line);
				}
				
				/*
				OnDragListener dragListener = new OnDragListener() {
					
					@Override
					public boolean onDrag(View v, DragEvent event) {
						LinearLayout chordParent = (LinearLayout)v.getParent();
						LinearLayout measureParent = (LinearLayout) chordParent.getParent();
						int chordsPos = -1, notesPos = -1;
						
						for(int chords = 0; chords<measureParent.getChildCount(); chords++)
						{
							if(chordParent == measureParent.getChildAt(chords))
							{
								chordsPos = chords;
								break;
							}
						}
						
						for(int notes = 0; notes < chordParent.getChildCount(); notes++)
						{
							if(v == chordParent.getChildAt(notes))
							{
								notesPos = notes;
								break;
							}
						}
						
						Chord chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);
						switch(event.getAction())
						{
							case DragEvent.ACTION_DRAG_ENTERED:
								
								switch(notesPos)
								{
									case 0:
										break;
									case 1:
										break;
									case 2:
										break;
									case 3:
										break;
									case 4:
										chordSel.addNote(NoteName.E, NoteType.QUARTER_NOTE, 4);
										break;
									case 5:
										chordSel.addNote(NoteName.D, NoteType.QUARTER_NOTE, 4);
										break;
									case 6:
										chordSel.addNote(NoteName.C, NoteType.QUARTER_NOTE, 4);
										break;
									case 7:
										chordSel.addNote(NoteName.B, NoteType.QUARTER_NOTE, 4);
										break;
									case 8:
										chordSel.addNote(NoteName.A, NoteType.QUARTER_NOTE, 4);
										break;
									case 9:
										chordSel.addNote(NoteName.G, NoteType.QUARTER_NOTE, 4);
										break;
									case 10:
										chordSel.addNote(NoteName.F, NoteType.QUARTER_NOTE, 4);
										break;
									case 11:
										break;
									case 12:
										break;
									case 13:
										break;
									case 14:
										break;
								}
								updateMeasures(currentMeasure);
								return true;
						
							
							case DragEvent.ACTION_DRAG_EXITED:
								
								switch(notesPos)
								{
									case 0:
										break;
									case 1:
										break;
									case 2:
										break;
									case 3:
										break;
									case 4:
										chordSel.deleteNote(NoteName.E, 4);
										break;
									case 5:
										chordSel.deleteNote(NoteName.D, 4);
										break;
									case 6:
										chordSel.deleteNote(NoteName.C, 4);
										break;
									case 7:
										chordSel.deleteNote(NoteName.B, 4);
										break;
									case 8:
										chordSel.deleteNote(NoteName.A, 4);
										break;
									case 9:
										chordSel.deleteNote(NoteName.G, 4);
										break;
									case 10:
										chordSel.deleteNote(NoteName.F, 4);
										break;
									case 11:
										break;
									case 12:
										break;
									case 13:
										break;
									case 14:
										break;
								}
								updateMeasures(currentMeasure);
								return true;
						}
				
					
					
						return false;
					}
				};
				*/
				
				OnTouchListener touchListener = new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						LinearLayout chordParent = (LinearLayout)v.getParent();
						LinearLayout measureParent = (LinearLayout) chordParent.getParent();
						int chordsPos = -1, notesPos = -1;
						
						for(int chords = 0; chords<measureParent.getChildCount(); chords++)
						{
							if(chordParent == measureParent.getChildAt(chords))
							{
								chordsPos = chords;
								break;
							}
						}
						
						for(int notes = 0; notes < chordParent.getChildCount(); notes++)
						{
							if(v == chordParent.getChildAt(notes))
							{
								notesPos = notes;
								break;
							}
						}
						
						Chord chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);
						if(chordSel == null)
						{
							sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).addChord(chordsPos);
							chordSel = sheet.getStaff(0).getSignature(0).getMeasure(currentMeasure).getChord(chordsPos);
						}
						switch(event.getAction())
						{
							case MotionEvent.ACTION_DOWN:
								
								switch(notesPos)
								{
									case 0:
										break;
									case 1:
										break;
									case 2:
										break;
									case 3:
										break;
									case 4:
										chordSel.addNote(NoteName.E, NoteType.QUARTER_NOTE, 4);
										break;
									case 5:
										chordSel.addNote(NoteName.D, NoteType.QUARTER_NOTE, 4);
										break;
									case 6:
										chordSel.addNote(NoteName.C, NoteType.QUARTER_NOTE, 4);
										break;
									case 7:
										chordSel.addNote(NoteName.B, NoteType.QUARTER_NOTE, 4);
										break;
									case 8:
										chordSel.addNote(NoteName.A, NoteType.QUARTER_NOTE, 4);
										break;
									case 9:
										chordSel.addNote(NoteName.G, NoteType.QUARTER_NOTE, 4);
										break;
									case 10:
										chordSel.addNote(NoteName.F, NoteType.QUARTER_NOTE, 4);
										break;
									case 11:
										break;
									case 12:
										break;
									case 13:
										break;
									case 14:
										break;
								}
								updateMeasures(currentMeasure);
								return true;
					}
						return false;
					}
					
				
				};
				
				
				selNote.setOnTouchListener(touchListener);
				
			}
			
			
		}
		
		//Read from Chord
		for(int chords = 0; chords < measureLookUp.getSize(); chords++)
		{
			Chord chordLookUp = measureLookUp.getChord(chords);
			if(chordLookUp!= null)
			{
				for(int notes = 0; notes < chordLookUp.getSize(); notes++)
				{
					Note noteLookUp = chordLookUp.getNote(notes);
					
					switch(noteLookUp.getName())
					{
					case A:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(8);
						selNote.setImageResource(R.drawable.fillednotenote);
						break;
					case B:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(7);
						selNote.setImageResource(R.drawable.fillednoteline);
						break;
					case C:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(6);
						selNote.setImageResource(R.drawable.fillednotenote);
						break;
					case D:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(5);
						selNote.setImageResource(R.drawable.fillednoteline);
						break;
					case E:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(4);
						selNote.setImageResource(R.drawable.fillednotenote);
						break;
					case F:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(10);
						selNote.setImageResource(R.drawable.fillednotenote);
						break;
					case G:
						selChord = (LinearLayout) measureLayout.getChildAt(chords);
						selNote = (ImageView) selChord.getChildAt(9);
						selNote.setImageResource(R.drawable.fillednoteline);
						break;
					}
				}
			}
		}
	}
	
	public void playButtonTouch(View v)
	{
		player.play();
		
	}
}
