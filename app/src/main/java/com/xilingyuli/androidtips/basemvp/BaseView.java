package com.xilingyuli.androidtips.basemvp;

/**
 * Created by xilingyuli on 2017/3/9.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
