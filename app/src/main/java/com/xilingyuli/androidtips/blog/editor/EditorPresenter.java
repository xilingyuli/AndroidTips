package com.xilingyuli.androidtips.blog.editor;

import android.util.Pair;

import com.xilingyuli.markdown.MarkDownController;
import com.xilingyuli.markdown.MarkDownEditorView;
import com.xilingyuli.markdown.MarkDownPreviewView;
import com.xilingyuli.markdown.OnPreInsertListener;
import com.xilingyuli.markdown.ToolsAdapter;


/**
 * Created by xilingyuli on 2017/3/9.
 */

public class EditorPresenter implements EditorContract.Presenter {

    private EditorFragment editorFragment;
    private PreviewFragment previewFragment;

    private MarkDownController.Builder builder;
    private MarkDownController markDownController;

    private EditorContract.View view;

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
    public void insertImage() {

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
