package com.android.componentssample.broadcast

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.componentssample.R
import kotlinx.android.synthetic.main.activity_broadcast.*

class BroadcastActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var contextBasedBroadcastReceiver: ContextBasedBroadcastReceiver
    private lateinit var localBroadcastReceiver: LocalBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        //register dynamic explicit broadcast receiver
        contextBasedBroadcastReceiver = ContextBasedBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction(BroadcastConstants.CONTEXT_BASED_BROADCAST_ACTION)
        filter.addAction(BroadcastConstants.PACKAGE_NAME_BASED_BROADCAST_ACTION)
        this.registerReceiver(contextBasedBroadcastReceiver, filter)

        //register local broadcast receiver
        localBroadcastReceiver = LocalBroadcastReceiver()
        val localIntentFilter = IntentFilter()
        localIntentFilter.addAction(BroadcastConstants.LOCAL_BROADCAST_RECEIVER_ACTION)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(localBroadcastReceiver, localIntentFilter);


        manifest_broadcast_btn.setOnClickListener(this)
        context_broadcast_btn.setOnClickListener(this)
        package_name_broadcast_btn.setOnClickListener(this)
        local_broadcast_btn.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.manifest_broadcast_btn -> sendManifestRegisteredBroadcast()
            R.id.context_broadcast_btn -> sendContextRegisteredExplicitBroadcast()
            R.id.local_broadcast_btn -> sendLocalBroadcast()
            R.id.package_name_broadcast_btn -> sendPackageNameBasedBroadcast()
        }
    }

    //sends broadcast only to application package name mentioned in setPackage method
    //app must be registered for action "android.intent.action.CONTEXT_BASED_BROADCAST_ACTION"
    private fun sendPackageNameBasedBroadcast() {
        val intent = Intent()
        intent.action = BroadcastConstants.PACKAGE_NAME_BASED_BROADCAST_ACTION
        //            intent.setPackage(packageName)
        intent.setPackage("com.secure.hiddenapp")
        applicationContext.sendBroadcast(intent)
    }

    /*
   send broadcasts of Intents to local objects within your process
    You know that the data you are broadcasting won't leave your app, so don't need to worry about leaking private data.
    It is not possible for other applications to send these broadcasts to your app, so you don't need to worry about having security holes they can exploit.
    It is more efficient than sending a global broadcast through the system.
    * */
    private fun sendLocalBroadcast() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        intent.action = BroadcastConstants.LOCAL_BROADCAST_RECEIVER_ACTION
        localBroadcastManager.sendBroadcast(intent)
    }

    //send broadcasts to all applications registered
    // for action "android.intent.action.CONTEXT_BASED_BROADCAST_ACTION"
    private fun sendContextRegisteredExplicitBroadcast() {
        val intent = Intent()
        intent.action = BroadcastConstants.CONTEXT_BASED_BROADCAST_ACTION
        applicationContext.sendBroadcast(intent)
    }

    private fun sendManifestRegisteredBroadcast() {
        val intent = Intent()
        intent.action = BroadcastConstants.MANIFEST_BROADCAST_ACTION
        sendBroadcast(intent)
    }
}