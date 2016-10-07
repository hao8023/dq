package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Application;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yolanda.nohttp.NoHttp;
/**
 * 初始化NoHttp
 * @author zenghao
 * 2016年9月10日 16:04:04
 */
public class MyApplication extends Application{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//初始化NoHttp
		NoHttp.init(this);
		PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
			}

			@Override
			public void onFailure(String s, String s1) {

			}
		});
	}

}
