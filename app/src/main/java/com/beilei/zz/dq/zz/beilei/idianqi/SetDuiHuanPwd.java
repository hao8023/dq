package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Toast;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.ResultBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ZengHao on 2016/9/24 0024.
 */

public class SetDuiHuanPwd extends Activity {
    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.ll_image)
    LinearLayout llImage;
    @InjectView(R.id.pwd)
    EditText pwd;
    @InjectView(R.id.repwd)
    EditText repwd;
    @InjectView(R.id.btn_enter)
    TextView btnEnter;
    private RequestQueue queue;
    private Request<String> request;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_duihuan_pwd);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.btn_enter)
    public void onClick() {
        phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", "");
        String password = pwd.getText().toString().trim();
        String repassword = repwd.getText().toString().trim();
        if (password.length()!=6 ||repassword.length()!=6 ) {
            Toast.showMessage(SetDuiHuanPwd.this,"兑换密码必须为6位数字");
        	return;
        }

        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.setduihuanpwd, RequestMethod.POST);
        request.add("phone", phone);
        request.add("password", password);
        request.add("repassword", repassword);
        queue.add(987, request, new OnResponseListener<String>() {

            private ResultBean resultBean;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                resultBean = new Gson().fromJson(response.get(), ResultBean.class);

                if (resultBean.result == 0) {
                    Toast.showMessage(SetDuiHuanPwd.this,"成功！");
                    finish();
                }else if (resultBean.result == 1) {
                    Toast.showMessage(SetDuiHuanPwd.this,"两次输入密码不同！");
                }else {
                    Toast.showMessage(SetDuiHuanPwd.this,"成功！");
                    finish();
                }
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
