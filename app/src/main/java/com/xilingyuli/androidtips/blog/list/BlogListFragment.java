package com.xilingyuli.androidtips.blog.list;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilingyuli.androidtips.R;

import java.util.List;
import java.util.Map;


public class BlogListFragment extends Fragment implements BlogListContract.View {

    BlogListRecyclerViewAdapter adapter;

    BlogListContract.Presenter presenter;

    public BlogListFragment() {
    }

    public static BlogListFragment newInstance() {
        return new BlogListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new BlogListPresenter(this.getActivity(),this);
        adapter = new BlogListRecyclerViewAdapter(getLayoutInflater(savedInstanceState),presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void setPresenter(BlogListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(List<Map<String, String>> data) {
        if(adapter!=null)
            adapter.setData(data);
    }

    @Override
    public void addData(List<Map<String, String>> data) {
        if(adapter!=null)
            adapter.addData(data);
    }

    @Override
    public void hasDataFinish(boolean isFinish) {

    }

    @Override
    public void showChooseOperationDialog(String name) {
        new AlertDialog.Builder(getActivity())
                .setItems(new String[]{"重命名","删除"}, (dialogInterface, i) -> {
                    switch (i){
                        case 0:
                            showRenameBlogDialog(name);
                            break;
                        case 1:
                            showDeleteBlogDialog(name);
                            break;
                    }
                }).show();
    }

    private void showRenameBlogDialog(String oldName){
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_rename_blog,null);
        TextInputEditText editText = (TextInputEditText)rootView.findViewById(R.id.title);
        editText.setText(oldName.replace(".md",""));
        new AlertDialog.Builder(getActivity())
                .setTitle("重命名文章")
                .setView(rootView)
                .setPositiveButton("确定", (dialogInterface, i) -> presenter.renameBlog(oldName,editText.getText()+".md"))
                .setNegativeButton("取消",null)
                .show();
    }

    private void showDeleteBlogDialog(String oldName){
        new AlertDialog.Builder(getActivity())
                .setTitle("删除文章")
                .setMessage("确定删除文章 "+oldName.replace(".md","")+" ？")
                .setPositiveButton("确定", (dialogInterface, i) -> presenter.deleteBlog(oldName))
                .setNegativeButton("取消",null)
                .show();
    }
}

