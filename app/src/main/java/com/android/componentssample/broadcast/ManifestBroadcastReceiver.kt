package com.android.componentssample.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Created by Krishna Upadhya on 17/10/20.
 * ManifestBroadcastReceiver is declared in manifest with actions in intent filter
 */

class ManifestBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(BroadcastConstants.TAG_BROADCAST, "ManifestBroadcastReceiver onReceive")
        intent?.run {
            Log.i(BroadcastConstants.TAG_BROADCAST, "action " + intent.action)
        }
    }
}