package com.xilingyuli.androidtips;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.cos.COSClient;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.DeleteObjectResult;
import com.tencent.cos.model.ListDirRequest;
import com.tencent.cos.model.ListDirResult;
import com.tencent.cos.model.MoveObjectResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/23.
 */

public class BaseListPresenter implements BaseListContract.Presenter {

    protected Activity activity;
    protected BaseListContract.View view;

    protected COSClient client;
    protected String requestAction;

    protected ICmdTaskListener refreshListener,nextPageListener;
    protected String pageIndex = "";

    public BaseListPresenter(Activity activity, BaseListContract.View view, String requestAction) {
        this.activity = activity;
        this.view = view;
        this.requestAction = requestAction;
        initListeners();
    }

    protected void initListeners(){
        refreshListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                activity.runOnUiThread(() -> dealData(true, cosResult));
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {

            }
        };
        nextPageListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                activity.runOnUiThread(() ->dealData(false, cosResult));
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

    @Override
    public void refresh()
    {
        requestData(true);
    }

    @Override
    public void nextPage()
    {
        requestData(false);
    }

    private void requestData(boolean isRefresh){
        if(isRefresh)
            pageIndex = "";
        if(client==null)
            client = CloudDataUtil.createCOSClient(activity);
        ListDirRequest request = (ListDirRequest) CloudDataHelper.createCOSRequest(
                requestAction,
                isRefresh?refreshListener:nextPageListener,
                pageIndex
        );
        client.listDir(request);
    }

    private void dealData(boolean isRefresh, COSResult cosResult){
        ListDirResult result = (ListDirResult)cosResult;
        Gson gson = new Gson();
        List<Map<String, String>> data = gson.fromJson(result.infos.toString(),
                new TypeToken<List<Map<String, String>>>(){}.getType());
        pageIndex = result.context;

        if(isRefresh)
            view.setData(data);
        else
            view.addData(data);
        view.hasDataFinish(result.listover);
    }
}
