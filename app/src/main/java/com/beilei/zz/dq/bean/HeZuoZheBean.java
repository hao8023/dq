package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/23 0023.
 */

public class HeZuoZheBean {

    /**
     * last_id : 5
     * secend_id : 1
     * three_id : 1
     * money : 334
     */

    public NumBean num;
    public DetailBean detail;

    public static class NumBean {
        public String last_id;
        public String secend_id;
        public String three_id;
        public String money;
    }

    public static class DetailBean {
        /**
         * head : http://192.168.2.99/Uploads/Dianqi/head/2016-09-23/57e54b3574fe4.jpg
         * username : 小王
         */

        public List<LastIdBean> last_id;
        /**
         * head : http://192.168.2.99/Uploads/Dianqi/head/2016-09-23/57e54b3574fe4.jpg
         * username : 123456
         */

        public List<SecendIdBean> secend_id;
        /**
         * head : null
         * username : 曾浩
         */

        public List<ThreeIdBean> three_id;

        public static class LastIdBean {
            public String head;
            public String username;
        }

        public static class SecendIdBean {
            public String head;
            public String username;
        }

        public static class ThreeIdBean {
            public String head;
            public String username;
        }
    }
}
