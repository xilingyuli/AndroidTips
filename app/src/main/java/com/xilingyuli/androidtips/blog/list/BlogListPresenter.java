package com.xilingyuli.androidtips.blog.list;

import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.cos.COSClient;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.ListDirRequest;
import com.tencent.cos.model.ListDirResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/13.
 */

class BlogListPresenter implements BlogListContract.Presenter {

    private BlogListContract.View view;

    private COSClient client;

    private ICmdTaskListener refreshListener;

    BlogListPresenter(BlogListContract.View view){
        this.view = view;
        refreshListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                ListDirResult result = (ListDirResult)cosResult;
                Gson gson = new Gson();
                List<Map<String, String>> data = gson.fromJson(result.infos.toString(),
                        new TypeToken<List<Map<String, String>>>(){}.getType());
                view.setData(data);
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {

            }
        };
    }

    @Override
    public void subscribe() {
        refresh();
    }

    @Override
    public void unsubscribe() {

    }

    public void refresh()
    {
        if(client==null)
            client = CloudDataUtil.createCOSClient(((Fragment)view).getActivity());
        ListDirRequest request = (ListDirRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_LIST_BLOG,
                refreshListener,
                ""
        );
        client.listDir(request);
    }
}
