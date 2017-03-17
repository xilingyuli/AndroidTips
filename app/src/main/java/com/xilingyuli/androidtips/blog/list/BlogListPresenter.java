package com.xilingyuli.androidtips.blog.list;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.cos.COSClient;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.DeleteObjectRequest;
import com.tencent.cos.model.DeleteObjectResult;
import com.tencent.cos.model.ListDirRequest;
import com.tencent.cos.model.ListDirResult;
import com.tencent.cos.model.MoveObjectRequest;
import com.tencent.cos.model.MoveObjectResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.xilingyuli.androidtips.blog.view.ReadBlogActivity;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;

import java.util.List;
import java.util.Map;

import static com.xilingyuli.androidtips.blog.view.ReadBlogActivity.BLOG_NAME;
import static com.xilingyuli.androidtips.blog.view.ReadBlogActivity.BLOG_URL;

/**
 * Created by xilingyuli on 2017/3/13.
 */

class BlogListPresenter implements BlogListContract.Presenter {

    private BlogListContract.View view;

    private COSClient client;

    private ICmdTaskListener refreshListener,nextPageListener,renameBlogListener,deleteBlogListener;
    private String pageIndex = "";

    BlogListPresenter(BlogListContract.View view){
        this.view = view;
        initListeners();
    }

    private void initListeners(){
        refreshListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                ((Fragment)view).getActivity().runOnUiThread(() -> dealData(true, cosResult));
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {

            }
        };
        nextPageListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                ((Fragment)view).getActivity().runOnUiThread(() ->dealData(false, cosResult));
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {

            }
        };
        renameBlogListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                ((Fragment)view).getActivity().runOnUiThread(() ->refresh());
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                MoveObjectResult result = (MoveObjectResult)cosResult;
            }
        };
        deleteBlogListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                ((Fragment)view).getActivity().runOnUiThread(() ->refresh());
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                DeleteObjectResult result = (DeleteObjectResult)cosResult;
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
        requestData(true);
    }

    public void nextPage()
    {
        requestData(false);
    }

    private void requestData(boolean isRefresh){
        if(isRefresh)
            pageIndex = "";
        if(client==null)
            client = CloudDataUtil.createCOSClient(((Fragment)view).getActivity());
        ListDirRequest request = (ListDirRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_LIST_BLOG,
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

    @Override
    public void viewBlog(String name, String url) {
        Intent intent = new Intent(((Fragment)view).getActivity(), ReadBlogActivity.class);
        intent.putExtra(BLOG_NAME,name);
        intent.putExtra(BLOG_URL,url);
        ((Fragment)view).getActivity().startActivity(intent);
    }

    @Override
    public void operateBlog(String name) {
        view.showChooseOperationDialog(name);
    }

    @Override
    public void renameBlog(String oldName, String newName) {
        if(client==null)
            client = CloudDataUtil.createCOSClient(((Fragment)view).getActivity());
        MoveObjectRequest request = (MoveObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_RENAME_BLOG,
                renameBlogListener,
                oldName,
                newName
        );
        client.moveObjcet(request);
    }

    @Override
    public void deleteBlog(String oldName) {
        if(client==null)
            client = CloudDataUtil.createCOSClient(((Fragment)view).getActivity());
        DeleteObjectRequest request = (DeleteObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_DELETE_BLOG,
                deleteBlogListener,
                oldName
        );
        client.deleteObject(request);
    }
}
