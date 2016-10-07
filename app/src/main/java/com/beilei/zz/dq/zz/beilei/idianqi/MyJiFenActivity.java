package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.JFBean;
import com.beilei.zz.dq.bean.ResultBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by ZengHao on 2016/9/24 0024.
 */

public class MyJiFenActivity extends Activity implements View.OnClickListener{

    private String jifen;
    TextView tv_myjifen;
    private ImageView back;
    private TextView tv_title;
    private TextView tv_jilu;
    private TextView tv_shouyi;
    private TextView btn_setpwd;
    private TextView btn_login;
    private RequestQueue queue;
    private Request<String> request;
    private String phone;
    private Request<String> stringRequest;
    boolean isSetDuiHuanPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jifen);
        jifen = getIntent().getStringExtra("jifen");

        initView();
        getInfo();
        getDuiHuanPwd();
    }

    private void getDuiHuanPwd() {
        RequestQueue queue = NoHttp.newRequestQueue();
        stringRequest = NoHttp.createStringRequest(Url.isset, RequestMethod.POST);
        stringRequest.add("phone", getSharedPreferences("config", MODE_PRIVATE).getString("phone", ""));
        queue.add(555, stringRequest, new OnResponseListener<String>() {

            private ResultBean resultBean;
            private String json;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                resultBean = new Gson().fromJson(json, ResultBean.class);
                if (resultBean.result == 0) {
                     isSetDuiHuanPwd = true;
                }else {
                    isSetDuiHuanPwd = false;
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

    private void getInfo() {
        phone = getSharedPreferences("config", MODE_PRIVATE).getString("phone", "");
        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.jf, RequestMethod.POST);
        request.add("phone",phone);
        queue.add(135, request, new OnResponseListener<String>() {

            private JFBean jfBean;
            private String json;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                jfBean = new Gson().fromJson(json, JFBean.class);
                tv_shouyi.setText(jfBean.rmb+"元");
                tv_jilu.setText(jfBean.total+"次");
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });

    }



    private void initView() {
        tv_myjifen = (TextView) findViewById(R.id.tv_myjifen);
        tv_myjifen.setText(jifen);


        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        tv_jilu = (TextView) findViewById(R.id.tv_jilu);
        tv_shouyi = (TextView) findViewById(R.id.tv_shouyi);
        btn_setpwd = (TextView) findViewById(R.id.btn_setpwd);
        btn_setpwd.setOnClickListener(this);
        btn_login = (TextView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setpwd:
                startActivity(new Intent(this, SetDuiHuanPwd.class));
                break;
            case R.id.btn_login://兑换

                        if (isSetDuiHuanPwd) {
                            startActivity(new Intent(this, DuiHuanActivity.class));

                        }else {
                            startActivity(new Intent(MyJiFenActivity.this, SetDuiHuanPwd.class));
                        }

                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }

    }
}
