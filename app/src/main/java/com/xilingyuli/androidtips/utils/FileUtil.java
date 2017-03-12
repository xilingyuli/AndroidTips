package com.xilingyuli.androidtips.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by xilingyuli on 2017/3/12.
 */

public class FileUtil {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory()+ File.separator + "AndroidTips" + File.separator;
    public static boolean requestWritePermission(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            }
        }
        return true;
    }
    public static boolean saveFile(String name, String content)
    {
        if(name==null||name.isEmpty())
            return false;
        FileOutputStream fos = null;
        try {
            File dir = new File(ROOT_PATH);
            if(!dir.exists())
                dir.mkdir();
            File file = new File(ROOT_PATH+name);
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if(fos!=null)
                    fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
