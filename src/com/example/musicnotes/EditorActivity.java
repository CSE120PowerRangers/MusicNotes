package com.example.musicnotes;

import MusicSheet.Sheet;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class EditorActivity extends Activity{

	Sheet sheet;
	
=======
import MusicSheet.*;

public class EditorActivity extends Activity{

>>>>>>> 486345c8019b7f122da044398b7f91ece75688e8
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.editor_layout);
    	ImageView timeSigTop = (ImageView) findViewById(R.id.timesigtop);
    	timeSigTop.setImageResource(R.drawable.halfnote);
    	sheet = new Sheet();
    	//Add Buttons to notesToolBar
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
    	ImageButton[] toolbarButtons = new ImageButton[6];
    	for(int i = 0; i < toolbarButtons.length; i++)
    	{
    		toolbarButtons[i] = new ImageButton(this);
    		toolbarButtons[i].setImageResource(R.drawable.four);
    		toolbarButtons[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    		toolbar.addView(toolbarButtons[i]);
    		setContentView(toolbar);
    	}

    	
    }
}
