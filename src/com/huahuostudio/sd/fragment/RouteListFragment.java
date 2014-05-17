package com.huahuostudio.sd.fragment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.huahuostudio.sd.R;
import com.huahuostudio.sd.helper.JsonHelper;
import com.huahuostudio.sd.httpclient.ShundaoRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

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

	// HTTP请求参数
	private final RequestParams params = new RequestParams(
			HTTP_PARAMS_NAME_SIZE, HTTP_PARAMS_VALUE_SIZE);
	// 数据处理handler
	private final RouteListHandler handler = new RouteListHandler();

	public RouteListFragment() {
		// Required empty public constructor

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 将intent参数绑定到接口参数
		Bundle bundle = getArguments();
		if (bundle != null) {
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				params.put(key, bundle.getString(key));
			}
		}

		load();
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		load();
	}

	// 上拉加载更多
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
					HashMap<String, Object> row = null;
					for (int i = 0; i < routes.length(); i++) {
						row = JsonHelper.JSONObject2HashMap(routes
								.getJSONObject(i));
						// row.put("avatar",
						// getAvatarURL(Integer.valueOf(row.get("user_id").toString())));
						// row.put("avatar", getAvatarURL(9));
						// System.out.println(getAvatarURL(9));
						listNew.add(row);
					}

					// 绑定数据到UI
					String[] keys = { "name", "avatar", "from_place",
							"from_time", "to_place" };
					int[] ids = { R.id.route_card_nickname,
							R.id.route_card_avatar, R.id.route_card_start,
							R.id.route_card_time, R.id.route_card_end };

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

		private final ImageLoader imageLoader = ImageLoader.getInstance();

		// 图片显示配置
		private final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).build();

		/**
		 * 构造函数
		 * 
		 * @param context
		 * @param data
		 * @param resource
		 * @param from
		 * @param to
		 */
		public RouteListAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

			// 异步加载图片配置
			ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(
					getActivity().getApplicationContext())
					.denyCacheImageMultipleSizesInMemory()
					.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
					.writeDebugLogs().build();

			imageLoader.init(imageLoaderConfig);
		}

		/**
		 * 根据用户id拼接用户头像
		 * 
		 * @param userId
		 *            用户id
		 * @return
		 */
		private URL getAvatarURL(int userId) {

			String protocol = "http";
			String host = "sd.huahuostudio.com";
			String file = "/uploads/" + userId + "/92_92.JPEG";
			URL url = null;
			try {
				url = new URL(protocol, host, file);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return url;
		}

		/**
		 * 覆盖父类方法，以实现异步加载图片
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HashMap<String, ?> viewData = (HashMap<String, ?>) getItem(position);
			View mainView = super.getView(position, convertView, parent);

			// change the card background association with data type
			if (!viewData.get("car").equals(0)) {
				Drawable findcarBg = parent.getResources().getDrawable(
						R.drawable.findcar_card_button_bg);

				mainView.findViewById(R.id.route_card_havecar)
						.setBackgroundDrawable(findcarBg);
			}

			// generate the url of user avatar
			URL avatarURL = getAvatarURL(9);
			ImageView imageView = (ImageView) mainView
					.findViewById(R.id.route_card_avatar);

			// display image
			imageLoader.displayImage(avatarURL.toString(), imageView,
					defaultOptions);

			return mainView;
		}
	}

}
