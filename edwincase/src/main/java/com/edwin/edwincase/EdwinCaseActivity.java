package com.edwin.edwincase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.edwin.edwincase.activity.EdwinFundActivity;
import com.edwin.edwincase.recycler.drag.EdwinCommentsActivity;

public class EdwinCaseActivity extends EdwinBaseActivity {
    private Button mBtnFund;
    private Button mBtnDrag;
    private View.OnClickListener l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_edwin_case);
    }

    @Override
    protected void initView() {
        mBtnFund = findViewById(R.id.btn_fund);
        mBtnDrag = findViewById(R.id.btn_drag);
    }

    @Override
    protected void initData() {
        l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_fund:
                        startTarget(EdwinFundActivity.class);
                        break;
                    case R.id.btn_drag:
                        startTarget(EdwinCommentsActivity.class);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void startTarget(Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(EdwinCaseActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void loadData() {
        mBtnFund.setOnClickListener(l);
        mBtnDrag.setOnClickListener(l);
    }

}
