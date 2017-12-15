package com.edwin.edwinlibrary.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by hongy_000 on 2017/12/9.
 */
class SimulateNetAPI {

    companion object {

        fun getOriginalFunData(context: Context) :String? {
            val input: InputStream?
            try {
                input = context.assets.open("fund.json")
                return convertStreamToString(input)
             } catch (e: Exception) {
                 e.printStackTrace()
             }
             return null
         }

        /**
         * input 流转换为字符串
         *
         * @param inputStream
         * @return
         */
        private fun convertStreamToString(inputStream: InputStream?): String? {
            var s: String? = null
            try {
                val scanner: Scanner =
                        Scanner(inputStream, "UTF-8").useDelimiter("\\A")
                while (scanner.hasNext()) {
                    s = scanner.next()
                }
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return s
        }
    }
}