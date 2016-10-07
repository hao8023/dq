package com.beilei.zz.dq.zz.beilei.idianqi.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.Utils.GetFileSizeUtil;
import com.beilei.zz.dq.Utils.Url;
import com.beilei.zz.dq.bean.QQbean;
import com.beilei.zz.dq.zz.beilei.idianqi.HelpActivity;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MoreFragment extends BaseFragment {


    TextView tv_qq;
    @InjectView(R.id.ll_title)
    LinearLayout llTitle;
    @InjectView(R.id.ll_help)
    LinearLayout llHelp;

    @InjectView(R.id.ll_clean)
    LinearLayout llClean;
    private RequestQueue queue;
    private Request<String> request;

    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.fragment_more, null);
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        final View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
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
                TextView   tv_qq = (TextView) rootView.findViewById(R.id.tv_qq);
                tv_qq.setText(qq);
            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({ R.id.ll_help, R.id.ll_clean})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_help://帮助
                Intent help  = new Intent(getActivity(), HelpActivity.class);
                startActivity(help);
                break;
            case R.id.ll_clean://清理缓存
                clear();


                break;
        }
    }

    private void clear() {


        File cache = getActivity().getApplication().getCacheDir();
        double size = GetFileSizeUtil.getFileOrFilesSize(cache.getPath(), GetFileSizeUtil.SIZETYPE_KB);
        GetFileSizeUtil.deleteCache(cache);
        Toast.makeText(getActivity(), "成功清理了" + size + "KB缓存", Toast.LENGTH_SHORT).show();
    }

}
