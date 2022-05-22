package com.example.storyapp.add


import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.storyapp.R
import com.example.storyapp.camera.CameraActivity
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.helper.DataHelper
import com.example.storyapp.helper.DataHelper.reduceFileImage
import com.example.storyapp.helper.DataHelper.rotateBitmap
import com.example.storyapp.helper.DataHelper.tokenBearer
import com.example.storyapp.helper.DataHelper.uriToFile
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.local.LocalData

import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    private val viewModel: AddStoryViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    private var TokenMe = ""

    private lateinit var  localData:LocalData

    private var currentFile: File? = null
    private val isValidPost = mutableListOf(false, false,false)

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localData = LocalData(this)
        setupAnimation()


       viewModel.isLoading.observe(this){
            showLoading(it)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        validasiButton()


        lifecycleScope.launch {
            localData.getLogin().collect {
                binding.tvUserName.text = it.name
                TokenMe = it.tokenBearer
            }
        }

        binding.imageAvatar.load(DataHelper.IconMe) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }

        binding.edtDesc.doAfterTextChanged {
            isValidPost[0] = it.toString().isNotBlank()
            validasiButton()
        }

        binding.addGalery.setOnClickListener { startGallery() }
        binding.addPhotos.setOnClickListener { startCameraX() }
        binding.btnPost.setOnClickListener { postStory() }
    }

    private fun showLoading(isShow:Boolean){
        binding.apply {
            progressBar2.visibility =if(isShow) View.VISIBLE else View.GONE
            btnPost.visibility = if(isShow) View.GONE else View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            currentFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.ivPosting.setImageBitmap(result)
            isValidPost[1] = true
        }
    }

    private fun validasiButton() {
        val isValid = isValidPost.filter { it }.size == 2
        binding.btnPost.apply {
            isEnabled = isValid
            (if (isValid) R.color.purple_700 else R.color.gray)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            currentFile = myFile

            binding.ivPosting.setImageURI(selectedImg)
            isValidPost[1] = true
            validasiButton()
        }
    }


    private fun postStory() {
        if (currentFile != null) {
            val file = reduceFileImage(currentFile as File)
            val requestImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImage
            )

            viewModel.postStory(
                TokenMe,
                imageMultipart,
                binding.edtDesc.text.toString()
            ) { isPosted, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.d("AddStory", "postStory: $TokenMe")
                if (isPosted) {
                    Log.d("AddStory", "postStory: $isPosted")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun setupAnimation(){
        ObjectAnimator.ofFloat(binding.ivPosting, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val username = ObjectAnimator.ofFloat(binding.tvUserName, View.ALPHA, 1f).setDuration(500)
        val editTeks = ObjectAnimator.ofFloat(binding.edtDesc, View.ALPHA, 1f).setDuration(500)
        val card = ObjectAnimator.ofFloat(binding.tvCaption, View.ALPHA, 1f).setDuration(500)
        val appBar = ObjectAnimator.ofFloat(binding.appBarLayout, View.ALPHA, 1f).setDuration(500)
        val addPhoto = ObjectAnimator.ofFloat(binding.addPhotos, View.ALPHA, 1f).setDuration(500)
        val addGaleri = ObjectAnimator.ofFloat(binding.addGalery, View.ALPHA, 1f).setDuration(500)
        val btnPost = ObjectAnimator.ofFloat(binding.btnPost, View.ALPHA, 1f).setDuration(500)
        val imgAvatar = ObjectAnimator.ofFloat(binding.imageAvatar, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                username,
                editTeks,
                card,
                appBar,
                addGaleri,
                addPhoto,
                btnPost,
                imgAvatar
            )
            startDelay = 500
        }.start()

    }
}