package com.example.storyapp.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.telecom.Call
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.example.storyapp.databinding.StoryitemBinding
import com.example.storyapp.detail.detail
import com.example.storyapp.main.StoryAdapter.ViewHolder
import com.example.storyapp.model.storyResult

class StoryAdapter(private val context: Context, private val storis: List<storyResult>) :
    RecyclerView.Adapter<ViewHolder>() {


    class ViewHolder(private val binding: StoryitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: storyResult) {
            binding.apply {
                ivPhoto.load(story.photoUrl)
                tvUsername.text = story.name
                tvDes.text = story.description

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            androidx.core.util.Pair(ivPhoto, "Photo"),
                            androidx.core.util.Pair(tvUsername, "name"),
                            androidx.core.util.Pair(tvDes, "description"),
                        )
                    itemView.context.startActivity(
                        Intent(
                            context,
                            detail::class.java
                        ).apply { putExtra(detail.EXTRA_DETAIL_STORY, story) },
                        optionsCompat.toBundle()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(
          StoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
      )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context,storis[position])
    }

    override fun getItemCount(): Int =storis.size


}