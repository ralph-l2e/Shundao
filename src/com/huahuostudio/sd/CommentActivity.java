package com.huahuostudio.sd;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

public class CommentActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.actionbar_comment);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		actionBar.getCustomView().findViewById(R.id.actionbar_comment_back).setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

}
