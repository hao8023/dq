package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by wangdh on 2016/8/15.
 * 新闻中心对应的Javabean
 * 字段名 必须 与json的key一致
 */
public class LunBoTuBean {

    /**
     * id : 14
     * imgs : http://123.60.63.245/Uploads/Dianqi/banner/2016-09-28/57ebb272e23e2.jpg
     * img_url :
     * times : 1475064434
     * status : 1
     */

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String imgs;
        public String img_url;
        public String times;
        public String status;
    }
}
