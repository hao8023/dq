package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.ResultBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;


public class ForgetPasswordsetPwd extends Activity {

	private final class OnRegListener implements OnResponseListener<String> {

		private ResultBean resultBean;
		private int result;

		@Override
		public void onStart(int what) {
			LogUtil.i("联网开始", "联网开始");
		}

		@Override
		public void onSucceed(int what, Response<String> response) {
			LogUtil.i("联网成功", "联网成功");
			String a = response.get();

			//TODO 确认成功
			Gson gson = new Gson();
			resultBean = gson.fromJson(a, ResultBean.class);
			result = resultBean.result;
			if (result==1) {
				com.beilei.zz.dq.Utils.Toast.showMessage(ForgetPasswordsetPwd.this,"密码重置成功");
			}else  if (result== 0) {
				com.beilei.zz.dq.Utils.Toast.showMessage(ForgetPasswordsetPwd.this,"密码重置失败，请联系客服");
			}

		}

		@Override
		public void onFailed(int what, Response<String> response) {
			LogUtil.i("联网失败", "联网失败");
			String a = response.get();
			LogUtil.i("联网失败", "联网失败原因" + a);
		}

		@Override
		public void onFinish(int what) {
			LogUtil.i("注册页面联网结束", "联网结束");
		}
	}

	private RequestQueue queue;
	private ImageView logo;
	private EditText repwd;
	private EditText pwd;
	private TextView btn_enter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wangjimimaquerenmima);
		queue = NoHttp.newRequestQueue();
		initView();
		initData();
	}

	private void initData() {

	}

	private void initView() {
		logo = (ImageView) findViewById(R.id.logo);
		repwd = (EditText) findViewById(R.id.repwd);
		pwd = (EditText) findViewById(R.id.pwd);
		btn_enter = (TextView) findViewById(R.id.btn_enter);
		//按钮点击事件
		btn_enter.setOnClickListener(new OnClickListener() {

			@Override
			/**
			 * 1.点击按钮时校验密码是否一致
			 * 2.
			 */
			public void onClick(View v) {
				Intent intent = getIntent();
				String phone = intent.getStringExtra("phone");
				String msg = intent.getStringExtra("msg");
				String password = pwd.getText().toString().trim();
				String repassword = repwd.getText().toString().trim();
				if (!password.equals(repassword) ) {
					Toast.makeText(ForgetPasswordsetPwd.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				}else{
					Request<String> request = NoHttp.createStringRequest(Url.forgetpsd, RequestMethod.POST);
					request.add("phone", phone);
					request.add("password", password);
					request.add("verify", msg);
					queue.add(3, request, new OnRegListener());

				}
			}
		});

	}

}
