package com.example.storyapp.main


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
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.storyapp.R
import com.example.storyapp.add.AddStoryActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.helper.DataHelper.tokenBearer
import com.example.storyapp.helper.ViewModelFactory
import com.example.storyapp.local.LocalData
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.model.LoginResult
import com.example.storyapp.model.storyResult
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var Mainadapter: StoryAdapter
    private var token = ""
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this@MainActivity)
    }

    private lateinit var localData :LocalData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localData = LocalData(this)
        observe()

        lifecycleScope.launch {
            localData.getLogin().collect {
                token = it.tokenBearer
                Log.d("TAG", "onCreate: getLogin = $it ")
                mainViewModel.getAllStories(token)
            }
        }

        binding.addStory.setOnClickListener {
            startForStoryResult.launch(Intent(this@MainActivity, AddStoryActivity::class.java))
        }

        mainViewModel.stories.observe(this, this::popularStory)
    }

    private val startForStoryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            mainViewModel.getAllStories(token)
        }
    }

    private fun observe() {
        mainViewModel.isSuccess.observe(this){ isSucces ->
            if (isSucces){
                Toast.makeText(this, "Succesfully to Login ", Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel.isLoading.observe(this){loading->
            showLoading(loading)
        }
        mainViewModel.errorMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading:Boolean){
       binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun popularStory(stories: List<storyResult>) {
      Mainadapter = StoryAdapter(this@MainActivity,stories)
        Log.d("TAG", "popularStory: stories = $stories")
        binding.rvStory.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = Mainadapter
        }
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpLogout(){
        lifecycleScope.launch {
            localData.putLoginIn(LoginResult("", "", ""))
                .also {
                    val logout = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(logout)
                }
        }
    }

}




