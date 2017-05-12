package com.xilingyuli.androidtips.blog.read;

import android.app.Activity;
import android.content.Intent;

import com.xilingyuli.androidtips.blog.editor.EditorActivity;
import com.xilingyuli.markdown.MarkDownPreviewView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.xilingyuli.androidtips.blog.editor.EditorActivity.CONTENT;
import static com.xilingyuli.androidtips.blog.editor.EditorActivity.TITLE;

/**
 * Created by xilingyuli on 2017/3/17.
 */

public class ReadBlogPresenter implements ReadBlogContract.Presenter {

    private Activity activity;
    private ReadBlogContract.View view;
    private MarkDownPreviewView previewView;

    private String name,url;
    private String content;
    private OkHttpClient client;

    ReadBlogPresenter(Activity activity, ReadBlogContract.View view, MarkDownPreviewView previewView){
        this.activity = activity;
        this.view = view;
        this.previewView = previewView;
        client = new OkHttpClient();
    }

    @Override
    public void setNameAndUrl(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public void edit() {
        if(name==null||name.isEmpty())
            return;
        Intent intent = new Intent(activity, EditorActivity.class);
        intent.putExtra(TITLE,name);
        intent.putExtra(CONTENT,content);
        activity.startActivity(intent);
    }

    @Override
    public void subscribe() {
        preview();
    }

    @Override
    public void unsubscribe() {

    }

    public void preview(){
        if(url==null||url.isEmpty())
            return;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                content = response.body().string();
                activity.runOnUiThread(()->previewView.preview(content));
            }
        });
    }
}
