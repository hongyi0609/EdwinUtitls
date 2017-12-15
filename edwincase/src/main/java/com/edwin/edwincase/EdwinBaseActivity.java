package com.edwin.edwincase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hongy_000 on 2017/12/15.
 */

public abstract class EdwinBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        loadData();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void loadData() {

    }
}
