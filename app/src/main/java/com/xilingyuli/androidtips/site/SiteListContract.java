package com.xilingyuli.androidtips.site;

import com.xilingyuli.androidtips.basemvp.BaseListContract;


/**
 * Created by xilingyuli on 2017/3/23.
 */

interface SiteListContract extends BaseListContract {
    interface View extends BaseListContract.View<Presenter> {
        void showAddSiteDialog();
        void showAlertDialog(String error);
    }
    interface Presenter extends BaseListContract.Presenter{
        void saveSite(String name, String url);
    }
}
