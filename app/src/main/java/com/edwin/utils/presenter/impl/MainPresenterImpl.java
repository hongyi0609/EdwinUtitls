package com.edwin.utils.presenter.impl;


import com.edwin.utils.api.zhihu.ZhihuRequest;
import com.edwin.utils.bean.UpdateItem;
import com.edwin.utils.presenter.IMainPresenter;
import com.edwin.utils.ui.iView.IMain;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Edwin on 2016/4/26 0026.
 * @author Edwin
 */
public class MainPresenterImpl extends BasePresenterImpl implements IMainPresenter {

    private IMain mIMain;

    public MainPresenterImpl(IMain main) {
        if (main == null) {
            throw new IllegalArgumentException("main must not be null");
        }
        mIMain = main;
    }

    @Override
    public void checkUpdate() {
        Subscription s = ZhihuRequest.getZhihuApi().getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final UpdateItem updateItem) {
                        mIMain.showUpdate(updateItem);
                    }
                });
        addSubscription(s);
    }
}
