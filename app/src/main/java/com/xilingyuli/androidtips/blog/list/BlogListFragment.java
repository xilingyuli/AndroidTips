package com.xilingyuli.androidtips.blog.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilingyuli.androidtips.R;


public class BlogListFragment extends Fragment {

    public BlogListFragment() {
    }

    public static BlogListFragment newInstance(int columnCount) {
        return new BlogListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            //recyclerView.setAdapter(new BlogListRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }

}
