package com.beilei.zz.dq.bean;

/**
 * Created by ZengHao on 2016/9/22 0022.
 */

public class PriceBean {


    /**
     * id : 1
     * name : 爱电器会员卡
     * price : 501
     * status : 1
     * des : 会员卡可以点击广告，点一次可以获取积分，每张会员卡每天可以点赞100次rn
     */

    public InfoBean info;

    public static class InfoBean {
        public String id;
        public String name;
        public int price;
        public String status;
        public String des;
    }
}
