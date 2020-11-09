package com.android.componentssample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Krishna Upadhya on 17/10/20.
 * packageNamedBroadcastReceiver is not declared in manifest with actions in intent filter,
 * it will be registerd dynamically using context
 */
class PackageNameBasedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(BroadcastConstants.TAG_BROADCAST, "packageNamedBroadcastReceiver onReceive")
        intent?.run {
            Log.i(BroadcastConstants.TAG_BROADCAST, "action " + intent.action)
        }
    }
}