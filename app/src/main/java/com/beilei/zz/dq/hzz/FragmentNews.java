package com.beilei.zz.dq.hzz;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.HeZuoZheBean;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.content.Context.MODE_PRIVATE;

public class FragmentNews extends Fragment {
	@InjectView(R.id.gv_hzz)
	GridView gvHzz;
	private RequestQueue queue;
	private Request<String> request;
	private static HeZuoZheBean heZuoZheBean;
	private List<HeZuoZheBean.DetailBean.SecendIdBean> secend_id;

	private String json;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmentnews, container, false);

		queue = NoHttp.newRequestQueue();
		request = NoHttp.createStringRequest(Url.hzz, RequestMethod.POST);
		request.add("phone", getActivity().getSharedPreferences("config", MODE_PRIVATE).getString("phone", ""));
		queue.add(1235, request, new OnResponseListener<String>() {




			@Override
			public void onStart(int what) {

			}

			@Override
			public void onSucceed(int what, Response<String> response) {
				json = response.get();
				heZuoZheBean = new Gson().fromJson(json, HeZuoZheBean.class);
				secend_id = heZuoZheBean.detail.secend_id;
				gvHzz.setAdapter(new MyAdapter());
			}

			@Override
			public void onFailed(int what, Response<String> response) {

			}

			@Override
			public void onFinish(int what) {

			}
		});
		ButterKnife.inject(this, view);

		return view;
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	class MyAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			return secend_id.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			//如果缓存convertView为空，则需要创建View
			if (convertView == null) {
				holder = new Holder();
				//根据自定义的Item布局加载布局
				convertView = View.inflate(getActivity(), R.layout.item_grildview, null);
				holder.partner_head = (ImageView) convertView.findViewById(R.id.partner_head);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				//将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			Picasso.with(getActivity()).load(secend_id.get(position).head).transform(transformation).into(holder.partner_head);
			holder.tv_name.setText(secend_id.get(position).username);
			return convertView;
		}


	}

	class Holder {
		ImageView partner_head;
		TextView tv_name;

	}


	Transformation transformation = new Transformation() {
		@Override
		public Bitmap transform(Bitmap source) {

			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;
			Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source) {
				source.recycle();
			}
			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap,
					BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			paint.setShader(shader);
			paint.setAntiAlias(true);
			float r = size / 2f;
			canvas.drawCircle(r, r, r, paint);
			squaredBitmap.recycle();
			return bitmap;
		}

		@Override
		public String key() {
			return "circle";
		}
	};
}
