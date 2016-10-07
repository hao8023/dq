package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * 我的会员卡实体类
 * Created by ZengHao on 2016/9/20 0020.
 */

public class MyCardBean {

    /**
     * id : 1
     * phone : 15936243589
     * praise : 67
     * card : 1561231
     * startat : 2016.09.20
     * endat : 2017.09.20
     * time : 251516515
     * status : 1
     * state : 1
     */

    public List<InfoBean> info;


    public static class InfoBean {
        public String id;
        public String phone;
        public String praise;
        public String card;
        public String startat;
        public String endat;
        public String time;
        public String status;
        public String state;
    }
}
