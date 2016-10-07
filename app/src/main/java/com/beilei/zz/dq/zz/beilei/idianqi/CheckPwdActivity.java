package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
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

import static com.beilei.zz.dq.Utils.Url.msg;


/**
 * 验证码页面Activity
 *
 * @author 曾浩
 */
public class CheckPwdActivity extends Activity {


    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.ll_image)
    LinearLayout llImage;
    @InjectView(R.id.oldpwd)
    EditText oldpwd;
    @InjectView(R.id.pwd)
    EditText pwd;
    @InjectView(R.id.repwd)
    EditText repwd;
    @InjectView(R.id.btn_enter)
    TextView btnEnter;
    private RequestQueue queue;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugaimima);
        ButterKnife.inject(this);
        queue = NoHttp.newRequestQueue();
        initView();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化视图
     */
    private void initView() {


    }


    @OnClick(R.id.btn_enter)
    public void onClick() {
        String oldpassword = oldpwd.getText().toString().trim();
        String password = pwd.getText().toString().trim();
        String repassword = repwd.getText().toString().trim();
        if (!password.equals(repassword) ) {
            Toast.makeText(CheckPwdActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }else{//联网提交注册
            final Request<String> request = NoHttp.createStringRequest(Url.check, RequestMethod.POST);
            phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", "");
            request.add("phone", phone);
            request.add("password", oldpassword);
            request.add("verify", msg);
            request.add("new_password", password);
            queue.add(3, request, new OnResponseListener<String>() {

                private int result;
                private ResultBean resultBean;
                private String json;

                @Override
                public void onStart(int what) {

                }

                @Override
                public void onSucceed(int what, Response<String> response) {
                    json = response.get();
                    Gson gson = new Gson();
                    resultBean = gson.fromJson(json, ResultBean.class);
                    result = resultBean.result;
                    if (result ==1) {
                    	Toast.makeText(CheckPwdActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(CheckPwdActivity.this,HomeActivity.class));
                        finish();
                    }else {
                        Toast.makeText(CheckPwdActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
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
}
