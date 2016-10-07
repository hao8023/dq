package com.beilei.zz.dq.zz.beilei.idianqi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.beilei.zz.dq.R;
import com.beilei.zz.dq.zz.beilei.idianqi.fragement.Fragment2;
import com.beilei.zz.dq.zz.beilei.idianqi.fragement.HomePageFragment;
import com.beilei.zz.dq.zz.beilei.idianqi.fragement.MeFragmnt;
import com.beilei.zz.dq.zz.beilei.idianqi.fragement.MoreFragment;


/**
 * 
 * @author 曾浩
 * 
 */
public class HomeActivity extends FragmentActivity {
	/**
	 * FragmentTabhost
	 */
	private FragmentTabHost mTabHost;

	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;

	/**
	 * Fragment数组界面
	 * 
	 */
	private Class mFragmentArray[] = { HomePageFragment.class, Fragment2.class,
			MeFragmnt.class, MoreFragment.class};
	/**
	 * 存放图片数组
	 * 
	 */
	private int mImageArray[] = { R.drawable.tab_home_btn,
			R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn};

	/**
	 * 选修卡文字
	 * 
	 */
	private String mTextArray[] = { "首页", "发现", "个人中心", "更多" };
	private FragmentManager fm;

	/**
	 * 
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);

		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		fm = getSupportFragmentManager();
		mTabHost.setup(this, fm, R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			// 设置Tab按钮的背景

			mTabHost.getTabWidget().getChildAt(i)
//					.setBackgroundResource(R.drawable.selector_tab_background);
					.setBackgroundColor(Color.parseColor("#EEEEEE"));

			}
	}

	/**
	 *
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		textView.setTextColor(Color.parseColor("#999999"));
		return view;
	}

}

