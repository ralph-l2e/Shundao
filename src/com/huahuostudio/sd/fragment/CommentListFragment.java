package com.huahuostudio.sd.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.huahuostudio.sd.R;

public class CommentListFragment extends RefreshableListFragment {

	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

	public CommentListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String[] keys = { "userName", "routeTime", "routeContent" };
		int[] ids = { R.id.comment_card_nickname, R.id.comment_card_time,
				R.id.comment_card_content };
		final SimpleAdapter adapter = new SimpleAdapter(getActivity(),
				getData(), R.layout.comment_card, keys, ids);
		getRefreshListView().setAdapter(adapter);

	}

	private List<HashMap<String, Object>> getData() {
		// 新建一个集合类，用于存放多条数据

		HashMap<String, Object> map = null;
		for (int i = 1; i <= 40; i++) {
			map = new HashMap<String, Object>();
			map.put("userName", "用户" + i);
			map.put("routeTime", "04-26 20:04");
			map.put("routeContent", "价格能便宜些吗？10元行不行，能行的话明天就走！准时准点~");
			list.add(map);
		}
		return list;
	}

}
