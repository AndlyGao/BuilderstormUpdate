package com.builderstrom.user.data.retrofit.modals;

public class AddMoreComment extends BaseApiModel {
    private PojoCommentData data;

    public PojoCommentData getData() {
        return data;
    }

    public class PojoCommentData {
        private Integer commentid;

        public Integer getCommentid() {
            return commentid;
        }

    }
}
