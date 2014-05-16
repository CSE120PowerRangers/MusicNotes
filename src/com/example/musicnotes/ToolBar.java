package com.example.musicnotes;

import java.util.ArrayList;

import Listeners.ToolButtonListener;
import MusicUtil.*;
import MusicUtil.AccidentalTool.AccidentalType;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

import com.example.musicnotes.EditorActivity.ToolFamily;

public class ToolBar {

	ArrayList<ArrayList<ImageButton>> myFamilies;
	EditorActivity myActivity;
	int size; 
	
	public ToolBar(Context context)
	{
		myActivity = (EditorActivity) context;
		myFamilies = new ArrayList<ArrayList<ImageButton>>();
		size = 0;
		addNoteFamily();
		addRestFamily();
		addAccidentalFamily();
		addPlayBackFamily();
	}
	
	public ArrayList<ImageButton> getFamily(ToolFamily myTool)
	{
		switch(myTool)
		{
		case NOTES:
			size = 5;
			return myFamilies.get(0);
		case RESTS:
			size = 4;
			return myFamilies.get(1);
		case ACCIDENTALS:
			size = 4;
			return myFamilies.get(2);
		case PLAYBACK:
			size = 4;
			return myFamilies.get(3);
			default:
				return null;
		}
	}
	
	public int getSize()
	{
		return size;
	}
	private void addNoteFamily()
	{
		ArrayList<ImageButton> myFamily = new ArrayList<ImageButton>();
		ImageButton button;
		Tool myTool;
		ToolButtonListener myListener;
		
		//Eight Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.EIGHTH_NOTE,R.drawable.eigthnote);
		button.setImageResource(R.drawable.eighthnotebutton);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Quarter Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.QUARTER_NOTE,R.drawable.quarternote);
		button.setImageResource(R.drawable.quarternotebutton);
		button.setScaleType(ScaleType.FIT_XY);

		myListener = new ToolButtonListener(myActivity, myTool, 1);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Half Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.HALF_NOTE,R.drawable.halfnotes);
		button.setImageResource(R.drawable.halfnotebutton);
		button.setScaleType(ScaleType.FIT_XY);

		myListener = new ToolButtonListener(myActivity, myTool, 2);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Whole Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.WHOLE_NOTE,R.drawable.wholenote);
		button.setImageResource(R.drawable.wholenotebutton);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 3);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Eraser 
		//Whole Note
		button = new ImageButton(myActivity);
		myTool = new EraserTool();
		button.setImageResource(R.drawable.eraserbutton);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 4);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		// Add Family
		myFamilies.add(myFamily);
	}
	
	private void addRestFamily()
	{
		ArrayList<ImageButton> myFamily = new ArrayList<ImageButton>();
		ImageButton button;
		NoteTool myTool;
		ToolButtonListener myListener;
		
		//Eight Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.EIGHTH_NOTE,R.drawable.four);
		button.setImageResource(R.drawable.four);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		
		// Add Family
		myFamilies.add(myFamily);
	}
	
	private void addAccidentalFamily()
	{
		ArrayList<ImageButton> myFamily = new ArrayList<ImageButton>();
		ImageButton button;
		Tool myTool;
		ToolButtonListener myListener;
		
		
		//Sharp Note
		button = new ImageButton(myActivity);
		myTool = new AccidentalTool(AccidentalType.SHARP);
		button.setImageResource(R.drawable.sharp);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Flat Note
		button = new ImageButton(myActivity);
		myTool = new AccidentalTool(AccidentalType.FLAT);
		button.setImageResource(R.drawable.flat);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 1);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Natural Note
		button = new ImageButton(myActivity);
		myTool = new AccidentalTool(AccidentalType.NATURAL);
		button.setImageResource(R.drawable.natural);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 2);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Dotted Note
		button = new ImageButton(myActivity);
		myTool = new AccidentalTool(AccidentalType.DOT);
		button.setImageResource(R.drawable.dot);
		button.setScaleType(ScaleType.CENTER_INSIDE);
		myListener = new ToolButtonListener(myActivity, myTool, 3);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		// Add Family
		myFamilies.add(myFamily);
	}
	
	private void addPlayBackFamily()
	{
		ArrayList<ImageButton> myFamily = new ArrayList<ImageButton>();
		ImageButton button;
		NoteTool myTool;
		ToolButtonListener myListener;
		
		//Eight Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.EIGHTH_NOTE,R.drawable.four);
		button.setImageResource(R.drawable.four);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
	
		//Whole Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.WHOLE_NOTE,R.drawable.treble);
		button.setImageResource(R.drawable.treble);
		myListener = new ToolButtonListener(myActivity, myTool, 1);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		// Add Family
		myFamilies.add(myFamily);
	}
}
