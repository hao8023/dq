package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/19 0019.
 */

public class ADTypeBean {

    /**
     * id : 4
     * name : 电器类
     * color : #ff9a56
     * type_detail : {"id":"34","name":"","content":"","type_id":"4","imgs":"http://192.168.2.99/Uploads/Dianqi/advert/2016-09-21/57e24e67b2686.jpg","praise":"12","times":"1474448999"}
     */

    public List<DataBean> data;

    public static class DataBean {
        public String id;
        public String name;
        public String color;
        /**
         * id : 34
         * name :
         * content :
         * type_id : 4
         * imgs : http://192.168.2.99/Uploads/Dianqi/advert/2016-09-21/57e24e67b2686.jpg
         * praise : 12
         * times : 1474448999
         */

        public TypeDetailBean type_detail;

        public static class TypeDetailBean {
            public String id;
            public String name;
            public String content;
            public String type_id;
            public String imgs;
            public String praise;
            public String times;
        }
    }
}
