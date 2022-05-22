package com.example.storyapp.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Repository.RemoteData
import com.example.storyapp.model.storyResult
import com.example.storyapp.response.Stories
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class StoryPagingSource(private val token:String,private  val remoteData: RemoteData):
    PagingSource<Int,storyResult>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, storyResult> {
        return try {
            val page = params.key ?: INITIAL_PAGE_STORY
            val responData = remoteData.getAllStory(token,page,params.loadSize).listStory.Stories()
            Log.d("TAG", "load: $responData")
            LoadResult.Page(
                data = responData,
                prevKey = if(page == INITIAL_PAGE_STORY) null else page - 1,
                nextKey = if(responData.isNullOrEmpty()) null else page + 1
            )
        } catch (e : Exception){
            Log.d("CHECKDATA", "load: ${e.message.toString()}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, storyResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    private companion object{
        const val INITIAL_PAGE_STORY = 1
    }

}