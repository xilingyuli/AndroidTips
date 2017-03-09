package com.xilingyuli.androidtips.blog.editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xilingyuli.androidtips.R;
import com.xilingyuli.markdown.MarkDownEditorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private String title;
    private String content;

    @BindView(R.id.title)
    EditText titleView;
    @BindView(R.id.content)
    MarkDownEditorView contentView;

    EditorContract.Presenter presenter;

    public EditorFragment() {
        // Required empty public constructor
    }

    public static EditorFragment newInstance(String title, String content) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPresenter(EditorContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            content = getArguments().getString(CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        ButterKnife.bind(this,view);
        titleView.setText(title);
        contentView.setText(content);
        presenter.setEditorView((MarkDownEditorView)view.findViewById(R.id.content));
        return view;
    }

    public String getTitle()
    {
        return titleView.getText()+"";
    }

    public String getContent()
    {
        return contentView.getText()+"";
    }

}
