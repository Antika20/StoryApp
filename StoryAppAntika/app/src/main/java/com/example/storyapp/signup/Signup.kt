package com.example.storyapp.signup


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.storyapp.body.RegisterBody
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.login.LoginActivity

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels() {
        ViewModelFactory.getInstance(this)
    }
    private val isValid = mutableListOf(false, false, false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAnimation()

        signupViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.apply {
            edtName.doAfterTextChanged {
                isValid[0] = it?.isNotEmpty() ?: false
                validasiButton()
            }
            edtEmail.doAfterTextChanged {
                isValid[1] = it?.isNotBlank() ?: false
                validasiButton()
            }
            inputPassword.OnValidasiPass(
                activity = this@Signup,
                hideErrorPass = {
                    binding.layout.isErrorEnabled = false
                    isValid[2] = true
                    validasiButton()
                },
                showErrorPass = {
                    binding.layout.apply {
                        error = it
                        isErrorEnabled = true
                        isValid[2] = false
                        validasiButton()
                    }
                }
            )

            tvRegister.setOnClickListener { finish() }

            btnRegisterSign.setOnClickListener {
                signupViewModel.signup(
                    RegisterBody(
                        inputPassword.text.toString(),
                        edtName.text.toString(),
                        edtEmail.text.toString()
                    )
                ) { isSuccess, message ->
                    if (isSuccess) {
                        Toast.makeText(this@Signup, message, Toast.LENGTH_SHORT).show()
                        toLoginActivity()
                    }
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean){
        binding.apply {
            progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
            btnRegisterSign.visibility = if (isShow) View.GONE else View.VISIBLE
        }
    }

    private fun toLoginActivity(){
        startActivity(Intent(this@Signup,LoginActivity::class.java))
        finish()
    }

    private fun validasiButton() {
        binding.btnRegisterSign.isEnabled = isValid.filter { it }.size == 3
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imgStarted, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvStarted, View.ALPHA, 1f).setDuration(500)
        val layoutName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(500)
        val layoutEmail = ObjectAnimator.ofFloat(binding.textInputLayout2, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val layoutPass = ObjectAnimator.ofFloat(binding.layout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegisterSign, View.ALPHA, 1f).setDuration(500)
        val tanyaRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                layoutName,
                name,
                layoutEmail,
                email,
                layoutPass,
                password,
                btnRegister,
                tanyaRegister
            )
            startDelay = 500
        }.start()
    }
}