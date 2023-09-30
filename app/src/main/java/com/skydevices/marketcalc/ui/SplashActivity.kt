package com.skydevices.marketcalc.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.skydevices.marketcalc.databinding.ActivitySplashBinding

@ExperimentalBadgeUtils class SplashActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()

        },3000)
    }
}


