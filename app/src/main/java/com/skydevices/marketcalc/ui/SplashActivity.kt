package com.skydevices.marketcalc.ui

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.viewbinding.ViewBinding
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.databinding.ActivitySplashBinding

@ExperimentalBadgeUtils class SplashActivity : AbstractActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun getLayout(): ViewBinding {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding
    }

    override fun onInject() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()

        },3000)
    }

    override fun onStartActivity() {

    }


}

