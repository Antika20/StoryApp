package com.example.storyapp.ComponenPass

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class PassEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        initView()

    }

    private val hideErrorPass = MutableLiveData<Boolean>()
    private val showErrorPass = MutableLiveData<String>()
    private val isNotEmpty = MutableLiveData<Boolean>()

    fun isNotEmpty(onEvent: (isNotEmpty: Boolean)-> Unit) {
        isNotEmpty.observe(context as LifecycleOwner) { onEvent(it) }
    }

    fun OnValidasiPass(activity: Activity,hideErrorPass:() -> Unit,showErrorPass: (message: String) -> Unit ){

        this.hideErrorPass.observe(activity as LifecycleOwner) { hideErrorPass() }
        this.showErrorPass.observe(activity as LifecycleOwner) { showErrorPass(it) }
    }

    private fun initView() {
        hint = "Input Your Password here"
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod()
        doAfterTextChanged {
            if (it?.isBlank() == true) showErrorPass("Must be not empty") else {
                if ((it?.length ?: 0) < 6
                ) showErrorPass("Password must be at least 6 char") else hideErrorPass()
            }
        }
    }

    private fun hideErrorPass() {
       hideErrorPass.value = true
    }

    private fun showErrorPass(message: String) {
        showErrorPass.value= message
    }
}

