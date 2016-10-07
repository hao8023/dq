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


public class RegSetPwd extends Activity {

	private final class OnRegListener implements OnResponseListener<String> {

		private ResultBean resultBean;
		private int result;

		@Override
		public void onStart(int what) {
			LogUtil.i("注册页面联网开始", "联网开始");
		}

		@Override
		public void onSucceed(int what, Response<String> response) {
			LogUtil.i("注册页面联网成功", "联网成功");
			String a = response.get();
			LogUtil.i("注册页面联网成功", "联网成功结果" + a.toString());
			//TODO 成功后跳转到个人中心
			Gson gson = new Gson();
			resultBean = gson.fromJson(a, ResultBean.class);
			result = resultBean.result;
//			Toast.makeText(RegSetPwd.this,result, Toast.LENGTH_SHORT).show();
			if (result == 5) {
				Intent intent = new Intent(RegSetPwd.this, LoginActivity.class);
                    startActivity(intent);
				finish();
			}else if (result==4) {
				Toast.makeText(RegSetPwd.this,"验证码错误", Toast.LENGTH_SHORT).show();
				finish();
			}else if (result==3) {
				Toast.makeText(RegSetPwd.this,"用户已存在", Toast.LENGTH_SHORT).show();
				finish();
			}else if (result==2) {
				Toast.makeText(RegSetPwd.this,"推荐人不存在", Toast.LENGTH_SHORT).show();
			}else{}
		}

		@Override
		public void onFailed(int what, Response<String> response) {
			LogUtil.i("注册页面联网失败", "联网失败");
			String a = response.get();
			LogUtil.i("注册页面联网失败", "联网失败原因" + a);
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
		setContentView(R.layout.activity_reg_set_pwd);
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
				String tjr = intent.getStringExtra("tjr");
				String msg = intent.getStringExtra("msg");
				String password = pwd.getText().toString().trim();
				String repassword = repwd.getText().toString().trim();
				if (!password.equals(repassword) ) {
					Toast.makeText(RegSetPwd.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				}else{//联网提交注册
					Request<String> request = NoHttp.createStringRequest(Url.reg, RequestMethod.POST);
					request.add("phone", phone);
					request.add("password", password);
					request.add("verify", msg);
					request.add("re_phone", tjr);
					queue.add(3, request, new OnRegListener());

				}
			}
		});

	}

}