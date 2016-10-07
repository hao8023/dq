package com.beilei.zz.dq.zz.beilei.idianqi.fragement;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.HeZuoZheBean;
import com.beilei.zz.dq.bean.ResultBean;
import com.beilei.zz.dq.hzz.HeZuoZeActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.BuyVipCardActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.DuiHuanActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.LoginActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.MDMFXActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.MyJiFenActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.MyMsgActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.MyVipCardActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.PersonActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.content.Context.MODE_PRIVATE;

public class MeFragmnt extends Fragment {

    @InjectView(R.id.rl_hezuozhe)
    RelativeLayout rl_hezuozhe;
    @InjectView(R.id.person_head)
    ImageView personHead;
    @InjectView(R.id.tv_nikename)
    TextView tvNikename;
    @InjectView(R.id.show_myinfo)
    TextView showMyinfo;
    @InjectView(R.id.ll_mdm_share)
    LinearLayout llMdmShare;

    @InjectView(R.id.ll_hezuozhea)
    LinearLayout llHezuozhea;

    @InjectView(R.id.ll_hezuozheb)
    LinearLayout llHezuozheb;

    @InjectView(R.id.ll_hezuozhec)
    LinearLayout llHezuozhec;
    LinearLayout ll_jifen;
    TextView my_jifen;
    @InjectView(R.id.rl_duihuan)
    RelativeLayout rl_duihuan;
    @InjectView(R.id.ll_mycard)
    LinearLayout llMycard;
    @InjectView(R.id.ll_buycard)
    LinearLayout llBuycard;
    @InjectView(R.id.ll_share)
    LinearLayout llShare;
    @InjectView(R.id.ll_mymsg)
    LinearLayout llMymsg;
    private View view;
    private String head;
    private String phone;
    private String username;
    private OnResponseListener<String> responseListener;
    private String money;
    TextView tv_num_a;
    TextView tv_num_b;
    TextView tv_num_c;
    private Request<String> stringRequest;
    boolean isSetDuiHuanPwd;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ShareSDK.initSDK(getActivity());//sharesdk初始化    放在分享页面初始化就可以
        SharedPreferences sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        //取头像
        head = sp.getString("head", "http://123.60.63.245/Uploads/Dianqi/head/wutu.jpg");
        phone = sp.getString("phone", "");
        username = sp.getString("username", "");
        getDuiHuanPwd();

        boolean isLogin = sp.getBoolean("isLogin", false);

        //判断账号是否登录，如果登录直接跳转主页，如果没有登录显示登录页面
        if (!isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        } else {
            view = View.inflate(getActivity(), R.layout.fragment_me, null);
            tv_num_a   = (TextView) view.findViewById(R.id.tv_num_a);
            tv_num_b   = (TextView) view.findViewById(R.id.tv_num_b);
            tv_num_c   = (TextView) view.findViewById(R.id.tv_num_c);
            my_jifen   = (TextView) view.findViewById(R.id.my_jifen);
            ll_jifen   = (LinearLayout) view.findViewById(R.id.ll_jifen);

            ButterKnife.inject(this, view);
            setHead();//设置头像
            setNickName();
            RequestQueue queue = NoHttp.newRequestQueue();
            Request<String> request = NoHttp.createStringRequest(Url.parnter, RequestMethod.POST);
//            request.setCacheMode(CacheMode.DEFAULT);//缓存
            request.add("phone", phone);
            queue.add(99, request, new OnResponseListener<String>() {

                private String last_id;

                @Override
                public void onStart(int what) {

                }

                @Override
                public void onSucceed(int what, Response<String> response) {
                    String json = response.get();
                    LogUtil.e("联网", "联网成功" + json);
                    Gson gson = new Gson();
                    HeZuoZheBean fromJson = gson.fromJson(json, HeZuoZheBean.class);

                    money = fromJson.num.money;
                    last_id = fromJson.num.last_id;


                    tv_num_a.setText("共" +last_id+ "人");
                    tv_num_b.setText("共" + fromJson.num.secend_id+ "人");
                    tv_num_c.setText("共" + fromJson.num.three_id + "人");
                    my_jifen.setText("我的积分:" + money + "分");
                }

                @Override
                public void onFailed(int what, Response<String> response) {

                }

                @Override
                public void onFinish(int what) {

                }
            });

//            ButterKnife.inject(this, view);
        }



        return view;
    }



    /**
     * 设置昵称
     */
    private void setNickName() {
        String info = "admin";
        if (username.equals("")) {
            info = phone;
        } else {
            info = username;
        }
        tvNikename.setText(info);
    }

    /**
     * 设置头像
     */
    private void setHead() {
        // 自定义Transformation

        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {

                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;
                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }
                Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);
                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);
                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circle";
            }
        };
        Picasso.with(getActivity())// 指定Context
                .load(head) //指定图片URL
                .error(R.mipmap.ic_launcher)//错误图片
                .transform(transformation) // 指定图片转换器
                .into(personHead); // 指定显示图片的ImageView
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.show_myinfo, R.id.ll_mdm_share,R.id.ll_jifen ,R.id.rl_hezuozhe, R.id.rl_duihuan, R.id.ll_mycard, R.id.ll_buycard, R.id.ll_share, R.id.ll_mymsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_myinfo://跳转个人信息
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("head", head);
                intent.putExtra("username", username);
                intent.putExtra("phone", phone);

                startActivity(intent);
                break;
            case R.id.ll_mdm_share:
                startActivity(new Intent(getActivity(), MDMFXActivity.class));
                break;
            case R.id.rl_hezuozhe:
                startActivity(new Intent(getActivity(), HeZuoZeActivity.class));
                break;
            case R.id.rl_duihuan://兑换
                startActivity(new Intent(getActivity(), DuiHuanActivity.class));
                break;
            case R.id.ll_mycard://我的会员卡
                startActivity(new Intent(getActivity(), MyVipCardActivity.class));
                break;
            case R.id.ll_buycard:
                startActivity(new Intent(getActivity(), BuyVipCardActivity.class));
                break;
            case R.id.ll_share://分享
                showShare();
                break;
            case R.id.ll_jifen://积分
                Intent jifen = new Intent(getActivity(), MyJiFenActivity.class);
                jifen.putExtra("jifen",money);
                startActivity(jifen);
                break;
            case R.id.ll_mymsg://我的消息
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
                break;
        }
    }

    /**
     * 一键分享方法  调用即可
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("欢迎下载本软件");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("爱电器APP");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("http://img4.imgtn.bdimg.com/it/u=1978205210,2226637880&fm=21&gp=0.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("taobao.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(getActivity());
    }
    private void getDuiHuanPwd() {
        RequestQueue queue = NoHttp.newRequestQueue();
        stringRequest = NoHttp.createStringRequest(Url.isset, RequestMethod.POST);
        stringRequest.add("phone", getActivity().getSharedPreferences("config", MODE_PRIVATE).getString("phone", ""));
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
}