package com.xilingyuli.androidtips.blog.list;

import android.app.Activity;
import android.content.Intent;

import com.tencent.cos.model.COSResult;
import com.xilingyuli.androidtips.basemvp.BaseListPresenter;
import com.xilingyuli.androidtips.blog.editor.EditorActivity;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.utils.FileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.xilingyuli.androidtips.blog.editor.EditorActivity.CONTENT;
import static com.xilingyuli.androidtips.blog.editor.EditorActivity.TITLE;


/**
 * Created by xilingyuli on 2017/3/13.
 */

class DraftListPresenter extends BaseListPresenter implements BlogListContract.Presenter {

    DraftListPresenter(Activity activity, BlogListContract.View view){
        super(activity,view, CloudDataHelper.ACTION_LIST_BLOG);
    }

    @Override
    protected void requestData(boolean isRefresh){
        File[] files = FileUtil.listFiles("md");
        List<Map<String, String>> data = new ArrayList<>();
        for(File file : files){
            Map<String,String> map = new HashMap<>();
            map.put("name",file.getName());
            map.put("ctime",(file.lastModified()/1000)+"");
            data.add(map);
        }
        data = formatData(data);
        view.setData(data);
        view.hasDataFinish(true);
    }

    @Override
    protected void dealData(boolean isRefresh, COSResult cosResult){

    }

    @Override
    public void viewBlog(String name, String url) {
        Intent intent = new Intent(activity, EditorActivity.class);
        intent.putExtra(TITLE,name.replace(".md",""));
        intent.putExtra(CONTENT,FileUtil.readFile(name));
        activity.startActivity(intent);
    }

    @Override
    public void operateBlog(String name) {
        ((BlogListContract.View)view).showChooseOperationDialog(name);
    }

    @Override
    public void renameBlog(String oldName, String newName) {
        FileUtil.renameFile(oldName,newName);
        refresh();
    }

    @Override
    public void deleteBlog(String oldName) {
        FileUtil.deleteFile(oldName);
        refresh();
    }

    @Override
    public List<Map<String, String>> formatData(List<Map<String, String>> data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        for(Map<String,String> map:data){
            map.put("fname",map.get("name").replace(".md",""));
            map.put("fctime",sdf.format(new Date(Long.parseLong(map.get("ctime"))*1000)));
        }
        return data;
    }
}
