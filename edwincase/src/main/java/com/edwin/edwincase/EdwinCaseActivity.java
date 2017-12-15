package com.edwin.edwincase;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.edwin.edwinlibrary.baseView.FundCurveView;
import com.edwin.edwinlibrary.entity.FundMode;
import com.edwin.edwinlibrary.entity.OriginFundMode;
import com.edwin.edwinlibrary.utils.GsonUtil;
import com.edwin.edwinlibrary.utils.SimulateNetAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdwinCaseActivity extends EdwinBaseActivity {

    private static final String TAG = EdwinCaseActivity.class.getSimpleName();
    private FundCurveView mFundCurveView;
    private static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edwin_case);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mFundCurveView = findViewById(R.id.fund_view);
    }

    @Override
    protected void initData() {
        mHandler = new FundHandler();
    }

    @Override
    protected void loadData() {
        mHandler.postDelayed(mRunnable, 1000);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            String originalFundData = SimulateNetAPI.Companion.getOriginalFunData(getApplicationContext());
            if (originalFundData == null) {
                Log.e(TAG, "loadData: 从网络获取到的数据为空");
                return;
            }
            OriginFundMode[] originFundModes;
            try {
                originFundModes = GsonUtil.fromJson2Object(originalFundData, OriginFundMode[].class);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            List<OriginFundMode> originFundModeList = Arrays.asList(originFundModes);
            //开始适配图标
            List<FundMode> fundModeList = adapterData(originFundModeList);
            if (fundModeList != null) {
                mFundCurveView.setDataList(fundModeList);
            } else {
                Log.e(TAG, "run: 数据适配失败、、、、");
            }
        }
    };

    private List<FundMode> adapterData(List<OriginFundMode> originFundModeList) {
        List<FundMode> fundModeList = new ArrayList<>();
        for (OriginFundMode origin: originFundModeList) {
            FundMode f = new FundMode(origin.getTimestamp() * 1000, origin.getActual());
            fundModeList.add(f);
            Log.e(TAG, "adapterData: 适配之前：" + origin.getActual() + "----->>" + f.getDataY());
        }
        return fundModeList;
    }

    private static class FundHandler extends Handler {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);

    }
}
