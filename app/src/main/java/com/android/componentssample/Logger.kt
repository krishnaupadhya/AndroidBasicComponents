package com.android.componentssample

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by Krishna Upadhya on 19/10/20.
 */

object Logger {

    @JvmOverloads
    fun logMsg(msg: String, tag: String = "DEFAULT_TAG") {
        Log.i(tag, msg)
    }

    fun showToast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}