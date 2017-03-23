package com.xilingyuli.androidtips.site;

import com.xilingyuli.androidtips.BaseListContract;


/**
 * Created by xilingyuli on 2017/3/23.
 */

interface SiteListContract extends BaseListContract {
    interface View extends BaseListContract.View<Presenter> {
    }
    interface Presenter extends BaseListContract.Presenter{
    }
}
