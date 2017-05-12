package com.xilingyuli.androidtips.site;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xilingyuli.androidtips.R;
import com.xilingyuli.androidtips.blog.editor.EditorActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SiteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SiteListFragment extends Fragment implements SiteListContract.View{

    @BindView(R.id.listview)
    ListView listView;

    SiteListContract.Presenter presenter;
    SimpleAdapter adapter;
    List<Map<String,String>> data;

    public SiteListFragment() {
        // Required empty public constructor
    }

    public static SiteListFragment newInstance() {
        return new SiteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new SiteListPresenter(getActivity(),this));
        data = new ArrayList<>();
        adapter = new SimpleAdapter(
                getActivity(),
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"fname","furl"},
                new int[]{android.R.id.text1,android.R.id.text2});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_site_list, container, false);
        ButterKnife.bind(this, view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {

        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void setPresenter(SiteListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(List<Map<String, String>> data) {
        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addData(List<Map<String, String>> data) {
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hasDataFinish(boolean isFinish) {

    }

    @Override
    public void showAddSiteDialog(){
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_insert_link,null);
        final TextInputEditText name = (TextInputEditText)view.findViewById(R.id.linkName);
        final TextInputEditText url = (TextInputEditText)view.findViewById(R.id.linkUrl);
        new AlertDialog.Builder(getActivity())
                .setTitle("Add Site")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> presenter.saveSite(name.getText() + "", url.getText() + ""))
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void showAlertDialog(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
