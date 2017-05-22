package com.example.weatherapps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends Activity {
TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Typeface tf=Typeface.createFromAsset(getAssets(), "sdfdsg");
		
	TimerTask tt=new TimerTask() {
		
		@Override
		public void run() {
		Intent i=new Intent(Splash.this,MainActivity.class);
		startActivity(i);
		finish();
		}
	};
	Timer time=new Timer();
	time.schedule(tt, 5000);
	}

	

}
