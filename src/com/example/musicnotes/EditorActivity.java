package com.example.musicnotes;

import MusicSheet.*;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditorActivity extends Activity{

	Sheet sheet;
	Spinner spinner;
	enum EditorVal{NOTES, RESTS, ACCIDENTALS};
	EditorVal currentVal;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
      
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.editor_layout);
    	ImageView timeSigTop = (ImageView) findViewById(R.id.timesigtop);
    	timeSigTop.setImageResource(R.drawable.halfnote);
    	
    	currentVal = EditorVal.NOTES;
    	spinner = (Spinner) findViewById(R.id.toolbarSpinner);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toolbarSpinnerArray, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(adapter);
    	
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
    	updateToolBar();
    	
    	
    	sheet = new Sheet();

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
}
