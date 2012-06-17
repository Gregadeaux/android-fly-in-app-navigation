package com.deaux.sample;

import com.deaux.fan.FanView;
import com.gregadeaux.fan.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SampleActivity extends Activity {
    /** Called when the activity is first created. */
	private FanView fan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        fan = (FanView) findViewById(R.id.fan_view);
        fan.setViews(R.layout.main, R.layout.fan);
    }
    
    public void unclick(View v) {
    	System.out.println("CLOSE");
    	fan.showMenu();
    }
    
    public void click(View v) {
    	System.out.println("OPEN");
    	fan.showMenu();
    }
    
}