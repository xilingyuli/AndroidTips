package com.xilingyuli.androidtips.blog.view;

import com.xilingyuli.androidtips.BasePresenter;
import com.xilingyuli.androidtips.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/13.
 */

interface ReadBlogContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter{
        void setUrl(String url);
    }
}
