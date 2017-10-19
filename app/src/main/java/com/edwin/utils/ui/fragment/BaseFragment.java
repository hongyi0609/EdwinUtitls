package com.edwin.utils.ui.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.edwin.utils.R;
import com.edwin.utils.utils.SharePreferenceUtil;

public class BaseFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    public void setSwipeRefreshLayoutColor(SwipeRefreshLayout swipeRefreshLayout){
        swipeRefreshLayout.setColorSchemeColors(
                getActivity().getSharedPreferences(
                        SharePreferenceUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
                                SharePreferenceUtil.VIBRANT, ContextCompat.getColor(getActivity(), R.color.colorAccent)));
    }
}
