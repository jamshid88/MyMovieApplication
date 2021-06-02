package com.example.mymovieapplication.ui

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import com.example.mymovieapplication.viewmodel.HomeViewModel
import androidx.navigation.NavController
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import com.example.mymovieapplication.R
import android.view.MenuInflater
import androidx.navigation.Navigation
import com.example.mymovieapplication.databinding.MainActivityBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    var binding: MainActivityBinding? = null
    var viewModel: HomeViewModel? = null
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = MainActivityBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        navController = Navigation.findNavController(this@HomeActivity, R.id.fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        //        menuInflater.inflate(R.menu.bottom_nav_menu, menu);
//        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return true
    }
}