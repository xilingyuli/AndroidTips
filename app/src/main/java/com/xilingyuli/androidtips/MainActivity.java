package com.xilingyuli.androidtips;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xilingyuli.androidtips.blog.editor.EditorActivity;
import com.xilingyuli.androidtips.blog.list.BlogListFragment;
import com.xilingyuli.androidtips.site.SiteListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    BlogListFragment blogListFragment,draftListFragment;
    SiteListFragment siteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //init fragments
        blogListFragment = BlogListFragment.newInstance(false);
        draftListFragment  = BlogListFragment.newInstance(true);
        siteListFragment = SiteListFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_main, blogListFragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @OnClick(R.id.fab)
    public void createBlog(){
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_blog:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, blogListFragment).commit();
                break;
            case R.id.nav_draft:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, draftListFragment).commit();
                break;
            case R.id.nav_site:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, siteListFragment).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
