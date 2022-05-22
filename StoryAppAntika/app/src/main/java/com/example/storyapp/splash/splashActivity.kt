package com.example.storyapp.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.databinding.ActivitySplashBinding
import com.example.storyapp.local.LocalData
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.main.MainActivity
import com.example.storyapp.signup.Signup
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class splashActivity : AppCompatActivity() {
    private lateinit var localData : LocalData

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localData = LocalData(this)

        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            lifecycleScope.launch {
                localData.getLogin().collect {
                    val targeting =
                        if (it.token.isEmpty()) LoginActivity::class.java else MainActivity::class.java
                    startActivity(Intent(this@splashActivity, targeting))
                    finish()
                }
            }
        }, 3000L)
    }
}
