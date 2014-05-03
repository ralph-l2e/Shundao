package com.huahuostudio.sd.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huahuostudio.sd.R;
import com.huahuostudio.sd.RouteDetailActivity;
import com.huahuostudio.sd.R.id;
import com.huahuostudio.sd.R.layout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class RefreshableListFragment extends Fragment implements
		PullToRefreshBase.OnRefreshListener2, OnItemClickListener {

	private PullToRefreshListView listView;


	public RefreshableListFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.refreshable_list, container, false);

		listView = (PullToRefreshListView) v
				.findViewById(R.id.refreshable_list);

		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		listView.setMode(PullToRefreshBase.Mode.BOTH);
		listView.setOnRefreshListener(this);
		listView.setOnItemClickListener(this);
	}
	
	public PullToRefreshListView getRefreshListView(){
		return listView;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}


}
