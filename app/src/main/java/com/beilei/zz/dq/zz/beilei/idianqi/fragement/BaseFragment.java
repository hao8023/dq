package com.beilei.zz.dq.zz.beilei.idianqi.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Fragment 基类
 * @author zenghao
 *
 */
public abstract class BaseFragment extends Fragment {
	public Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity();
		View view = initView();
		initData();
		return view;
	}



	/**
	 * 初始化View 确定视图
	 * @return
	 */
	public abstract View initView();



	/**
	 * 初始化数据，给控件赋值
	 */
	public abstract  void initData();


}