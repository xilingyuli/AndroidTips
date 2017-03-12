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
import com.xilingyuli.androidtips.utils.FileUtil;
import com.xilingyuli.markdown.MarkDownController;
import com.xilingyuli.markdown.MarkDownEditorView;
import com.xilingyuli.markdown.MarkDownPreviewView;
import com.xilingyuli.markdown.ToolsAdapter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    private IUploadTaskListener uploadImageListener,uploadBlogListener;


    EditorPresenter(ToolsAdapter toolsAdapter, EditorFragment editorFragment, PreviewFragment previewFragment, EditorContract.View view)
    {
        this.view = view;
        this.editorFragment = editorFragment;
        this.previewFragment = previewFragment;
        builder = new MarkDownController.Builder();
        builder.setToolsAdapter(toolsAdapter).setAutoPreview(false);

        uploadImageListener = new IUploadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long l, long l1) {
                view.showProcessDialog((int)(l/l1));
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {}

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                if(markDownController!=null) {
                    ((Activity) view).runOnUiThread(() -> markDownController.insertImage(((PutObjectResult) cosResult).source_url));
                }
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                view.showAlertDialog(cosResult.msg);
            }
        };
        uploadBlogListener = new IUploadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long l, long l1) {
                view.showProcessDialog((int)(l/l1));
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {}

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                view.showAlertDialog("上传成功");
                ((Activity)view).finish();
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                view.showAlertDialog(cosResult.msg);
            }
        };
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
    public boolean save(boolean local) {
        if(!FileUtil.requestWritePermission((Activity)view))
            return false;
        String title = editorFragment.getTitle();
        if(title.isEmpty()){
            view.showAlertDialog("请输入标题");
            return false;
        }
        FileUtil.saveFile(title+".md",editorFragment.getContent());
        if(local) {
            view.showAlertDialog("本地保存成功");
            return true;
        }

        if(client==null)
            client = CloudDataUtil.createCOSClient((Activity)view);
        PutObjectRequest request = (PutObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_UPLOAD_BLOG,
                uploadBlogListener,
                new File(FileUtil.ROOT_PATH+title+".md"));
        if(request==null) {
            view.showAlertDialog("无法获取文件路径");
            return false;
        }
        client.putObject(request);
        return true;
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
        PutObjectRequest request = (PutObjectRequest) CloudDataHelper.createCOSRequest(
                CloudDataHelper.ACTION_UPLOAD_IMAGE,
                uploadImageListener,
                new File(path));
        if(request==null) {
            view.showAlertDialog("无法获取文件路径");
            return;
        }
        client.putObject(request);
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
