package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class PojoSnagCommentModel extends BaseApiModel {

    private List<SnagComment> data = null;

    public List<SnagComment> getComments() {
        return data;
    }

}
