package com.android.componentssample.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.android.componentssample.GenericUtils
import com.android.componentssample.Logger
import com.android.componentssample.R
import kotlinx.android.synthetic.main.activity_service_demo.*


class ServiceDemoActivity : AppCompatActivity(), View.OnClickListener {

    var randomNumberBinderService: RandomNumberBinderService? = null

    var mIsServiceBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_demo)
        start_foreground_service_btn.setOnClickListener(this)
        stop_forground_service_btn.setOnClickListener(this)

        bind_service_btn.setOnClickListener(this)
        unbind_service_btn.setOnClickListener(this)
        next_activity_btn.setOnClickListener(this)
        GenericUtils.startBackgroundWorker()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bind_service_btn -> bindRandomNumberService()
            R.id.unbind_service_btn -> unbindRandomNumberService()
            R.id.next_activity_btn -> launchNextActivity()
            R.id.start_foreground_service_btn -> startDemoForegroundService()
            R.id.stop_forground_service_btn -> stopDemoForegroundService()
        }

    }

    private fun stopDemoForegroundService() {
        if (ScannerForegroundService.serviceRunning) {
            val intent = Intent(this, ScannerForegroundService::class.java)
            intent.action = ScannerForegroundService.STOP_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, intent)
            } else {
                startService(intent)
            }
        }
    }


    private fun startDemoForegroundService() {

        if (!ScannerForegroundService.serviceRunning) {
            val intent = Intent(this, ScannerForegroundService::class.java)
            intent.action = ScannerForegroundService.START_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, intent)
            } else {
                startService(intent)
            }
        }
    }

    private fun launchNextActivity() {
        startActivity(Intent(this, BindServiceDemoActivity::class.java))
    }

    private fun unbindRandomNumberService() {
        if (mIsServiceBound) {
            Logger.logMsg(" ${ServiceConstants.BOUND_SERVICE_TAG} unbindRandomNumberService")
            unbindService(serviceConnection)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        unbindRandomNumberService()
    }
}