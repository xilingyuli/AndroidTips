package com.xilingyuli.markdown;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;

/**
 * Created by xilingyuli on 2017/2/28.
 */

public class MarkDownController implements TextWatcher {

    private final MarkDownEditorView editorView;
    private final MarkDownPreviewView previewView;
    private final ToolsAdapter toolsAdapter;
    private boolean autoPreview = false;

    MarkDownController(@NonNull MarkDownEditorView editorView,@NonNull  MarkDownPreviewView previewView,@NonNull  ToolsAdapter toolsAdapter)
    {
        this(editorView,previewView,toolsAdapter,true);
    }

    public MarkDownController(@NonNull MarkDownEditorView editorView, @NonNull MarkDownPreviewView previewView, @NonNull ToolsAdapter toolsAdapter, boolean autoPreview)
    {
        this.editorView = editorView;
        this.previewView = previewView;
        this.toolsAdapter = toolsAdapter;

        //bind toolsAdapter to editorView
        this.toolsAdapter.setEditor(this.editorView);

        //preview
        preview();

        //set autoPreview true or false
        setAutoPreview(autoPreview);
    }

    public void setAutoPreview(boolean autoPreview)
    {
        if(this.autoPreview==autoPreview)
            return;
        this.autoPreview = autoPreview;
        if(autoPreview)
            editorView.addTextChangedListener(this);
        else
            editorView.removeTextChangedListener(this);
    }

    public void setOnPreInsertListener(OnPreInsertListener listener)
    {
        toolsAdapter.setOnPreInsertListener(listener);
    }

    public void insertImage(String url)
    {
        editorView.insertImage(url);
    }

    public void insertLink(Pair<String, String> info)
    {
        editorView.insertLink(info);
    }

    public void insertTable(Pair<Integer,Integer> size)
    {
        editorView.insertTable(size);
    }

    public void preview()
    {
        previewView.preview(editorView.getText()+"");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        preview();
    }

    public static class Builder
    {
        private MarkDownEditorView editorView;
        private MarkDownPreviewView previewView;
        private ToolsAdapter toolsAdapter;
        private boolean autoPreview = false;

        public Builder(){}

        public Builder setEditorView(MarkDownEditorView editorView) {
            this.editorView = editorView;
            return this;
        }

        public Builder setPreviewView(MarkDownPreviewView previewView) {
            this.previewView = previewView;
            return this;
        }

        public Builder setToolsAdapter(ToolsAdapter toolsAdapter) {
            this.toolsAdapter = toolsAdapter;
            return this;
        }

        public Builder setAutoPreview(boolean autoPreview) {
            this.autoPreview = autoPreview;
            return this;
        }

        public MarkDownController build(){
            if(editorView!=null&&previewView!=null&&toolsAdapter!=null)
                return new MarkDownController(editorView,previewView,toolsAdapter);
            else
                return null;
        }
    }
}
