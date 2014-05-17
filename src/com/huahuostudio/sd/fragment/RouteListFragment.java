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

	// ��ǰ����ʱ��Ϊ���ظ���
	private boolean isLoadNext = false;

	// HTTP�������
	private final RequestParams params = new RequestParams(
			HTTP_PARAMS_NAME_SIZE, HTTP_PARAMS_VALUE_SIZE);
	// ���ݴ���handler
	private final RouteListHandler handler = new RouteListHandler();

	public RouteListFragment() {
		// Required empty public constructor

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// ��intent�����󶨵��ӿڲ���
		Bundle bundle = getArguments();
		if (bundle != null) {
			Set<String> keySet = bundle.keySet();
			for (String key : keySet) {
				params.put(key, bundle.getString(key));
			}
		}

		load();
	}

	// ����ˢ��
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		load();
	}

	// �������ظ���
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

					// �����ݵ�UI
					String[] keys = { "name", "avatar", "from_place",
							"from_time", "to_place" };
					int[] ids = { R.id.route_card_nickname,
							R.id.route_card_avatar, R.id.route_card_start,
							R.id.route_card_time, R.id.route_card_end };

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

		private final ImageLoader imageLoader = ImageLoader.getInstance();

		// ͼƬ��ʾ����
		private final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).build();

		/**
		 * ���캯��
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

			// �첽����ͼƬ����
			ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(
					getActivity().getApplicationContext())
					.denyCacheImageMultipleSizesInMemory()
					.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
					.writeDebugLogs().build();

			imageLoader.init(imageLoaderConfig);
		}

		/**
		 * �����û�idƴ���û�ͷ��
		 * 
		 * @param userId
		 *            �û�id
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
		 * ���Ǹ��෽������ʵ���첽����ͼƬ
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
