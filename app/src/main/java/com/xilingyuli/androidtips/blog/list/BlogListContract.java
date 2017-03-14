package com.xilingyuli.androidtips.blog.list;

import com.xilingyuli.androidtips.BasePresenter;
import com.xilingyuli.androidtips.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/13.
 */

interface BlogListContract {
    interface View extends BaseView<Presenter> {
        void setData(List<Map<String, String>> data);
    }
    interface Presenter extends BasePresenter{

    }
}
