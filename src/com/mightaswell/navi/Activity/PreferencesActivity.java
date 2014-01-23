package com.mightaswell.navi.Activity;



import com.mightaswell.navi.R;
import com.mightaswell.navi.R.layout;
import com.mightaswell.navi.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class PreferencesActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preferences, menu);
		return true;
	}
public int colorPicker(int i){
		int c = 0;
		switch(i){
		case 0:
			c = Color.TRANSPARENT;
			break;
		case 1:
			c = Color.BLUE;
			break;
		case 2:
			c = Color.MAGENTA;
			break;
		case 3:
			c = Color.GREEN;
			break;
		case 4:
			c = Color.CYAN;
			break;
		}	
		return c;
	}
}
