package com.xilingyuli.markdown;

import android.util.Pair;

/**
 * Created by xilingyuli on 2017/3/2.
 */

public interface OnPreInsertListener {
    public String onPreInsertImage();
    public Pair<String, String> onPreInsertLink();
    public Pair<Integer,Integer> onPreInsertTable();
}
