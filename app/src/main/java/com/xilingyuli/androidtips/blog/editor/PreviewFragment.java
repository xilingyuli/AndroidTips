package com.xilingyuli.androidtips.blog.editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilingyuli.androidtips.R;
import com.xilingyuli.markdown.MarkDownPreviewView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewFragment extends Fragment {

    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.content)
    MarkDownPreviewView contentView;

    EditorContract.Presenter presenter;

    public PreviewFragment() {
        // Required empty public constructor
    }

    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    public void setPresenter(EditorContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ButterKnife.bind(this, view);
        presenter.setPreviewView((MarkDownPreviewView)view.findViewById(R.id.content));
        return view;
    }

    public void setTitle(String title)
    {
        titleView.setText(title);
    }

}
