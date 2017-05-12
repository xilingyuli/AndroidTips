package com.xilingyuli.androidtips.blog.editor;

import android.animation.LayoutTransition;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.xilingyuli.androidtips.BaseActivity;
import com.xilingyuli.androidtips.R;
import com.xilingyuli.androidtips.utils.RealPathUtil;
import com.xilingyuli.markdown.ToolsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
* Markdown Blog Edit And Preview
*/
public class EditorActivity extends BaseActivity implements EditorContract.View {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    @BindView(R.id.tools)
    RecyclerView tools;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private EditorContract.Presenter presenter;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //保持viewPager随toolbar动画移动
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup appappBarLayout = (ViewGroup)findViewById(R.id.appBar_layout);
            appappBarLayout.getLayoutTransition().setDuration(LayoutTransition.CHANGE_DISAPPEARING, 0);

            ViewGroup viewGroup = (ViewGroup)findViewById(R.id.activity_editor);
            LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        //init toolbar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ToolsAdapter toolsAdapter = new ToolsAdapter(getLayoutInflater());
        tools.setAdapter(toolsAdapter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init fragments
        String title = getIntent().getStringExtra(TITLE)==null?"":getIntent().getStringExtra(TITLE);
        String content = getIntent().getStringExtra(CONTENT)==null?"":getIntent().getStringExtra(CONTENT);
        final EditorFragment editorFragment = EditorFragment.newInstance(title,content);
        final PreviewFragment previewFragment = PreviewFragment.newInstance();

        //setPresenter
        setPresenter(new EditorPresenter(this,toolsAdapter,editorFragment,previewFragment,this));
        editorFragment.setPresenter(presenter);
        previewFragment.setPresenter(presenter);

        //init viewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position==1) {
                    tools.setVisibility(View.GONE);
                    presenter.preview();
                }
                else {
                    tools.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //显示/隐藏软键盘，在onPageSelected中调用会引起滑动动画冲突
                if (state == ViewPager.SCROLL_STATE_IDLE)
                {
                    if (viewPager.getCurrentItem() == 1) {
                        try {
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    /*else {
                        imm.showSoftInput(editorView, 0);
                    }*/
                }
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position)
                {
                    case 0:
                        return editorFragment;
                    case 1:
                        return previewFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

        });
    }

    @OnClick(R.id.fab)
    public void onChangeClick()
    {
        if(viewPager!=null) {
            int index = 1 - viewPager.getCurrentItem();
            viewPager.setCurrentItem(index, false);  //设为true不能正常切换，原因待探寻
        }
    }

    @Override
    public void onBackPressed() {
        if(presenter.isNeedSave()) {
            new AlertDialog.Builder(this)
                    .setTitle("退出")
                    .setMessage("是否保存并上传？")
                    .setPositiveButton("确定",
                            (dialogInterface, i) -> {
                                if (presenter.save(false)) super.onBackPressed();
                            })
                    .setNegativeButton("取消", (dialogInterface, i) -> super.onBackPressed())
                    .show();
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                presenter.save(true);
                break;
            case R.id.upload:
                presenter.save(false);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");  //从所有图片中进行选择
        startActivityForResult(intent, 0);
    }

    @Override
    public void showInsertLinkDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_insert_link,null);
        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.linkName);
        final TextInputEditText url = (TextInputEditText)view.findViewById(R.id.linkUrl);
        new AlertDialog.Builder(this)
                .setTitle("插入链接")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> presenter.insertLink(new Pair<>(name.getText() + "", url.getText() + "")))
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void showInsertTableDialog() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_insert_table,null);
        final TextInputEditText row = (TextInputEditText)view.findViewById(R.id.row);
        final TextInputEditText column = (TextInputEditText)view.findViewById(R.id.column);
        new AlertDialog.Builder(this)
                .setTitle("插入表格")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> presenter.insertTable(new Pair<>(
                        Integer.parseInt(row.getText() + ""),
                        Integer.parseInt(column.getText() + ""))))
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void showProcessDialog(int process) {

    }

    @Override
    public void showAlertDialog(String error) {
        Toast.makeText(EditorActivity.this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK)
        {
            presenter.insertImage(RealPathUtil.getRealPath(this,data.getData()));
        }
    }

    @Override
    public void setPresenter(EditorContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
