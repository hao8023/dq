package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/27 0027.
 */

public class BankCardBean {

    /**
     * bank : 整死你
     * bank_num :
     * name : 保密
     * result : 0
     */

    public List<DataBean> data;

    public static class DataBean {
        public String bank;
        public String bank_num;
        public String name;
        public String result;
    }
}
