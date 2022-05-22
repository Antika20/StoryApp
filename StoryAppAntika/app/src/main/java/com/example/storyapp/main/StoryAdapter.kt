package com.example.storyapp.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.storyapp.databinding.StoryitemBinding
import com.example.storyapp.detail.detail
import com.example.storyapp.main.StoryAdapter.ViewHolder
import com.example.storyapp.model.storyResult

class StoryAdapter(private val context: Context) :
    PagingDataAdapter<storyResult, ViewHolder>(DIFF_CALLBACK) {

    //    (private val context: Context, private val storis: List<storyResult>)
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
            StoryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(context, data)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<storyResult>() {
            override fun areItemsTheSame(oldItem: storyResult, newItem: storyResult): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: storyResult, newItem: storyResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}