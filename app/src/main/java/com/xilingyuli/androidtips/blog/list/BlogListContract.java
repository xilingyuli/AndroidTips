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
        void addData(List<Map<String, String>> data);
        void hasDataFinish(boolean isFinish);

        void showChooseOperationDialog(String accessUrl);
    }
    interface Presenter extends BasePresenter{
        void viewBlog(String url);
        void operateBlog(String accessUrl);

        void renameBlog(String accessUrl, String newName);
        void deleteBlog(String accessUrl);
    }
}
