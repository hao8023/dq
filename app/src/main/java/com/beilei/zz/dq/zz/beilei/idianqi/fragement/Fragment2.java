package com.beilei.zz.dq.zz.beilei.idianqi.fragement;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.LogUtil;
import com.beilei.zz.dq.Utils.NetWorkUtils;
import com.beilei.zz.dq.Utils.Toast;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.ADTypeBean;
import com.beilei.zz.dq.zz.beilei.idianqi.AdActivity;
import com.beilei.zz.dq.zz.beilei.idianqi.LoginActivity;
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

import static android.content.Context.MODE_PRIVATE;


public class Fragment2 extends Fragment {


    @InjectView(R.id.tv_title)
    TextView tvTitle;

    ListView lvAdtype;

    private Request<String> stringRequest;
    private RequestQueue requestQueue;
    private List<ADTypeBean.DataBean> data;

    private ADTypeBean.DataBean.TypeDetailBean detailBean;
    private View view;
    private String phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        phone = sp.getString("phone", "");

        //判断账号是否登录，如果登录直接跳转主页，如果没有登录显示登录页面
        if (!isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

        } else {
            if (NetWorkUtils.isNetworkConnected(getActivity())) {
                view = inflater.inflate(R.layout.fragment_faxian, null);
                ButterKnife.inject(this, view);
                lvAdtype = (ListView) view.findViewById(R.id.lv_adtype);
                initData();
                return view;
            } else {
                Toast.showMessage(getActivity(), "请检查网络");
//                view = inflater.inflate(R.layout.fragment_faxian, null);
//                ButterKnife.inject(this, view);
//                initData();
            }


        }
        return view;
    }

    private void initData() {
        requestQueue = NoHttp.newRequestQueue();
        stringRequest = NoHttp.createStringRequest(Url.advert, RequestMethod.POST);
        requestQueue.add(5, stringRequest, new OnResponseListener<String>() {


            @Override
            public void onStart(int what) {
                LogUtil.i("发现页面联网开始", "联网开始");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                LogUtil.i("发现页面联网成功", "联网成功" + response.get());
//                Toast.makeText(getActivity(), "联网成功" + response.get(), Toast.LENGTH_LONG).show();
                String json = response.get();
                Gson gson = new Gson();
                ADTypeBean fromJson = gson.fromJson(json, ADTypeBean.class);
                data = fromJson.data;

//                imgs = data.get(0).type_detail.imgs;
//                type_id = data.get(0).type_detail.type_id;
//               adid = data.get(0).type_detail.id;
                LogUtil.i("发现页面联网成功", "size+++++" + Fragment2.this.data.size() + "数据" + Fragment2.this.data.toString());
                //设置数据适配
                MyAdapter adapter = new MyAdapter();
                lvAdtype.setAdapter(adapter);
                lvAdtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    private String name;
                    private String imgs;
                    private String detairid;//一级列表广告ID
                    private String type_id;//一级列表分类id

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (data.get(position).type_detail == null) {
                            android.widget.Toast.makeText(getActivity(), "该列表没有广告呦，换一条吧~", android.widget.Toast.LENGTH_SHORT).show();
                        } else {
                            name = data.get(position).name;//一级列表分类name
                            type_id = data.get(position).id;//一级列表分类id
                            detairid = data.get(position).type_detail.id;//一级列表广告详情ID
                            imgs = data.get(position).type_detail.imgs;//一级列表点击随机广告图片地址

                            Intent intent = new Intent(getActivity(), AdActivity.class);
                            intent.putExtra("imgs", imgs);
                            intent.putExtra("name", name);
                            intent.putExtra("type_id", type_id);
                            intent.putExtra("id", detairid);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                LogUtil.i("发现页面联网失败", "联网失败" + response);
            Toast.showMessage(getActivity(),"网络连接失败，请检查网络!");
            }

            @Override
            public void onFinish(int what) {
                LogUtil.i("发现页面联网结束", "联网结束");
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
            MyHolder holder = null;
            if (convertView == null) {
                holder = new MyHolder();
                view = View.inflate(getActivity(), R.layout.item_listview_faxian, null);

                holder.tv_type = (TextView) view.findViewById(R.id.tv_type);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (MyHolder) view.getTag();
            }

            holder.tv_type.setText(data.get(position).name);
            holder.tv_type.setBackgroundColor(Color.parseColor(data.get(position).color));
            return view;
        }

        class MyHolder {
            TextView tv_type;
        }


    }
}

