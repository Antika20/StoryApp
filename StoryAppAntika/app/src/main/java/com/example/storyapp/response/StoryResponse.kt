package com.example.storyapp.response

import android.os.Parcelable
import com.example.storyapp.model.storyResult
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
@Parcelize
data class ListStoryItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
):Parcelable

fun ListStoryItem.keStory() = storyResult(
	this.photoUrl ?: "",
	this.createdAt ?: "",
	this.name ?: "",
	this.description ?: "",
	this.lon ?: 0.0,
	this.id ?: "",
	this.lat ?: 0.0,
)

fun List<ListStoryItem>.Stories(): MutableList<storyResult> {
	val storis = mutableListOf<storyResult>()
	this.forEach {
		storis.add(it.keStory()) }
	return storis
}