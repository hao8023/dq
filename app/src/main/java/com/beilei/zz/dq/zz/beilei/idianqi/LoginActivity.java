package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.bean.SucessBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import static com.beilei.zz.dq.Utils.Url.login;


/**
 * 登录页面
 *
 * @author 曾浩
 */
public class LoginActivity extends Activity implements OnClickListener {

    private EditText user_phone;
    private EditText user_pwd;
    private TextView wangjimima;
    private TextView zhuce;
    private TextView btn_login;
    private ImageView logo;
    private RequestQueue queue;
    private String username;
    private String userpwd;

    //	boolean isLogin =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);

        //判断账号是否登录，如果登录直接跳转主页，如果没有登录显示登录页面
        if (isLogin) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            queue = NoHttp.newRequestQueue();

            initView();
            initData();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化页面控件
     */
    private void initView() {

        user_phone = (EditText) findViewById(R.id.user_phone);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        wangjimima = (TextView) findViewById(R.id.wangjimima);
        zhuce = (TextView) findViewById(R.id.zuche);
        btn_login = (TextView) findViewById(R.id.btn_login);
        logo = (ImageView) findViewById(R.id.logo);
        btn_login.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        wangjimima.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:// 登录按钮
                username = user_phone.getText().toString().trim();
                userpwd = user_pwd.getText().toString().trim();
                LogUtil.i("登录", "登录按钮");
                // 联网请求数据
                Request<String> request = NoHttp.createStringRequest(login, RequestMethod.POST);
                request.add("phone", username);
                request.add("password", userpwd);
                queue.add(1, request, new OnResponseListener<String>() {
                    private String login;
                    private SharedPreferences sharedPreferences;

                    @Override
                    public void onStart(int what) {
                        LogUtil.i("登录联网开始", "联网开始");
                    }

                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        LogUtil.i("登录联网成功", "联网成功");
                        String json = response.get();
                        Gson gson = new Gson();
                        SucessBean fromJson = gson.fromJson(json, SucessBean.class);
                        login = fromJson.login;
                        if (login.equals("success")) {

                            String phone = fromJson.data.phone.toString();
                            String headUrl = fromJson.data.head.toString();
                            String username = fromJson.data.username.toString();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("head", headUrl);
                            intent.putExtra("username", username);
                            intent.putExtra("phone", phone);
                            //SharedPreferences 保存数据的实现代码
                            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("phone", phone);
                            editor.putString("head",headUrl);
                            editor.putString("username", username);
                            editor.putString("password", userpwd);
                            editor.putBoolean("isLogin", true);
                            editor.commit();


                            startActivity(intent);
                            finish();
                            Log.e("22222", "ssss");
                        } else {
                            if (login.equals("1")) {
                                Toast.makeText(LoginActivity.this, "帐号或密码错误！", Toast.LENGTH_SHORT).show();
                            } else {
                                LogUtil.i("登录联网方式错误", "联网方式错误");
                            }

                        }

                    }

                    @Override
                    public void onFailed(int what, Response<String> response) {
                        LogUtil.i("登录联网失败", "联网失败");
                        String a = response.get();
                        LogUtil.i("登录联网失败", "联网失败原因" + a);
                    }

                    @Override
                    public void onFinish(int what) {
                        LogUtil.i("登录联网结束", "联网结束");
                    }
                });
                break;
            case R.id.wangjimima://忘记密码
                Intent checkpwd = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(checkpwd);



                break;
            case R.id.zuche://注册帐号
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
