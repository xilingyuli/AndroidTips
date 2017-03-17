package com.xilingyuli.androidtips.blog.read;

import com.xilingyuli.androidtips.BasePresenter;
import com.xilingyuli.androidtips.BaseView;

/**
 * Created by xilingyuli on 2017/3/13.
 */

interface ReadBlogContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter{
        void setNameAndUrl(String name, String url);
        void edit();
    }
}
