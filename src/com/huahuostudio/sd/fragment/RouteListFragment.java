package com.huahuostudio.sd.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.huahuostudio.sd.R;
import com.huahuostudio.sd.helper.JsonHelper;
import com.huahuostudio.sd.httpclient.ShundaoRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class RouteListFragment extends RefreshableListFragment {

	private final int HTTP_PARAMS_VALUE_SIZE = 20;
	private final String HTTP_PARAMS_NAME_PAGE = "start_id";
	private final String HTTP_PARAMS_NAME_SIZE = "count";

	// ��ǰ����ʱ��Ϊ���ظ���
	private boolean isLoadNext = false;

	// http�������
	private RequestParams params = new RequestParams(HTTP_PARAMS_NAME_SIZE,
			HTTP_PARAMS_VALUE_SIZE);
	// ���ݴ���handler
	private RouteListHandler handler = new RouteListHandler();

	public RouteListFragment() {
		// Required empty public constructor

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		load();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		load();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		loadNext();
	}

	/**
	 * ���ص�һҳ����
	 */
	private void load() {
		isLoadNext = false;
		params.remove(HTTP_PARAMS_NAME_PAGE);
		ShundaoRestClient.processRouteList(params, handler);
	}

	/**
	 * ������һҳ����
	 */
	private void loadNext() {
		isLoadNext = true;
		params.put(HTTP_PARAMS_NAME_PAGE,
				String.valueOf(handler.getPageOffset()));
		ShundaoRestClient.processRouteList(params, handler);
	}

	/**
	 * �첽���ݴ���Handler �첽�������ݳɹ�������ݵ�ui
	 * 
	 * @author Ralph
	 * 
	 */
	private class RouteListHandler extends JsonHttpResponseHandler {

		// ����list
		private final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		private SimpleAdapter adapter = null;

		/**
		 * ִ�гɹ��������
		 */
		@Override
		public void onSuccess(JSONObject response) {
			try {
				String status = response
						.getString(ShundaoRestClient.REPONSE_STATUS);

				// ������ر�ʶΪok���������
				if (status.equals(ShundaoRestClient.REPONSE_STATUS_OK)) {

					// ȡ�������б�
					JSONArray routes = response
							.getJSONArray(ShundaoRestClient.REPONSE_CONTENT);

					// ������Ԫ��ת��ΪHashMap
					ArrayList<HashMap<String, Object>> listNew = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < routes.length(); i++) {
						listNew.add(JsonHelper.JSONObject2HashMap(routes
								.getJSONObject(i)));
					}

					// �����ݵ�UI
					String[] keys = { "name", "from_place", "from_time",
							"to_place" };
					int[] ids = { R.id.route_card_nickname,
							R.id.route_card_start, R.id.route_card_time,
							R.id.route_card_end };

					if (adapter == null) {// ��ʼ������
						list.addAll(listNew);
						adapter = new RouteListAdapter(getActivity(), list,
								R.layout.route_card_havecar, keys, ids);

						getRefreshListView().setAdapter(adapter);
					} else {
						if (!isLoadNext) {// ˢ�²�������֮ǰ���������
							list.clear();
						}
						list.addAll(listNew);
						adapter.notifyDataSetChanged();
						getRefreshListView().onRefreshComplete();
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public int getPageOffset() {
			int lastIndex = list.size() - 1;
			int offset = Integer.valueOf(list.get(lastIndex).get("route_id")
					.toString());

			System.out.println("dssssssssssssssssssssssssssssssssss");
			System.out.println(offset);

			return offset;
		}
	}

	/**
	 * ListView adapter extends SimpleAdapter
	 * 
	 * �Ը����Ƿ����г������ı䱳��ͼƬ
	 * 
	 * @author Ralph
	 * 
	 */
	private class RouteListAdapter extends SimpleAdapter {

		public RouteListAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, ?> viewData = (HashMap<String, ?>) getItem(position);
			View mainView = super.getView(position, convertView, parent);

			if (!viewData.get("car").equals(0)) {
				Drawable findcarBg = parent.getResources().getDrawable(
						R.drawable.findcar_card_button_bg);
				mainView.findViewById(R.id.route_card_havecar)
						.setBackgroundDrawable(findcarBg);
			}

			return mainView;

		}

	}

}
