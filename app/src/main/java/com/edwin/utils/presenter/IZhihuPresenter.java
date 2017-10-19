package com.edwin.utils.presenter;

/**
 * Created by Edwin on 2016/4/23 0023.
 */
public interface IZhihuPresenter extends BasePresenter{
    void getLastZhihuNews();

    void getTheDaily(String date);

    void getLastFromCache();
}
