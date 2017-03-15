package com.xilingyuli.androidtips.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.DeleteObjectRequest;
import com.tencent.cos.model.ListDirRequest;
import com.tencent.cos.model.MoveObjectRequest;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.ITaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tencent.cos.utils.COSPathUtils;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by xilingyuli on 2017/3/11.
 */

public class CloudDataHelper{

    public static final String ACTION_UPLOAD_IMAGE = "ACTION_UPLOAD_IMAGE";
    public static final String ACTION_UPLOAD_BLOG = "ACTION_UPLOAD_BLOG";
    public static final String ACTION_LIST_BLOG = "ACTION_LIST_BLOG";
    public static final String ACTION_RENAME_BLOG = "ACTION_RENAME_BLOG";
    public static final String ACTION_DELETE_BLOG = "ACTION_DELETE_BLOG";

    public enum OBJECT_TYPE {
        IMAGE("images"), BLOG("blogs");
        public final String path;
        OBJECT_TYPE(String path) {
            this.path = path;
        }
    }

    public static COSRequest createCOSRequest(Object... params){
        String action = (String) params[0];
        switch (action){
            case ACTION_UPLOAD_IMAGE:
                return createUpdateObjectRequest((ITaskListener)params[1], OBJECT_TYPE.IMAGE, (File)params[2], false);
            case ACTION_UPLOAD_BLOG:
                return createUpdateObjectRequest((ITaskListener)params[1], OBJECT_TYPE.BLOG, (File)params[2], true);
            case ACTION_LIST_BLOG:
                return createListDirRequest((ITaskListener)params[1], OBJECT_TYPE.BLOG, (String)params[2]);
            case ACTION_RENAME_BLOG:
                return createMoveObjectRequest((ITaskListener)params[1], OBJECT_TYPE.BLOG, (String)params[2],(String)params[3]);
            case ACTION_DELETE_BLOG:
                return createDeleteObjectRequest((ITaskListener)params[1], OBJECT_TYPE.BLOG, (String)params[2]);
        }
        return null;
    }

    private static PutObjectRequest createUpdateObjectRequest(ITaskListener listener, OBJECT_TYPE type, File file, boolean keepName) {
        if(file==null||!file.exists()||!file.isFile()||!file.canRead())
            return null;
        String fileName = file.getName();
        String newFileName = System.currentTimeMillis()
                +(fileName.contains(".")?fileName.substring(fileName.indexOf(".")):"");
        String cosPath = type.path + "/" + (keepName?fileName:newFileName);
        String signature = CloudDataUtil.sign(false, "");

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(CloudDataUtil.bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(file.getPath());
        putObjectRequest.setSign(signature);
        putObjectRequest.setInsertOnly("0");
        putObjectRequest.setListener(listener);
        return putObjectRequest;
    }

    private static ListDirRequest createListDirRequest(ITaskListener listener, OBJECT_TYPE type, String content) {
        String signature = CloudDataUtil.sign(false, "");
        ListDirRequest listDirRequest = new ListDirRequest();
        listDirRequest.setBucket(CloudDataUtil.bucket);
        listDirRequest.setCosPath(type.path);
        listDirRequest.setNum(100);
        listDirRequest.setContent(content);
        listDirRequest.setSign(signature);
        listDirRequest.setListener(listener);
        return listDirRequest;
    }

    private static MoveObjectRequest createMoveObjectRequest(ITaskListener listener, OBJECT_TYPE type, String oldName, String newName) {
        String cosPath = "/"+type.path+"/"+oldName;
        try {
            cosPath = "/"+type.path+"/"+URLEncoder.encode(oldName,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String fullPath = "/"+CloudDataUtil.appId+"/"+CloudDataUtil.bucket+cosPath;

        MoveObjectRequest moveObjectRequest = new MoveObjectRequest();
        moveObjectRequest.setBucket(CloudDataUtil.bucket);
        moveObjectRequest.setCosPath(cosPath);
        moveObjectRequest.setDest_Filed(type.path+"/"+newName);
        moveObjectRequest.setSign(CloudDataUtil.sign(true, fullPath));
        moveObjectRequest.setListener(listener);
        return moveObjectRequest;

    }

    private static DeleteObjectRequest createDeleteObjectRequest(ITaskListener listener, OBJECT_TYPE type, String name){
        String cosPath = "/"+type.path+"/"+name;
        try {
            cosPath = "/"+type.path+"/"+URLEncoder.encode(name,"utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String fullPath = "/"+CloudDataUtil.appId+"/"+CloudDataUtil.bucket+cosPath;

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
        deleteObjectRequest.setBucket(CloudDataUtil.bucket);
        deleteObjectRequest.setCosPath(cosPath);
        deleteObjectRequest.setSign(CloudDataUtil.sign(true,fullPath));
        deleteObjectRequest.setListener(listener);
        return deleteObjectRequest;
    }
}
