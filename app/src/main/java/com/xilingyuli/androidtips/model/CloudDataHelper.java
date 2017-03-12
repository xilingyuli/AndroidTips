package com.xilingyuli.androidtips.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.ITaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.io.File;

/**
 * Created by xilingyuli on 2017/3/11.
 */

public class CloudDataHelper{

    public static final String ACTION_UPLOAD_IMAGE = "ACTION_UPLOAD_IMAGE";
    public static final String ACTION_UPLOAD_BLOG = "ACTION_UPLOAD_BLOG";

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
        String signature = CloudDataUtil.sign(false, cosPath);

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(CloudDataUtil.bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(file.getPath());
        putObjectRequest.setSign(signature);
        putObjectRequest.setInsertOnly("0");
        putObjectRequest.setListener(listener);
        return putObjectRequest;
    }
}
