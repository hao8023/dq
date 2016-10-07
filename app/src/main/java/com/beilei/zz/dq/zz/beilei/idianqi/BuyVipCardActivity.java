package com.beilei.zz.dq.zz.beilei.idianqi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Toast;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.PriceBean;
import com.beilei.zz.dq.bean.QQbean;
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

public class BuyVipCardActivity extends AppCompatActivity {

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.imageView1)
    ImageView imageView1;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    @InjectView(R.id.tv_allprice)
    TextView tvAllprice;
    @InjectView(R.id.btn_buy)
    TextView btnBuy;
    @InjectView(R.id.cb_enter)
    CheckBox cb_enter;
    @InjectView(R.id.tv_xy)
    TextView tv_xy;
    private RequestQueue requestQueue;
    private Request<String> stringRequest;
    private Request<String> request;
    private int price;
    private PriceBean priceBean;
    private String json;
    private String number;
    private SharedPreferences sp;
    private String phone;
    private int money;
    private int intprice;
    private RequestQueue queue;
    TextView tv_kefu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getQQ();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        phone = sp.getString("phone", "");
        requestQueue = NoHttp.newRequestQueue();
        stringRequest = NoHttp.createStringRequest(Url.price, RequestMethod.POST);
        requestQueue.add(1000, stringRequest, new OnResponseListener<String>() {


            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                Gson gson = new Gson();
                priceBean = gson.fromJson(json, PriceBean.class);
                price = priceBean.info.price;
                tvPrice.setText(price + "元");
                tvAllprice.setText(price + "元");
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
        setContentView(R.layout.activity_buy_vip_card);
        ButterKnife.inject(this);
//        button.setAutoChangeNumber(true);
//        button.setNum(0);//设置默认值

//        tvAllprice.setText(1*price+"元");
//        button.setOnNumChangeListener(new CicleAddAndSubView.OnNumChangeListener() {
//
//            @Override
//            public void onNumChange(View view, int stype, int num) {
//                number = num + "";
//                money = num * price;
//                tvAllprice.setText( price + "元");
//            }
//        });
    }

    private void getQQ() {
        queue = NoHttp.newRequestQueue();
        request = NoHttp.createStringRequest(Url.qq, RequestMethod.POST);
        queue.add(525, request, new OnResponseListener<String>() {


            private String qq;
            private String json;

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                qq = new Gson().fromJson(json, QQbean.class).data.get(0).qq;
                tv_kefu = (TextView) findViewById(R.id.tv_kefu);
                tv_kefu.setText("提交订单后请联系客服QQ："+qq);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });


    }

    @OnClick({R.id.back, R.id.btn_buy,R.id.tv_xy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_xy:
                startActivity(new Intent(BuyVipCardActivity.this, OKActivity.class));
                break;

            case R.id.btn_buy:
                //TODO 提交订单每次只能提交一张 ，先弹出协议   同意协议再进行下一步
                if (cb_enter.isChecked()) {
                   request = NoHttp.createStringRequest(Url.buycard, RequestMethod.POST);
                    request.add("num", 1);
                    request.add("phone", phone);
                    request.add("price", price);
                    request.add("money", money);
                    requestQueue.add(10086, request, new OnResponseListener<String>() {
                        private int result;
                        private ResultBean resultBean;
                        @Override
                        public void onStart(int what) {
                        }
                        @Override
                        public void onSucceed(int what, Response<String> response) {
                            json = response.get();
                            Gson gson = new Gson();
                            resultBean = gson.fromJson(json, ResultBean.class);
                            result = resultBean.result;
                            switch (result) {
                                case 0://提交失败
                                    Toast.showMessage(BuyVipCardActivity.this, "提交失败");
                                    break;
                                case 1://提交成功
                                    Toast.showMessage(BuyVipCardActivity.this, "提交成功");
                                    startActivity(new Intent(BuyVipCardActivity.this, PayActivity.class));
                                    break;
                                case 2://
                                    android.widget.Toast.makeText(BuyVipCardActivity.this, "会员卡总量超过15张", android.widget.Toast.LENGTH_SHORT).show();
                                    break;
                                case 3://
                                    android.widget.Toast.makeText(BuyVipCardActivity.this, "购买会员卡数量不能为0", android.widget.Toast.LENGTH_SHORT).show();
//                                Toast.showMessage(BuyVipCardActivity.this,"购买会员卡数量不能为0");
                                    break;
                                default:
                                    break;
                            }


                        }

                        @Override
                        public void onFailed(int what, Response<String> response) {

                        }

                        @Override
                        public void onFinish(int what) {

                        }
                    });
                } else {
                    android.widget.Toast.makeText(BuyVipCardActivity.this, "请阅读协议", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }


                break;
        }
    }
}
