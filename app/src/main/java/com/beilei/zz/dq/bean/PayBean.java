package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/27 0027.
 */

public class PayBean {

    /**
     * id : 1
     * code_url : http://192.168.2.100/Uploads/Dianqi/code/2016-09-27/57e9ea9bb0993.jpg
     */

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String code_url;
    }
}
