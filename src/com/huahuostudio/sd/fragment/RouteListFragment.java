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

	// 当前操作时否为加载更多
	private boolean isLoadNext = false;

	// http请求参数
	private RequestParams params = new RequestParams(HTTP_PARAMS_NAME_SIZE,
			HTTP_PARAMS_VALUE_SIZE);
	// 数据处理handler
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
	 * 加载第一页数据
	 */
	private void load() {
		isLoadNext = false;
		params.remove(HTTP_PARAMS_NAME_PAGE);
		ShundaoRestClient.processRouteList(params, handler);
	}

	/**
	 * 加载下一页数据
	 */
	private void loadNext() {
		isLoadNext = true;
		params.put(HTTP_PARAMS_NAME_PAGE,
				String.valueOf(handler.getPageOffset()));
		ShundaoRestClient.processRouteList(params, handler);
	}

	/**
	 * 异步数据处理Handler 异步请求数据成功后绑定数据到ui
	 * 
	 * @author Ralph
	 * 
	 */
	private class RouteListHandler extends JsonHttpResponseHandler {

		// 数据list
		private final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		private SimpleAdapter adapter = null;

		/**
		 * 执行成功后绑定数据
		 */
		@Override
		public void onSuccess(JSONObject response) {
			try {
				String status = response
						.getString(ShundaoRestClient.REPONSE_STATUS);

				// 如果返回标识为ok，则绑定数据
				if (status.equals(ShundaoRestClient.REPONSE_STATUS_OK)) {

					// 取得数据列表
					JSONArray routes = response
							.getJSONArray(ShundaoRestClient.REPONSE_CONTENT);

					// 将数据元素转化为HashMap
					ArrayList<HashMap<String, Object>> listNew = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < routes.length(); i++) {
						listNew.add(JsonHelper.JSONObject2HashMap(routes
								.getJSONObject(i)));
					}

					// 绑定数据到UI
					String[] keys = { "name", "from_place", "from_time",
							"to_place" };
					int[] ids = { R.id.route_card_nickname,
							R.id.route_card_start, R.id.route_card_time,
							R.id.route_card_end };

					if (adapter == null) {// 初始化数据
						list.addAll(listNew);
						adapter = new RouteListAdapter(getActivity(), list,
								R.layout.route_card_havecar, keys, ids);

						getRefreshListView().setAdapter(adapter);
					} else {
						if (!isLoadNext) {// 刷新操作，将之前的数据清空
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
	 * 以根据是否“我有车”来改变背景图片
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
