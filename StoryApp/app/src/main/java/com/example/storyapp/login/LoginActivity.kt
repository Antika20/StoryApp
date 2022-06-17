package com.example.storyapp.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.body.LoginBody
import com.example.storyapp.signup.Signup
import com.example.storyapp.databinding.ActivityLoginBinding

import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.local.LocalData
import com.example.storyapp.main.MainActivity
import com.example.storyapp.model.LoginResult

import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel :LoginViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }
    private val isValid = mutableListOf(false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAnimation()
        setupView()


        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.edtEmail.doAfterTextChanged {
            isValid[0] = it?.isNotBlank() ?: false
            validasiButton()
        }
        binding.btnLogin1.setOnClickListener {

            loginViewModel.login(
                this,LoginBody(
                    binding.inputPassword.text.toString(),
                    binding.edtEmail.text.toString()
                )
            ){
                putLoginIn(it)
            }
        }


        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, Signup::class.java))
        }



        binding.inputPassword.OnValidasiPass(
            activity = this@LoginActivity,
            hideErrorPass = { binding.layout.isErrorEnabled = false
                isValid[1] = true
                validasiButton()
            },showErrorPass = {
                binding.layout.apply {
                    error = it
                    isErrorEnabled = true
                    isValid[1] = false
                    validasiButton()
                }
            }
        )

    }



   private fun showLoading(isLoading:Boolean){
       binding.progressBar3.visibility = if(isLoading)View.VISIBLE else View.GONE
       binding.btnLogin1.visibility = if (isLoading) View.GONE else View.VISIBLE
   }

    private fun putLoginIn(loginResult: LoginResult) {
        lifecycleScope.launch { LocalData(this@LoginActivity).putLoginIn(loginResult) }
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    private fun validasiButton() {
        binding.btnLogin1.isEnabled = isValid.filter { it }.size == 2
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAnimation(){
        ObjectAnimator.ofFloat(binding.imgStarted, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val layoutEmail = ObjectAnimator.ofFloat(binding.postLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val layoutPass = ObjectAnimator.ofFloat(binding.layout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin1, View.ALPHA, 1f).setDuration(500)
        val tanyaLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                layoutEmail,
                email,
                layoutPass,
                password,
                btnLogin,
                tanyaLogin
            )
            startDelay = 500
        }.start()
    }

}