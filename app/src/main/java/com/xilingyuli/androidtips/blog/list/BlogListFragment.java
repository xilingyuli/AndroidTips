package com.xilingyuli.androidtips.blog.list;

import android.os.Bundle;
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
        presenter = new BlogListPresenter(this);
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
    public void showChooseOperationDialog(String accessUrl) {
        new AlertDialog.Builder(this.getActivity())
                .setItems(new String[]{"重命名","删除"}, (dialogInterface, i) -> {
                    switch (i){
                        case 0:
                            presenter.renameBlog(accessUrl,"xx.md");
                            break;
                        case 1:
                            presenter.deleteBlog(accessUrl);
                            break;
                    }
                }).show();
    }
}

