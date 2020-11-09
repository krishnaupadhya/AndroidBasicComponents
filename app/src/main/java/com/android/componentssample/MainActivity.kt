package com.android.componentssample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.componentssample.broadcast.BroadcastActivity
import com.android.componentssample.services.ServiceDemoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_broadcast.setOnClickListener {
            startActivity(Intent(this, BroadcastActivity::class.java))
        }

        btn_service.setOnClickListener {
            startActivity(Intent(this, ServiceDemoActivity::class.java))
        }

        btn_content_provider.setOnClickListener {
        }
    }
}