package com.builderstrom.user.data.retrofit.modals;

import java.util.List;

public class PojoSnagListModel extends BaseApiModel {

    private SnagListData data;

    public SnagListData getData() {
        return data;
    }

    public class SnagListData {
        private List<SnagTotal> snagTotals = null;
        private List<Snag> snags = null;


        public List<SnagTotal> getSnagTotals() {
            return snagTotals;
        }

        public List<Snag> getSnags() {
            return snags;
        }
    }


}
