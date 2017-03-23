package com.xilingyuli.androidtips.site;

import android.app.Activity;

import com.xilingyuli.androidtips.BaseListContract;
import com.xilingyuli.androidtips.BaseListPresenter;
import com.xilingyuli.androidtips.model.CloudDataHelper;


/**
 * Created by xilingyuli on 2017/3/23.
 */

public class SiteListPresenter extends BaseListPresenter implements SiteListContract.Presenter {

    SiteListPresenter(Activity activity, BaseListContract.View view) {
        super(activity, view, CloudDataHelper.ACTION_LIST_SITE);
    }
}
