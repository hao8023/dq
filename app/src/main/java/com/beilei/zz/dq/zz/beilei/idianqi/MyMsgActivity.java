package com.beilei.zz.dq.zz.beilei.idianqi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.MyMsgBean;
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
import butterknife.OnClick;

/**
 * Created by ZengHao on 2016/9/21 0021.
 */

public class MyMsgActivity extends Activity {

    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.lv_mymsg)
    ListView lvMymsg;
    @InjectView(R.id.tv_nomsg)
    TextView tvNomsg;
    private RequestQueue queue;
    private Request<String> request;
    private List<MyMsgBean.DataBean> data;
    private MyMsgBean msgBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymsg);
        ButterKnife.inject(this);
        queue = NoHttp.newRequestQueue();
        initView();
        initData();
    }

    private void initData() {
        request = NoHttp.createStringRequest(Url.mymsg, RequestMethod.POST);
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
                msgBean = new Gson().fromJson(json, MyMsgBean.class);
                data = msgBean.data;
                if (data == null) {
                    Toast.makeText(MyMsgActivity.this, "没有消息", Toast.LENGTH_SHORT).show();
                    tvNomsg.setText("没有任何消息");
                } else {
                    lvMymsg.setAdapter(new MyAdapter());
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
//        ButterKnife.inject(this);
    }



    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return data.size();
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
            Holder holder = null;
            if (convertView == null) {

                view = View.inflate(MyMsgActivity.this, R.layout.item_liseview_mymsg, null);
                holder = new Holder();
                holder.time = (TextView) view.findViewById(R.id.time);
                holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (Holder) view.getTag();
            }
            holder.tv_desc.setText(data.get(position).info+"");
            holder.time.setText(data.get(position).time);
            return view;
        }
    }

    class Holder {
        TextView tv_desc, time;
    }
}
