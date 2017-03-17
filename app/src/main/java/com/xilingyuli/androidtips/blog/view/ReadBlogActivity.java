package com.xilingyuli.androidtips.blog.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xilingyuli.androidtips.R;
import com.xilingyuli.markdown.MarkDownPreviewView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadBlogActivity extends AppCompatActivity implements ReadBlogContract.View {

    public static final String BLOG_NAME = "name";
    public static final String BLOG_URL = "url";

    @BindView(R.id.preview)
    MarkDownPreviewView previewView;

    ReadBlogContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_blog);
        ButterKnife.bind(this);

        setPresenter(new ReadBlogPresenter(this, previewView));
        presenter.setUrl(getIntent().getStringExtra(BLOG_URL));
    }

    @Override
    public void setPresenter(ReadBlogContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }
}
