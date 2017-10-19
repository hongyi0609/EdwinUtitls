package com.edwin.utils.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by YiuChoi on 2016/4/7 0007.
 */
public class RxBus {
    private static volatile RxBus defaultInstance;

    private final Subject<Object, Object> bus;

    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    public void send(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventClass) {
        return bus.ofType(eventClass);
    }
}
