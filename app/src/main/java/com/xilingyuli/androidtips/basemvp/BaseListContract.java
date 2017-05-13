package com.xilingyuli.androidtips.basemvp;

import java.util.List;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/23.
 */

public interface BaseListContract {
    interface View<Presenter> extends BaseView<Presenter> {
        void setData(List<Map<String, String>> data);
        void addData(List<Map<String, String>> data);
        void hasDataFinish(boolean isFinish);
    }
    interface Presenter extends BasePresenter {
        List<Map<String, String>> formatData(List<Map<String, String>> data);
        void refresh();
        void nextPage();
    }
}
