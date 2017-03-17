package com.xilingyuli.androidtips.blog.view;

import android.app.Activity;

import com.xilingyuli.markdown.MarkDownPreviewView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xilingyuli on 2017/3/17.
 */

public class ReadBlogPresenter implements ReadBlogContract.Presenter {

    ReadBlogContract.View view;
    MarkDownPreviewView previewView;

    String url;
    OkHttpClient client;

    ReadBlogPresenter(ReadBlogContract.View view, MarkDownPreviewView previewView){
        this.view = view;
        this.previewView = previewView;
        client = new OkHttpClient();
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
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
                String content = response.body().string();
                ((Activity)view).runOnUiThread(()->previewView.preview(content));
            }
        });
    }
}
