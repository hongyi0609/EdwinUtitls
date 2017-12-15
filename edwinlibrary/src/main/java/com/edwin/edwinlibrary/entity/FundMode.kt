package com.edwin.edwinlibrary.entity

import com.edwin.edwinlibrary.utils.RegxUtils

/**
 * Created by hongy_000 on 2017/12/7.
 * @author hongy_000
 */
 class FundMode(timestamp: Long, actual: String) : BaseMode() {
    // x轴原始数据ms
    var datetime: Long = 0
    var dataY: Float = 0f
    var originDataY: String
    //FundCurveView中的位置坐标
    var floatX:Float = 0f
    var floatY: Float = 0f

    init {
        this.datetime = timestamp
        this.originDataY = actual
        this.dataY = RegxUtils.getPureDouble(originDataY)//提取后的Y轴的值
    }

    override fun toString(): String {
        return "FundMode{" +
                "datetime=" + datetime +
                ", dataY=" + dataY +
                ", originDataY='" + originDataY + '\'' +
                ", floatX=" + floatX +
                ", floatY=" + floatY +
                "}"
    }
}