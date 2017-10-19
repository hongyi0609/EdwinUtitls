package com.edwin.utils.ui.iView;

import com.edwin.utils.config.Config;

import java.util.ArrayList;


/**
 * Created by Edwin on 2016/4/26 0026.
 */
public interface IChangeChannel {

    void showChannel(ArrayList<Config.Channel> savedChannel, ArrayList<Config.Channel> otherChannel);
}
