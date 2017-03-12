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
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.io.File;

/**
 * Created by xilingyuli on 2017/3/11.
 */

public class CloudDataHelper{

    public static final String ACTION_UPLOAD_IMAGE = "ACTION_UPLOAD_IMAGE";

    public enum OBJECT_TYPE {
        IMAGE("images"), BLOG("blogs");
        public final String path;
        OBJECT_TYPE(String path) {
            this.path = path;
        }
    }

    public enum ACTION {
        ACTION_UPLOAD_IMAGE, ACTION_UPLOAD_BLOG;
    }

    public static COSRequest createCOSRequest(String... params){
        switch (params[0]){
            case ACTION_UPLOAD_IMAGE:
                return createUpdateObjectRequest(OBJECT_TYPE.IMAGE, new File(params[1]));

        }
        return null;
    }

    private static PutObjectRequest createUpdateObjectRequest(OBJECT_TYPE type, File file) {
        if(file==null||!file.exists()||!file.isFile()||!file.canRead())
            return null;
        String fileName = file.getName();
        String newFileName = System.currentTimeMillis()
                +(fileName.contains(".")?fileName.substring(fileName.indexOf(".")):"");
        String cosPath = type.path + "/" + newFileName;
        String signature = CloudDataUtil.sign(false, cosPath);

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(CloudDataUtil.bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(file.getPath());
        putObjectRequest.setSign(signature);
        return putObjectRequest;
    }
}
