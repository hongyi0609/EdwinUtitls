package com.edwin.edwincase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Edwin on 2017/12/15.
 * @author Edwin
 */

public abstract class EdwinBaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView();
        super.onCreate(savedInstanceState);
        initView();
        initData();
        loadData();
    }

    protected abstract void setContentView();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void loadData() {

    }
}
