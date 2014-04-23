package com.example.musicnotes;

import java.util.ArrayList;

import Listeners.ToolButtonListener;
import MusicUtil.NoteTool;
import MusicUtil.NoteType;
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
			size = 4;
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
		NoteTool myTool;
		ToolButtonListener myListener;
		
		//Eight Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.EIGHTH_NOTE,R.drawable.eigthnote);
		button.setImageResource(R.drawable.eigthnote);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Quarter Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.QUARTER_NOTE,R.drawable.quarternote);
		button.setImageResource(R.drawable.quarternote);
		button.setScaleType(ScaleType.FIT_XY);

		myListener = new ToolButtonListener(myActivity, myTool, 1);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Half Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.HALF_NOTE,R.drawable.halfnotes);
		button.setImageResource(R.drawable.halfnotes);
		button.setScaleType(ScaleType.FIT_XY);

		myListener = new ToolButtonListener(myActivity, myTool, 2);
		button.setOnClickListener(myListener);
		button.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT));
		myFamily.add(button);
		
		//Whole Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.WHOLE_NOTE,R.drawable.wholenote);
		button.setImageResource(R.drawable.wholenote);
		button.setScaleType(ScaleType.FIT_XY);
		myListener = new ToolButtonListener(myActivity, myTool, 3);
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
		NoteTool myTool;
		ToolButtonListener myListener;
		
		
		//Whole Note
		button = new ImageButton(myActivity);
		myTool = new NoteTool(NoteType.WHOLE_NOTE,R.drawable.treble);
		button.setImageResource(R.drawable.treble);
		myListener = new ToolButtonListener(myActivity, myTool, 0);
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
