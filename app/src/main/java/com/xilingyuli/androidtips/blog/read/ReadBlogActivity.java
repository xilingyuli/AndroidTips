package com.xilingyuli.androidtips.blog.read;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        String blogName = getIntent().getStringExtra(BLOG_NAME);
        String blogUrl = getIntent().getStringExtra(BLOG_URL);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(blogName);

        setPresenter(new ReadBlogPresenter(this, this, previewView));
        presenter.setNameAndUrl(blogName,blogUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_read_blog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                presenter.edit();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
