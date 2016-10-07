package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
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


/**
 * 修改昵称Activity
 *
 * @author 曾浩
 */
public class CheckNikeActivity extends Activity {


    @InjectView(R.id.btn_enter)
    TextView btnEnter;
    @InjectView(R.id.logo)
    ImageView logo;
    @InjectView(R.id.ll_image)
    LinearLayout llImage;
    @InjectView(R.id.nicheng)
    EditText nicheng;
    private RequestQueue queue;
    private String phone;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiugainicheng);
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
        username = nicheng.getText().toString().trim();

        Request<String> request = NoHttp.createStringRequest(Url.username, RequestMethod.POST);
        phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", "");
        request.add("phone", phone);

        request.add("username", username);
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
                if (result == 1) {
                    Toast.makeText(CheckNikeActivity.this, "修改昵称成功", Toast.LENGTH_SHORT).show();
                    Intent ok = new Intent(CheckNikeActivity.this, PersonActivity.class);
                    ok.putExtra("username", username);
                    getSharedPreferences("config", MODE_PRIVATE).edit().putString("username", username).commit();
                    startActivity(ok);
                    finish();
                } else {
                    Toast.makeText(CheckNikeActivity.this, "修改昵称失败", Toast.LENGTH_SHORT).show();
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

