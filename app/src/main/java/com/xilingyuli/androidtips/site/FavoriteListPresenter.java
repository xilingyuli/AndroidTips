package com.xilingyuli.androidtips.site;

import android.app.Activity;

import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.xilingyuli.androidtips.BaseListContract;
import com.xilingyuli.androidtips.BaseListPresenter;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;
import com.xilingyuli.androidtips.utils.FileUtil;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * Created by xilingyuli on 2017/3/23.
 */

public class FavoriteListPresenter extends BaseListPresenter implements SiteListContract.Presenter {

    FavoriteListPresenter(Activity activity, BaseListContract.View view) {
        super(activity, view, CloudDataHelper.ACTION_LIST_FAVORITE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Map<String, String>> formatData(List<Map<String, String>> data) {
        for(Map<String,String> map:data){
            String name = map.get("name");
            map.put("fname", URLDecoder.decode(name.substring(0,name.indexOf(" "))));
            map.put("furl", URLDecoder.decode(name.substring(name.indexOf(" ")+1)));
        }
        return data;
    }

    @SuppressWarnings("deprecation")
    public void saveSite(String name, String url){
        String fileName = URLEncoder.encode(name)+" "+URLEncoder.encode(url);
        FileUtil.saveFile(fileName,"");
        if(client==null)
            client = CloudDataUtil.createCOSClient(activity);
        PutObjectRequest request = (PutObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_UPLOAD_FAVORITE,
                new ICmdTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        activity.runOnUiThread(FavoriteListPresenter.this::refresh);
                    }

                    @Override
                    public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                        activity.runOnUiThread(()->((SiteListContract.View)view).showAlertDialog(cosResult.msg));
                    }
                },
                new File(FileUtil.ROOT_PATH+fileName));
        client.putObject(request);
    }

}
