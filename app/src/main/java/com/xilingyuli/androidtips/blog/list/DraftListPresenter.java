package com.xilingyuli.androidtips.blog.list;

import android.app.Activity;
import android.content.Intent;

import com.xilingyuli.androidtips.blog.editor.EditorActivity;
import com.xilingyuli.androidtips.utils.FileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xilingyuli.androidtips.blog.editor.EditorActivity.CONTENT;
import static com.xilingyuli.androidtips.blog.editor.EditorActivity.TITLE;


/**
 * Created by xilingyuli on 2017/3/13.
 */

class DraftListPresenter implements BlogListContract.Presenter {

    private Activity activity;
    private BlogListContract.View view;

    DraftListPresenter(Activity activity, BlogListContract.View view){
        this.activity = activity;
        this.view = view;
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
        File[] files = FileUtil.listFiles("md");
        List<Map<String, String>> data = new ArrayList<>();
        for(File file : files){
            Map<String,String> map = new HashMap<>();
            map.put("name",file.getName());
            map.put("ctime",(file.lastModified()/1000)+"");
            data.add(map);
        }
        view.setData(data);
        view.hasDataFinish(true);
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
        view.showChooseOperationDialog(name);
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
}
