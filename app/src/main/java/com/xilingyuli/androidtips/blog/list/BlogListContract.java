package com.xilingyuli.androidtips.blog.list;

import com.xilingyuli.androidtips.basemvp.BaseListContract;

/**
 * Created by xilingyuli on 2017/3/13.
 */

interface BlogListContract{
    interface View extends BaseListContract.View<Presenter> {
        void showChooseOperationDialog(String accessUrl);
    }
    interface Presenter extends BaseListContract.Presenter{
        void viewBlog(String name, String url);
        void operateBlog(String name);

        void renameBlog(String oldName, String newName);
        void deleteBlog(String oldName);
    }
}
