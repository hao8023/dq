package com.beilei.zz.dq.bean;

/**
 * Created by ZengHao on 2016/9/22 0022.
 */

public class ZanBean {

    /**
     * result : 1
     * info : {"advert":{"id":"31","name":"","content":"","type_id":"4","imgs":"http://192.168.2.99/Uploads/Dianqi/advert/2016-09-21/57e24e2f389e3.jpg","praise":"49","times":"2016-09-21 17:09:03"},"xnb":"6"}
     */

    public int result;
    /**
     * advert : {"id":"31","name":"","content":"","type_id":"4","imgs":"http://192.168.2.99/Uploads/Dianqi/advert/2016-09-21/57e24e2f389e3.jpg","praise":"49","times":"2016-09-21 17:09:03"}
     * xnb : 6
     */

    public InfoBean info;

    public static class InfoBean {
        /**
         * id : 31
         * name :
         * content :
         * type_id : 4
         * imgs : http://192.168.2.99/Uploads/Dianqi/advert/2016-09-21/57e24e2f389e3.jpg
         * praise : 49
         * times : 2016-09-21 17:09:03
         */

        public AdvertBean advert;
        public String xnb;

        public static class AdvertBean {
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
