package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.MyCardBean;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.beilei.zz.dq.R.id.lv_mycard;
import static com.beilei.zz.dq.R.id.tv_nocard;

/**
 * Created by ZengHao on 2016/9/20 0020.
 */

public class MyVipCardActivity extends Activity implements View.OnClickListener {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(lv_mycard)
    ListView lvMycard;
    @InjectView(tv_nocard)
    TextView tvNocard;
    private RequestQueue queue;
    private Request<String> request;
    private MyCardBean cardBean;
    private List<MyCardBean.InfoBean> info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycard);
        queue = NoHttp.newRequestQueue();
        initView();
        initData();
    }


    private void initData() {
        request = NoHttp.createStringRequest(Url.card, RequestMethod.POST);
        request.add("phone", getSharedPreferences("config", MODE_PRIVATE).getString("phone", "8888"));
        queue.add(7, request, new OnResponseListener<String>() {




            @Override
            public void onStart(int what) {
                LogUtil.e("联网开始", "联网开始");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                LogUtil.e("联网成功", "联网成功" + response.get());
                String json = response.get();
                cardBean = new Gson().fromJson(json, MyCardBean.class);
                info = cardBean.info;
                if (info == null) {
                    Toast.makeText(MyVipCardActivity.this, "没有会员卡", Toast.LENGTH_SHORT).show();
                    tvNocard.setText("对不起您没有会员卡");
                } else {

                    lvMycard.setAdapter(new MyAdapter());
                }
            }
            @Override
            public void onFailed(int what, Response<String> response) {
                LogUtil.e("联网失败", "联网失败" + response.get());

            }

            @Override
            public void onFinish(int what) {
                LogUtil.e("联网结束", "联网结束");

            }
        });
    }

    private void initView() {
        ButterKnife.inject(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return info.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            MyHolder holder = null;
            if (convertView == null) {
                holder = new MyHolder();
                view = View.inflate(MyVipCardActivity.this, R.layout.item_mycard, null);

                holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (MyHolder) view.getTag();
            }

            holder.tv_time.setText("有效期："+info.get(position).startat+"-"+info.get(position).endat);
            return view;
        }
    }
    class MyHolder {
        TextView tv_time;
    }
}
