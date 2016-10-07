package com.beilei.zz.dq.zz.beilei.idianqi.fragement;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.Utils.WaitDialog;
import com.beilei.zz.dq.bean.LunBoTuBean;
import com.beilei.zz.dq.bean.LunBoTuBean.DataBean;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityBingXiang;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityDianChiLu;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityDianFanBao;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityDianNao;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityDianShi;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityKongTiao;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityShouJi;
import com.beilei.zz.dq.zz.beilei.idianqi.ActivityXiYiJi;
import com.google.gson.Gson;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ParseError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends BaseFragment implements OnClickListener {
	/**
	 * 请求的时候等待框。
	 */
	private WaitDialog mWaitDialog;
	private final class bannerHttpResponseListener implements OnResponseListener<String> {


		@Override
		public void onSucceed(int what, Response<String> response) {
			// 联网成功
			LogUtil.i("banner联网成功", "联网成功");
			LogUtil.i("banner联网成功", "联网成功结果" + response.get());
			String bannerJson = response.get();
			Gson gson = new Gson();
			LunBoTuBean lunBoTuBean = gson.fromJson(bannerJson, LunBoTuBean.class);
			
			
			
			List<DataBean> datas = lunBoTuBean.data;
			images.clear();
			for(LunBoTuBean.DataBean data :datas){
				images.add(data.imgs);

			}
			//设置轮播图数据源
			banner.setImages(images);

			
			
//			String imgs = lunBoTuBean.getData().get().getImgs();
			
			
		
			
			LogUtil.e("集合数据", images.size() + images.toString());

		}

		@Override
		public void onStart(int what) {
			// 请求开始，这里可以显示一个dialog
			if (mWaitDialog != null && !mWaitDialog.isShowing())
				mWaitDialog.show();
		}

		@Override
		public void onFinish(int what) {
			// 请求结束，这里关闭dialog
			if (mWaitDialog != null && mWaitDialog.isShowing())
				mWaitDialog.dismiss();
		}

		@Override
		public void onFailed(int what, Response<String> response) {
			//TODO 特别注意：这里可能有人会想到是不是每个地方都要这么判断，其实不用，请参考HttpResponseListener类的封装，你也可以这么封装。

			// 请求失败
			Exception exception = response.getException();
			if (exception instanceof NetworkError) {// 网络不好
				com.beilei.zz.dq.Utils.Toast.showMessage(getActivity(),"网络连接超时");

			} else if (exception instanceof TimeoutError) {// 请求超时
				com.beilei.zz.dq.Utils.Toast.showMessage(getActivity(),"请求超时");
			} else if (exception instanceof UnKnownHostError) {// 找不到服务器
				com.beilei.zz.dq.Utils.Toast.showMessage(getActivity(),"找不到服务器");
			} else if (exception instanceof URLError) {// URL是错的
				com.beilei.zz.dq.Utils.Toast.showMessage(getActivity(),"地址错误");
			} else if (exception instanceof NotFoundCacheError) {
				// 这个异常只会在仅仅查找缓存时没有找到缓存时返回
			} else if (exception instanceof ProtocolException) {
			} else if (exception instanceof ParseError) {
			} else {
			}
			Logger.e("错误：" + exception.getMessage());
		}
	}

	RequestQueue queue;

	// 图片网址集合
	List<String> images = new ArrayList<String>();

	// 以下是 爱电器页面所有控件
	Banner banner;
	private ImageView xiyiji;
	private ImageView bingxiang;
	private ImageView dianshi;
	private ImageView dianfanbao;
	private ImageView kongtiao;
	private ImageView dianchilu;
	private ImageView shouji;
	private ImageView diannao;
	@Override
	/**
	 * 初始化爱电器页面视图。
	 * 
	 * 
	 */
	public View initView() {
		queue = NoHttp.newRequestQueue();

		// 初始化View
		View view = View.inflate(context, R.layout.fragment_homepage, null);

		banner = (Banner) view.findViewById(R.id.banner);

		xiyiji = (ImageView) view.findViewById(R.id.xiyiji);
		bingxiang = (ImageView) view.findViewById(R.id.bingxiang);
		dianshi = (ImageView) view.findViewById(R.id.dianshi);
		dianfanbao = (ImageView) view.findViewById(R.id.dianfanbao);
		kongtiao = (ImageView) view.findViewById(R.id.kongtiao);
		dianchilu = (ImageView) view.findViewById(R.id.dianchilu);
		shouji = (ImageView) view.findViewById(R.id.shouji);
		diannao = (ImageView) view.findViewById(R.id.diannao);

		// 爱电器页面点击事件
		bingxiang.setOnClickListener(this);
		dianshi.setOnClickListener(this);
		xiyiji.setOnClickListener(this);
		kongtiao.setOnClickListener(this);
		dianfanbao.setOnClickListener(this);
		dianchilu.setOnClickListener(this);
		diannao.setOnClickListener(this);
		shouji.setOnClickListener(this);
		//设置小圆点样式
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
		banner.setIndicatorGravity(BannerConfig.RIGHT);
		//设置点击事件
		banner.setOnBannerClickListener(new OnBannerClickListener() {
			@Override
			public void OnBannerClick(int position) {
				Toast.makeText(getContext(),"你点击了："+position,Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}

	@Override
	/**
	 * 初始化数据
	 */
	public void initData() {


		// 联网请求数据
		Request<String> request = NoHttp.createStringRequest(Url.banner);
		request.setCacheMode(CacheMode.DEFAULT); //设置缓存
		queue.add(1, request, new bannerHttpResponseListener());
//		if (images != null && images.size() > 0) {
//			String[] aaa = new String[images.size()];
//			images.toArray(aaa);// 这是转的 类型
//			kanner.setImagesUrl(aaa);


		}
		


	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(),ActivityBingXiang.class);
		switch (v.getId()) {
		case R.id.bingxiang:
			LogUtil.i("爱电器页面点击了",""+bingxiang );
			startActivity(intent);
			
			break;
		case R.id.dianshi:
			LogUtil.i("爱电器页面点击了",""+dianshi );
			startActivity(new Intent(getActivity(),ActivityDianShi.class));
			break;
		case R.id.xiyiji:
			LogUtil.i("爱电器页面点击了",""+xiyiji );
			startActivity(new Intent(getActivity(),ActivityXiYiJi.class));

			break;
		case R.id.kongtiao:
			LogUtil.i("爱电器页面点击了",""+kongtiao );
			startActivity(new Intent(getActivity(),ActivityKongTiao .class));

			break;
		case R.id.dianfanbao:
			LogUtil.i("爱电器页面点击了",""+dianfanbao );
			startActivity(new Intent(getActivity(),ActivityDianFanBao.class));

			break;
		case R.id.dianchilu:
			LogUtil.i("爱电器页面点击了",""+dianchilu );
			startActivity(new Intent(getActivity(),ActivityDianChiLu.class));

			break;
		case R.id.diannao:
			LogUtil.i("爱电器页面点击了",""+diannao );
			startActivity(new Intent(getActivity(),ActivityDianNao.class));

			break;
		case R.id.shouji:
			LogUtil.i("爱电器页面点击了",""+shouji );
//			Intent login = new Intent(getActivity(), LoginActivity.class);
			startActivity(new Intent(getActivity(),ActivityShouJi.class));

			break;
		default:
			break;
		}
	}

}
