package com.mightaswell.navi.Activity;

import com.mightaswell.navi.R;
import com.mightaswell.navi.R.id;
import com.mightaswell.navi.R.layout;
import com.mightaswell.navi.R.menu;
import com.mightaswell.navi.Service.NaviFlight;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	final String act = "ACTIVITY";
	final String servStart = "STARTSERVICE";
	final String servStop = "STOPSERVICE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void setButtons(){
		ImageButton naviStart = createButtons(findViewById(R.id.naviOn), new Intent(MainActivity.this, NaviFlight.class), servStart);
		ImageButton naviStop = createButtons(findViewById(R.id.naviOff), new Intent(MainActivity.this, NaviFlight.class), servStop);
		ImageButton help = createButtons(findViewById(R.id.helpButton), new Intent(MainActivity.this, HelpActivity.class), act);
		ImageButton settings = createButtons(findViewById(R.id.settingsButton), new Intent(MainActivity.this, PreferencesActivity.class), act);
		ImageButton about = createButtons(findViewById(R.id.infoButton), new Intent(MainActivity.this, AboutActivity.class), act);
	}
	public ImageButton createButtons(View v, final Intent i, final String type){
		ImageButton b = (ImageButton)v;
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(type.equals("ACTIVITY")){
					startActivity(i);
				}else if(type.equals("STARTSERVICE")){
					
					startService(i);
				}else if(type.equals("STOPSERVICE")){
					stopService(i);
				}
						
			}
		});
		return b;
	}
}
