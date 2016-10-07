package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.regex.Pattern;


/**
 * 验证码页面Activity
 *
 * @author 曾浩
 */
public class RegisterActivity extends Activity implements OnClickListener {

    private final class OnGetMsg implements OnResponseListener<String> {
        @Override
        public void onStart(int what) {
            LogUtil.i("验证码联网开始", "联网开始");
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
//            LogUtil.i("验证码联网成功", "联网成功");
//            String json = response.get();
//            LogUtil.i("验证码联网成功", "联网成功结果" + json.toString());
//            Gson gson = new Gson();
//            RegisterBean bean = gson.fromJson(json, RegisterBean.class);
//            String result = bean.result;

            Toast.makeText(RegisterActivity.this, "sucess", Toast.LENGTH_SHORT).show();

//                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
//                    //注册成功跳转到登录页面
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();


        }

        @Override
        public void onFailed(int what, Response<String> response) {
            LogUtil.i("验证码联网失败", "联网失败");
            String a = response.get();
            LogUtil.i("验证码联网失败", "联网失败原因" + a);


        }

        @Override
        public void onFinish(int what) {
            LogUtil.i("验证码联网结束", "联网结束");
        }
    }

    private ImageView logo;
    private EditText et_phone;
    private EditText et_tjr;
    private Button get_msg;
    private EditText et_msg;
    private TextView btn_next;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

        logo = (ImageView) findViewById(R.id.logo);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_tjr = (EditText) findViewById(R.id.et_tjr);
        get_msg = (Button) findViewById(R.id.btn_getmsg);
        et_msg = (EditText) findViewById(R.id.et_msg);
        btn_next = (TextView) findViewById(R.id.btn_next);
        get_msg.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getmsg: // 获取验证码
                String phone = et_phone.getText().toString().trim();
                if (isMobile(phone)) {
                    showResidueSeconds();
                    LogUtil.i("验证码手机号", "手机号是" + phone);
                    // 联网请求
                    Request<String> request = NoHttp.createStringRequest(Url.msg, RequestMethod.POST);
                    request.add("phone", phone);// 传手机号给服务器，获取验证码

                    queue.add(2, request, new OnGetMsg());
                } else {
                    Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.btn_next:
                String num = et_phone.getText().toString().trim();
                String msg = et_msg.getText().toString().trim();
                String tuijianren = et_tjr.getText().toString().trim(); //TODO 推荐人 传给服务器
                if (TextUtils.isEmpty(msg) || msg.length() == 0) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                } else if (!isMobile(num)) {
                    Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                }else {


                    Intent intent = new Intent(RegisterActivity.this, RegSetPwd.class);
                    intent.putExtra("phone", num);
                    intent.putExtra("msg", msg);
                    if (!TextUtils.isEmpty(tuijianren)) {
                        if (isMobile(tuijianren)) {
                            intent.putExtra("tjr", tuijianren);
                            startActivity(intent);
                        } else {

                            Toast.makeText(this, "请输入正确的推荐人手机号", Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String mobile) {
        boolean flag = false;
        if (mobile.length() == 0) {
            return false;
        }
        String[] mobiles = mobile.split(",");
        int len = mobiles.length;
        if (len == 1) {
            return Pattern.matches("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$", mobile);
        } else {
            for (int i = 0; i < len; i++) {
                if (isMobile(mobiles[i])) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    private void showResidueSeconds() {
        //显示倒计时按钮
        new CountDownTimer(60 * 1000, 1000) {

            @Override
            public void onTick(long lastTime) {
                //倒计时执行的方法
                get_msg.setClickable(false);
                get_msg.setFocusable(false);
                get_msg.setText(lastTime / 1000 + "秒后重发");
                get_msg.setTextColor(getResources().getColor(R.color.text_hint_color));
                get_msg.setBackgroundResource(R.drawable.btn_bg_hint);
                LogUtil.d("lasttime", "剩余时间:" + lastTime / 1000);
            }

            @Override
            public void onFinish() {
                get_msg.setClickable(true);
                get_msg.setFocusable(true);
                get_msg.setText("重新获取");
            }
        }.start();
    }

}
