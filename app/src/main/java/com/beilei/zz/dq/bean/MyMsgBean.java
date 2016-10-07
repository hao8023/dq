package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/21 0021.
 */

public class MyMsgBean  {


    /**
     * info : 您已经购买了1张会员卡，有效期是：2016-09-20到2017-09-20
     * time : 251516515
     * result : success
     */

    public List<DataBean> data;

    public static class DataBean {
        public String info;
        public String time;
        public String result;
    }
}
