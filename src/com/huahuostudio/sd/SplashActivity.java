package com.huahuostudio.sd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashActivity extends Activity {

	private final int SPLASH_LIFETIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);	

		// TODO: 改为预加载数据后启动MainActivity
		new Handler().postDelayed(new Runnable(){
			
			@Override
			public void run(){
				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
			
		}, SPLASH_LIFETIME);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
}
