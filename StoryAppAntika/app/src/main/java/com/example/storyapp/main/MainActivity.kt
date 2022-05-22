package com.example.storyapp.main


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.add.AddStoryActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.helper.DataHelper.tokenBearer
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.local.LocalData
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.maps.MapsActivity
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import com.example.storyapp.remote.LoadingStateAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var Mainadapter: StoryAdapter
    private var token = ""
    private var isLoading = false


    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this@MainActivity)
    }
    private lateinit var localData: LocalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localData = LocalData(this)



        Mainadapter = StoryAdapter(this@MainActivity)



        lifecycleScope.launch {
            localData.getLogin().collect {
                token = it.tokenBearer
                Log.d("TAG", "onCreate: getLogin = $it ")
            }
        }


        binding.addStory.setOnClickListener {
            startForStoryResult.launch(Intent(this@MainActivity, AddStoryActivity::class.java))
        }

        addToRecyclerView()
        observe()
        mainViewModel.stories.observe(this,this::popularStories)
    }




    private fun addToRecyclerView() {

        lifecycleScope.launch {
            Mainadapter.loadStateFlow.collect {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        isLoading = true
                        mainViewModel.setLoading(true)
                    }
                    is LoadState.Error -> {
                        setError()
                    }
                    is LoadState.NotLoading -> {
                        if (isLoading) {
                            mainViewModel.setLoading(false)
                            isLoading = false
                        }
//                        lifecycleScope.launch {
//                            currentStories.clear()
//                            currentStories.addAll(Mainadapter.snapshot().items)
//                        }
                    }
                }
            }
        }

        val layoutManager1 =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        val footerAdapterLoading = Mainadapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                Mainadapter.retry()
            }
        )

        binding.rvStory.apply {
            layoutManager = layoutManager1
            adapter = footerAdapterLoading
        }
    }

    private fun setError() {
        Toast.makeText(this@MainActivity, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT)
            .show()
    }

    private val startForStoryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mainViewModel.getallStories(token)
            Log.d("DATA", "Data token $token")
        }
    }

    private fun popularStories(stories: PagingData<storyResult>){
        Mainadapter.submitData(lifecycle,stories)
    }

    private fun observe() {
        mainViewModel.isSuccess.observe(this) { isSucces ->
            if (isSucces) {
                Toast.makeText(this, "Succesfully to Login ", Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        lifecycleScope.launch {
            localData.getLogin().collect {
                mainViewModel.getallStories(it.tokenBearer)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.languange -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                setUpLogout()
            }
            R.id.maps -> {
                moveToMapsActivity()
            }
        }
        return true
    }

    private fun moveToMapsActivity() {
        Intent(this@MainActivity, MapsActivity::class.java).apply {
            putExtra(EXTRA_TOKEN, token)
        }.also {
            startActivity(it)
        }
    }

    private fun setUpLogout() {
        lifecycleScope.launch {
            localData.putLoginIn(LoginResult("", "", ""))
                .also {
                    val logout = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(logout)
                    finish()
                }
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}




