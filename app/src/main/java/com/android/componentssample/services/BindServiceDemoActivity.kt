package com.android.componentssample.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.componentssample.Logger
import com.android.componentssample.R
import kotlinx.android.synthetic.main.content_first_binder_service.*

class BindServiceDemoActivity : AppCompatActivity() {
    var randomNumberBinderService: RandomNumberBinderService? = null

    var mIsServiceBound: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_binder_service)
        setSupportActionBar(findViewById(R.id.toolbar))

        bind_service_btn.setOnClickListener {
            bindRandomNumberService()
        }
    }

    private fun unbindRandomNumberService() {
        Logger.logMsg(" ${ServiceConstants.BOUND_SERVICE_TAG} unbindRandomNumberService")
        unbindService(serviceConnection)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            Logger.logMsg(" ${ServiceConstants.BOUND_SERVICE_TAG} onServiceConnected")
            Logger.showToast("bind service success onServiceConnected", applicationContext)
            val binder = iBinder as RandomNumberBinderService.RandomBinder
            randomNumberBinderService = binder.service
            mIsServiceBound = true
            getRandomNumberService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Logger.logMsg(" ${ServiceConstants.BOUND_SERVICE_TAG} onServiceDisconnected")
            Logger.showToast("onServiceDisconnected", applicationContext)
            mIsServiceBound = false
        }
    }

    private fun getRandomNumberService() {
        randomNumberBinderService
        randomNumberBinderService?.randomNumberLiveData?.observe(this, Observer { number ->
            Logger.logMsg("  RandomNumber $number ")
        })
    }

    private fun bindRandomNumberService() {
        Logger.logMsg(" ${ServiceConstants.BOUND_SERVICE_TAG} bindRandomNumberService")
        val intent = Intent(this, RandomNumberBinderService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

}