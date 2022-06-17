package com.example.storyapp.detail

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.helper.DataHelper.simpleTime
import com.example.storyapp.model.storyResult
import java.util.*

class detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_DETAIL_STORY = "extra_Detail story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getParcelable<storyResult>(EXTRA_DETAIL_STORY)?.let {
            with(binding) {
                imageView.load(it.photoUrl) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(8f))
                }
                tvUserName.text = it.name
                tvDesc.text = it.description
                tvDate.text = it.createdAt.simpleTime
            }
        }
    }
}