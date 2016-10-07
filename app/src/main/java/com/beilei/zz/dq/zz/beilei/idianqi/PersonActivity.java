package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PersonActivity extends Activity implements View.OnClickListener {
	private static final int REQUEST_CODE = 0;
	ImageView back;
	RelativeLayout rl_nikename;
	TextView btn_out;
	LinearLayout ll_xiugai;
	ImageView person_head;
	 String username;
	 String head;
	 String phone;
	 Intent intent;
	TextView tv_nikename;
	Transformation transformation;
	private OnResponseListener<String> stringOnResponseListener;
	private FragmentManager fm;
	private RequestQueue queue;
	private FileBinary binary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_person);

		initView();
		initData();

	}

	private void initView() {

		back = (ImageView) findViewById(R.id.back);
		rl_nikename = (RelativeLayout) findViewById(R.id.rl_nikename);
		btn_out = (TextView) findViewById(R.id.btn_out);
		ll_xiugai = (LinearLayout) findViewById(R.id.ll_xiugai);
		person_head = (ImageView) findViewById(R.id.person_head);
		tv_nikename = (TextView) findViewById(R.id.tv_nikename);
		intent = getIntent();
		phone = intent.getStringExtra("phone");
		head = intent.getStringExtra("head");
		username = intent.getStringExtra("username");
		LogUtil.e("用户名", username);
		LogUtil.e("头像", head);
		LogUtil.e("手机号", phone);


		//设置点击事件
		rl_nikename.setOnClickListener(this);
		btn_out.setOnClickListener(this); //登出
		person_head.setOnClickListener(this);//修改头像
		ll_xiugai.setOnClickListener(this);//修改昵称
		back.setOnClickListener(this);
	}

	private void initData() {

		//设置昵称
		String info = "admin";
		if (username.equals("")) {
			info = phone;
		} else {
			info = username;
		}

		tv_nikename.setText(info);

		//设置头像
		// 自定义Transformation
//		head = getSharedPreferences("config", MODE_PRIVATE).getString("head", "http://192.168.199.185:8080/Uploads/Dianqi/head/wutu.jpg");
		transformation = new Transformation() {
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
		Picasso.with(this)// 指定Context
				.load(head) //指定图片URL
//				.error(R.mipmap.ic_launcher)//错误图片
				.transform(transformation) // 指定图片转换器
				.into(person_head); // 指定显示图片的ImageView

	}


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.person_head://修改头像
				modifyHead();
				break;

			case R.id.ll_xiugai://修改密码
				Intent intent = new Intent(PersonActivity.this,CheckPwdActivity.class);
				//输入旧密码 新密码  修改成功然后跳转到 首页
				startActivity(intent);
				break;
			case R.id.back://返回按钮
//				startActivity(new Intent(PersonActivity.this,HomeActivity.class));
				finish();
				break;
			case R.id.rl_nikename://修改昵称按钮
				Intent name = new Intent(PersonActivity.this,CheckNikeActivity.class);
				startActivityForResult(name,8821778);

				break;
			case R.id.btn_out://登出按钮
				SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
				sharedPreferences.edit().putBoolean("isLogin",false).commit();
				Intent out = new Intent(PersonActivity.this,HomeActivity.class);
				startActivity(out);

//					.setBackgroundResource(R.drawable.selector_tab_background);

				finish();
				break;
		}


	}

	/**
	 * 修改头像
	 */
	private void modifyHead() {
		ArrayList<String> path = new ArrayList<>();
		ImageConfig imageConfig
				= new ImageConfig.Builder(
				// GlideLoader 可用自己用的缓存库
				new GlideLoader())
				// 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
				.steepToolBarColor(Color.parseColor("#dd3C44"))
				// 标题的背景颜色 （默认黑色）
				.titleBgColor(Color.parseColor("#dd3C44"))
				// 提交按钮字体的颜色  （默认白色）
				.titleSubmitTextColor(getResources().getColor(R.color.white))
				// 标题颜色 （默认白色）
				.titleTextColor(getResources().getColor(R.color.white))
				// 开启多选   （默认为多选）  (单选 为 singleSelect)
				.singleSelect()
				.crop()
				// 多选时的最大数量   （默认 9 张）
				.mutiSelectMaxSize(9)
				// 已选择的图片路径
				.pathList(path)
				// 拍照后存放的图片路径（默认 /temp/picture）
				.filePath("/ImageSelector/Pictures")
				// 开启拍照功能 （默认开启）
				.showCamera()
				.requestCode(REQUEST_CODE)
				.build();


		ImageSelector.open(PersonActivity.this, imageConfig);   // 开启图片选择器

	}

	//创建 图片加载器
	public class GlideLoader implements com.yancy.imageselector.ImageLoader {

		@Override
		public void displayImage(Context context, String path, ImageView imageView) {
			Glide.with(context)
					.load(path)
					.placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
					.centerCrop()
					.into(imageView);
		}

	}

	/**
	 * 在onactivityResult 中获取结果
	 * @param requestCode
	 * @param resultCode
     * @param data
     */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK && data != null) {

			// Get Image Path List
			List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

			for (String path : pathList) {
				Log.e("ImagePathList", path);

			}
			head = "file://"+(pathList.get(0));
			SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
			sp.edit().putString("head",head).commit();
			Picasso.with(PersonActivity.this).load(head).transform(transformation).into(person_head);
			//上传头像到服务器
			queue = NoHttp.newRequestQueue();
			Request<String> request = NoHttp.createStringRequest(Url.head, RequestMethod.POST);
			File file = new File(pathList.get(0));
			request.add("phone",phone);
//			binary = new FileBinary(file);
//			request.add("file", new BitmapStreamBinary(Bitmap));

			request.add("file",file);
			queue.add(123, request, new OnResponseListener<String>() {

				private String json;

				@Override
				public void onStart(int what) {
					LogUtil.e("sss",what+"");
				}

				@Override
				public void onSucceed(int what, Response<String> response) {
					json = response.get();

				}

				@Override
				public void onFailed(int what, Response<String> response) {

				}

				@Override
				public void onFinish(int what) {

				}
			});
		}

	}


}