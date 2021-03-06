package com.xilingyuli.androidtips.blog.editor;

import android.util.Pair;

import com.xilingyuli.androidtips.basemvp.BasePresenter;
import com.xilingyuli.androidtips.basemvp.BaseView;
import com.xilingyuli.markdown.MarkDownEditorView;
import com.xilingyuli.markdown.MarkDownPreviewView;
import com.xilingyuli.markdown.OnPreInsertListener;

import java.io.Serializable;

/**
 * Created by xilingyuli on 2017/3/9.
 */

interface EditorContract {
    interface View extends BaseView<Presenter> {
        void selectImage();
        void showInsertLinkDialog();
        void showInsertTableDialog();

        void showProcessDialog(int process);
        void showAlertDialog(String error);
    }

    interface Presenter extends BasePresenter, OnPreInsertListener, Serializable {
        void setEditorView(MarkDownEditorView editorView);
        void setPreviewView(MarkDownPreviewView previewView);

        void preview();
        boolean save(boolean local);
        boolean isNeedSave();

        void insertImage(String path);
        void insertTable(Pair<Integer, Integer> pair);
        void insertLink(Pair<String, String> pair);
    }
}
