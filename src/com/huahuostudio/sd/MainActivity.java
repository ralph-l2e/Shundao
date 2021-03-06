package com.huahuostudio.sd;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.huahuostudio.sd.fragment.RouteListFragment;

public class MainActivity extends BaseActivity {
	
	public static final String ROUTE_FILTER_NAME_ROLE = "role";
	public static final String ROUTE_FILTER_VALUE_PASSENGER = "passenger";
	public static final String ROUTE_FILTER_VALUE_DRIVER = "driver";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ��ʼ��ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		RouteListFragment fragment = new RouteListFragment();
		fragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(R.id.route_fragment_container, fragment).commit();

		// ��ఴť�������˵�
		actionBar.getCustomView().findViewById(R.id.left_area)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						slideMenu.toggle();
					}

				});

		// �Ҳఴť�����Ҳ�˵�
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
