package com.example.zsk.scrollpicture;

import java.util.List;

/**
 * @author ZSK
 * @date 2018/3/28
 * @function
 */

public class ScrolModel {

    /**
     * data : {"player":[{"pic":"http://doucaiwang.com/data/afficheimg/1518062774006654912.jpg"},{"pic":"http://doucaiwang.com/data/afficheimg/1518062872850603609.jpg"}]}
     * status : {"succeed":1}
     */

    private DataBean data;
    private StatusBean status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public static class DataBean {
        private List<PlayerBean> player;

        public List<PlayerBean> getPlayer() {
            return player;
        }

        public void setPlayer(List<PlayerBean> player) {
            this.player = player;
        }

        public static class PlayerBean {
            /**
             * pic : http://doucaiwang.com/data/afficheimg/1518062774006654912.jpg
             */

            private String pic;

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }

    public static class StatusBean {
        /**
         * succeed : 1
         */

        private int succeed;

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }
    }
}
