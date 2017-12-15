package com.edwin.edwinlibrary.entity

import android.content.Context

/**
 * Created by hongy_000 on 2017/12/9.
 */
class OriginFundMode(context: Context) : BaseMode() {
    /**
     * actual : 103
     * createTime : Apr 10, 2017 12:04:14 AM
     * economicId : 518
     * forecast : 102
     * historyId : 449366
     * previous : 103
     * revised :
     * timestamp : 1456213500
     */
    lateinit var actual: String
    lateinit var createTime: String
    var economicId: Int = 0
    lateinit var forecast: String
    var historyId = 0
    lateinit var previous: String
    lateinit var revised: String
    var timestamp: Long = 0

}