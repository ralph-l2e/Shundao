package com.huahuostudio.sd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BaseActivity extends FragmentActivity {

	protected SlidingMenu slideMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化SlideMenu
		slideMenu = new SlidingMenu(this);
		slideMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// slideMenu.setFadeEnabled(true);
		slideMenu.setBehindScrollScale((float) 0.5);
		slideMenu.setMenu(R.layout.menu_left);
		slideMenu.setSecondaryMenu(R.layout.menu_right);
		slideMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		slideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slideMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slideMenu.setShadowWidthRes(R.dimen.shadow_width);
		slideMenu.setShadowDrawable(R.drawable.shadow);
		slideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

	}

	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.intent_home:
			intent = new Intent(BaseActivity.this, MainActivity.class);
			break;
		case R.id.intent_publish:
			intent = new Intent(BaseActivity.this, PublishActivity.class);
			break;
		case R.id.intent_findcar:
			intent = new Intent(BaseActivity.this, MainActivity.class);
			break;
		case R.id.intent_havecar:
			intent = new Intent(BaseActivity.this, MainActivity.class);
			break;
		case R.id.intent_findcar_mine:
			intent = new Intent(BaseActivity.this, MainActivity.class);
			break;
		case R.id.intent_havecar_mine:
			intent = new Intent(BaseActivity.this, MainActivity.class);
			break;
		case R.id.intent_user_manager:
			intent = new Intent(BaseActivity.this, AccountActivity.class);
			break;
		case R.id.intent_comment:
			intent = new Intent(BaseActivity.this, CommentActivity.class);
			break;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}

	/**
	 * 隐藏默认的设置按钮
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.action_settings).setVisible(false);
		return true;
	}

}
