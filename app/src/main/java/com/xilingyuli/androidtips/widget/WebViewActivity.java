package com.xilingyuli.androidtips.widget;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tencent.cos.COSClient;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.xilingyuli.androidtips.R;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;
import com.xilingyuli.androidtips.utils.FileUtil;

import java.io.File;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webView;

    COSClient client;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        if(!url.startsWith("http"))
            url = "http://"+url;

        //init toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadUrl(url);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.stopLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        else
            return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.favorite:
                addFavorite();
                break;
            case R.id.open_in_browser:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webView.getUrl()));
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFavorite()
    {
        final View view = getLayoutInflater().inflate(R.layout.dialog_insert_link,null);
        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.linkName);
        final TextInputEditText url = (TextInputEditText)view.findViewById(R.id.linkUrl);
        name.setText(webView.getTitle());
        url.setText(webView.getUrl());
        new AlertDialog.Builder(this)
                .setTitle("Add Site")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> saveSite(name.getText() + "", url.getText() + ""))
                .setNegativeButton("取消",null)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void saveSite(String name, String url){
        String fileName = URLEncoder.encode(name)+" "+URLEncoder.encode(url);
        FileUtil.saveFile(fileName,"");
        if(client==null)
            client = CloudDataUtil.createCOSClient(this);
        PutObjectRequest request = (PutObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_UPLOAD_FAVORITE,
                new ICmdTaskListener() {
                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        runOnUiThread(()-> Toast.makeText(WebViewActivity.this,"已收藏",Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                        runOnUiThread(()-> Toast.makeText(WebViewActivity.this,"收藏失败",Toast.LENGTH_SHORT).show());
                    }
                },
                new File(FileUtil.ROOT_PATH+fileName));
        client.putObject(request);
    }
}
