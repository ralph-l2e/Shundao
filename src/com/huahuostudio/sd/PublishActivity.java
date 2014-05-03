package com.huahuostudio.sd;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class PublishActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.actionbar_publish);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		actionBar.getCustomView().findViewById(R.id.actionbar_publish_back).setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publish, menu);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

}
