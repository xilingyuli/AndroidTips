package com.xilingyuli.androidtips.blog.list;

import android.app.Fragment;

import com.tencent.cos.COSClient;
import com.xilingyuli.androidtips.model.CloudDataUtil;

/**
 * Created by xilingyuli on 2017/3/13.
 */

class BlogListPresenter implements BlogListContract.Presenter {

    private BlogListContract.View view;

    private COSClient client;

    BlogListPresenter(BlogListContract.View view){
        this.view = view;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    public void refresh()
    {
        if(client==null)
            client = CloudDataUtil.createCOSClient(((Fragment)view).getActivity());
    }
}
