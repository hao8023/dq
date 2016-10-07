package com.beilei.zz.dq.bean;

import java.util.List;

/**
 * Created by ZengHao on 2016/9/20 0020.
 */

public class PartnerBean {

    /**
     * last_id : 2
     * secend_id : 2
     * three_id : 3
     * money : 50
     */

    public NumBean num;
    public DetailBean detail;

    public static class NumBean {
        public int last_id;
        public int secend_id;
        public int three_id;
        public String money;
    }

    public static class DetailBean {
        /**
         * head : https://aecpm.alicdn.com/simba/img/TB17sn0MVXXXXc8XpXXSutbFXXX.jpg
         * username : adminq
         */

        public List<LastIdBean> last_id;
        /**
         * head : https://aecpm.alicdn.com/simba/img/TB17sn0MVXXXXc8XpXXSutbFXXX.jpg
         * username : 小王
         */

        public List<SecendIdBean> secend_id;
        /**
         * head : https://aecpm.alicdn.com/simba/img/TB17sn0MVXXXXc8XpXXSutbFXXX.jpg
         * username : 线长
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
