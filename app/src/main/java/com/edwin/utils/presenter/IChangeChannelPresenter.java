package com.edwin.utils.presenter;


import com.edwin.utils.config.Config;

import java.util.ArrayList;

/**
 * Created by Edwin on 2016/4/26 0026.
 */
public interface IChangeChannelPresenter {

    void getChannel();

    void saveChannel(ArrayList<Config.Channel> savedChannel);
}
