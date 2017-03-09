package com.xilingyuli.androidtips.blog.editor;

import android.util.Pair;

import com.xilingyuli.androidtips.BasePresenter;
import com.xilingyuli.androidtips.BaseView;
import com.xilingyuli.markdown.MarkDownEditorView;
import com.xilingyuli.markdown.MarkDownPreviewView;
import com.xilingyuli.markdown.OnPreInsertListener;

/**
 * Created by xilingyuli on 2017/3/9.
 */

public interface EditorContract {
    interface View extends BaseView<Presenter> {
        void selectImage();
        void showInsertLinkDialog();
        void showInsertTableDialog();
    }

    interface Presenter extends BasePresenter, OnPreInsertListener {
        void setEditorView(MarkDownEditorView editorView);
        void setPreviewView(MarkDownPreviewView previewView);
        void preview();
        void save();

        void insertImage();
        void insertTable(Pair<Integer, Integer> pair);
        void insertLink(Pair<String, String> pair);
    }
}
