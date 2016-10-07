package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.ZanBean;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.beilei.zz.dq.R.id.tv_title;

/**
 * 广告详情页面
 *
 * @author zenghao
 */
public class AdActivity extends Activity implements View.OnClickListener {
    @InjectView(tv_title)
    TextView textView1;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.ad)
    ImageView ad;
    @InjectView(R.id.zan)
    ImageView zan;
    private String imgs;
    private String url;
    //金币掉落动画的主体动画
    private FlakeView flakeView;
    private String id;
    private String phone;
    private String type_id;
    private RequestQueue requestQueue;
    private Request<String> stringRequest;
    private String adtype_id;
    private String adid;
    private String adimgs;
    private ZanBean.InfoBean.AdvertBean advert;
    private ZanBean zanBean;
    private String json;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        imgs = intent.getStringExtra("imgs");//拿到一级广告列表广告详情图片
        id = intent.getStringExtra("id");//拿到一级列表广告详情ID
        phone = intent.getStringExtra("phone");
        name = intent.getStringExtra("name");

        type_id = intent.getStringExtra("type_id");//拿到点击广告类型类型Type_id
        setContentView(R.layout.activity_ad);
        flakeView = new FlakeView(this);
        initView();
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Picasso.with(this).load(imgs).into(ad);
    }

    /**
     * 初始化view
     */
    private void initView() {
        ButterKnife.inject(this);
//        setImg(url);
        textView1.setText(name);
        back.setOnClickListener(this);
        zan.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                SharedPreferences sp =getSharedPreferences("config", MODE_PRIVATE);
//                sp.edit().putString("type_id","").commit();
                finish();
                break;
            case R.id.zan:
                zan.setClickable(false);
                getimage();

                zan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zan.setClickable(true);
                    }
                },3*1000);
                break;
            default:
                break;
        }
    }

    private void getimage() {
        requestQueue = NoHttp.newRequestQueue();
        stringRequest = NoHttp.createStringRequest(Url.zan);
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//        idd = sp.getString("id", id);
//        type_idd = sp.getString("type_id", type_id);
        stringRequest.add("id", id);
        stringRequest.add("phone", phone);
        stringRequest.add("type_id", type_id);

        requestQueue.add(10010, stringRequest, new OnResponseListener<String>() {


            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                json = response.get();
                Gson gson = new Gson();
                zanBean = gson.fromJson(json, ZanBean.class);
                if (zanBean.info == null) {
                    Toast.makeText(getApplicationContext(), "您没有会员卡请购买会员卡", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (zanBean.result == 0) {
                        Toast.makeText(getApplicationContext(), "您没有会员卡请购买会员卡", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (zanBean.result == 2) {
                        Toast.makeText(getApplicationContext(), "您今日的点赞次数已用完", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (zanBean.result == 1) {
                        advert = zanBean.info.advert;
                        adimgs = advert.imgs;//点赞后广告图片地址
                        adid = advert.id;//点赞后广告图片id
                        adtype_id = advert.type_id;//点赞后图片类型ID type_id
//                editor = getSharedPreferences("config", MODE_PRIVATE).edit();
//                editor.putString("id",adid).commit();
//                editor.putString("type_id",adtype_id).commit();
                        id = adid;//当前请求到的广告id 赋值给下次请求 需要的参数广告id
                        type_id = adtype_id;//当前请求到的广告类型 赋值给下次请求 需要的参数广告类型id  type_id
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               showPopWindows(zan, "6", false);
                           }
                       });
                        Picasso.with(AdActivity.this).load(adimgs).into(ad);
                    }
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

    private PopupWindow pop;

    private PopupWindow showPopWindows(View v, String moneyStr, boolean show) {
        View view = this.getLayoutInflater().inflate(R.layout.goldcoin, null);
        TextView tvTips = (TextView) view.findViewById(R.id.tv_tip);
        TextView money = (TextView) view.findViewById(R.id.tv_money);
        tvTips.setText("点赞送您" + moneyStr + "个积分");
        money.setText("+" + moneyStr);
        final LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
        //将flakeView 添加到布局中
        container.addView(flakeView);
        //设置背景
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置同时出现在屏幕上的金币数量  建议64以内 过多会引起卡顿
        flakeView.addFlakes(16);
        /**
         * 绘制的类型
         * @see View.LAYER_TYPE_HARDWARE
         * @see View.LAYER_TYPE_SOFTWARE
         * @see View.LAYER_TYPE_NONE
         */
        flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
//        view.findViewById(R.id.btn_ikow).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.half_color));
        pop.setBackgroundDrawable(dw);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);

        /**
         * 移除动画
         */
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置2秒后
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            container.removeAllViews();
//                            if (container != null) {
//                                container.removeAllViews();
//                            }
//                            pop.dismiss();
                            pop.dismiss();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        if (!show)
            thread.start();
        MediaPlayer player = MediaPlayer.create(this, R.raw.shake);
        player.start();
        return pop;
    }


}