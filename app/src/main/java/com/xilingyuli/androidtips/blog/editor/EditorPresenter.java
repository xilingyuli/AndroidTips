package com.xilingyuli.androidtips.blog.editor;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;

import com.tencent.cos.COSClient;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.xilingyuli.androidtips.model.CloudDataHelper;
import com.xilingyuli.androidtips.model.CloudDataUtil;
import com.xilingyuli.markdown.MarkDownController;
import com.xilingyuli.markdown.MarkDownEditorView;
import com.xilingyuli.markdown.MarkDownPreviewView;
import com.xilingyuli.markdown.ToolsAdapter;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by xilingyuli on 2017/3/9.
 */

public class EditorPresenter implements EditorContract.Presenter {

    private EditorContract.View view;
    private EditorFragment editorFragment;
    private PreviewFragment previewFragment;

    private MarkDownController.Builder builder;
    private MarkDownController markDownController;

    private COSClient client;

    EditorPresenter(ToolsAdapter toolsAdapter, EditorFragment editorFragment, PreviewFragment previewFragment, EditorContract.View view)
    {
        this.view = view;
        this.editorFragment = editorFragment;
        this.previewFragment = previewFragment;
        builder = new MarkDownController.Builder();
        builder.setToolsAdapter(toolsAdapter).setAutoPreview(false);
    }

    public void setEditorView(MarkDownEditorView editorView)
    {
        markDownController = builder.setEditorView(editorView).build();
        if(markDownController!=null)
            markDownController.setOnPreInsertListener(this);
    }

    public void setPreviewView(MarkDownPreviewView previewView)
    {
        markDownController = builder.setPreviewView(previewView).build();
        if(markDownController!=null)
            markDownController.setOnPreInsertListener(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void save() {

    }

    @Override
    public void preview() {
        previewFragment.setTitle(editorFragment.getTitle());
        markDownController.preview();
    }


    @Override
    public void insertImage(final String path) {
        if(client==null)
            client = CloudDataUtil.createCOSClient((Activity)view);
        Observable.create((ObservableOnSubscribe<String>) e -> {
                PutObjectRequest request = (PutObjectRequest) CloudDataHelper.createCOSRequest(CloudDataHelper.ACTION_UPLOAD_IMAGE,path);
                if(request==null) {
                    e.onError(new Throwable("Error file"));
                    return;
                }
                request.setListener(new IUploadTaskListener() {
                    @Override
                    public void onProgress(COSRequest cosRequest, long l, long l1) {}

                    @Override
                    public void onCancel(COSRequest cosRequest, COSResult cosResult) {}

                    @Override
                    public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                        e.onNext(((PutObjectResult)cosResult).access_url);
                    }

                    @Override
                    public void onFailed(COSRequest cosRequest, COSResult cosResult) {}
                });
                client.putObject(request);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(url -> markDownController.insertImage(url));
    }

    @Override
    public void insertTable(Pair<Integer, Integer> pair) {
        if(markDownController!=null)
            markDownController.insertTable(pair);
    }

    @Override
    public void insertLink(Pair<String, String> pair) {
        if(markDownController!=null)
            markDownController.insertLink(pair);
    }

    @Override
    public void onPreInsertImage() {
        view.selectImage();
    }

    @Override
    public void onPreInsertLink() {
        view.showInsertLinkDialog();
    }

    @Override
    public void onPreInsertTable() {
        view.showInsertTableDialog();
    }
}
