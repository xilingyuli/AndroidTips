package com.xilingyuli.androidtips.site;

import android.app.Activity;

import com.xilingyuli.androidtips.BaseListContract;
import com.xilingyuli.androidtips.BaseListPresenter;
import com.xilingyuli.androidtips.model.CloudDataHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by xilingyuli on 2017/3/23.
 */

public class SiteListPresenter extends BaseListPresenter implements SiteListContract.Presenter {

    SiteListPresenter(Activity activity, BaseListContract.View view) {
        super(activity, view, CloudDataHelper.ACTION_LIST_SITE);
    }

    @Override
    public List<Map<String, String>> formatData(List<Map<String, String>> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        for(Map<String,String> map:data){
            map.put("fname",map.get("name"));
            map.put("fctime",sdf.format(new Date(Long.parseLong(map.get("ctime"))*1000)));
        }
        return data;
    }

}
