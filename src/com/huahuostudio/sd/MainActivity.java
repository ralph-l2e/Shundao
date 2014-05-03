package com.huahuostudio.sd;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ³õÊ¼»¯ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);

		// ×ó²à°´Å¥¿Õ×Å×ó²à²Ëµ¥
		actionBar.getCustomView().findViewById(R.id.left_area)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						slideMenu.toggle();
					}

				});

		// ÓÒ²à°´Å¥¿ØÖÆÓÒ²à²Ëµ¥
		actionBar.getCustomView().findViewById(R.id.right_area)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						slideMenu.showSecondaryMenu();
						// Intent intent = new Intent(MainActivity.this,
						// SigninActivity.class);
						// // startActivity(intent);
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

}
