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
		// �½�һ�������࣬���ڴ�Ŷ�������

		HashMap<String, Object> map = null;
		for (int i = 1; i <= 40; i++) {
			map = new HashMap<String, Object>();
			map.put("userName", "�û�" + i);
			map.put("routeTime", "04-26 20:04");
			map.put("routeContent", "�۸��ܱ���Щ��10Ԫ�в��У����еĻ�������ߣ�׼ʱ׼��~");
			list.add(map);
		}
		return list;
	}

}
