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
import com.xilingyuli.androidtips.BaseListPresenter;
import com.xilingyuli.androidtips.blog.read.ReadBlogActivity;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;

import java.util.List;
import java.util.Map;

import static com.xilingyuli.androidtips.blog.read.ReadBlogActivity.BLOG_NAME;
import static com.xilingyuli.androidtips.blog.read.ReadBlogActivity.BLOG_URL;

/**
 * Created by xilingyuli on 2017/3/13.
 */

class BlogListPresenter extends BaseListPresenter implements BlogListContract.Presenter {

    private ICmdTaskListener renameBlogListener,deleteBlogListener;

    BlogListPresenter(Activity activity, BlogListContract.View view){
        super(activity,view,CloudDataHelper.ACTION_LIST_BLOG);
    }

    @Override
    protected void initListeners(){
        super.initListeners();
        renameBlogListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                activity.runOnUiThread(() ->refresh());
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                MoveObjectResult result = (MoveObjectResult)cosResult;
            }
        };
        deleteBlogListener = new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                activity.runOnUiThread(() ->refresh());
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                DeleteObjectResult result = (DeleteObjectResult)cosResult;
            }
        };
    }

    @Override
    public void viewBlog(String name, String url) {
        Intent intent = new Intent(activity, ReadBlogActivity.class);
        intent.putExtra(BLOG_NAME,name.replace(".md",""));
        intent.putExtra(BLOG_URL,url);
        activity.startActivity(intent);
    }

    @Override
    public void operateBlog(String name) {
        ((BlogListContract.View)view).showChooseOperationDialog(name);
    }

    @Override
    public void renameBlog(String oldName, String newName) {
        if(client==null)
            client = CloudDataUtil.createCOSClient(activity);
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
            client = CloudDataUtil.createCOSClient(activity);
        DeleteObjectRequest request = (DeleteObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_DELETE_BLOG,
                deleteBlogListener,
                oldName
        );
        client.deleteObject(request);
    }
}
