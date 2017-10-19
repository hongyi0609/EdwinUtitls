package com.edwin.utils.presenter.impl;


import com.edwin.utils.api.zhihu.ZhihuRequest;
import com.edwin.utils.bean.zhihu.ZhihuStory;
import com.edwin.utils.presenter.IZhihuStoryPresenter;
import com.edwin.utils.ui.iView.IZhihuStory;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Edwin on 2016/4/26 0026.
 * @author Edwin
 */
public class ZhihuStoryPresenterImpl extends BasePresenterImpl implements IZhihuStoryPresenter {

    private IZhihuStory mIZhihuStory;

    public ZhihuStoryPresenterImpl(IZhihuStory zhihuStory) {
        if (zhihuStory == null) {
            throw new IllegalArgumentException("zhihuStory must not be null");
        }
        mIZhihuStory = zhihuStory;
    }

    @Override
    public void getZhihuStory(String id) {
        Subscription s = ZhihuRequest.getZhihuApi().getZhihuStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuStory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuStory.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {
                        mIZhihuStory.showZhihuStory(zhihuStory);
                    }
                });
        addSubscription(s);
    }

    /*@Override
    public void getGuokrArticle(String id) {
        Subscription s = GuokrRequest.getGuokrApi().getGuokrArticle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GuokrArticle>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuStory.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(GuokrArticle guokrArticle) {
                        mIZhihuStory.showGuokrArticle(guokrArticle);
                    }
                });
        addSubscription(s);
    }*/
}
