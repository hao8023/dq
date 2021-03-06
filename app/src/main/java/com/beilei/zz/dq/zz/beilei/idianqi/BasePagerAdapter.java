package com.beilei.zz.dq.zz.beilei.idianqi;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZengHao on 2016/9/16 0016.
 */

public class BasePagerAdapter  extends PagerAdapter{

    private List<View> views=new ArrayList<View>();

    public BasePagerAdapter(List<View> views){
        this.views=views;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }

}
