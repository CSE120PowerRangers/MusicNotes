package com.example.musicnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class EditorActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_layout);
        
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
