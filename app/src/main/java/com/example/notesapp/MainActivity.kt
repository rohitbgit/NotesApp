package com.example.notesapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if(savedInstanceState==null){
            val isLoggedIn = getSharedPreferences("NotesApp",Context.MODE_PRIVATE)
                .getBoolean("isLoggedIn",false)
            if (isLoggedIn){
                navController.navigate(R.id.notesScreen)
            }else{
                navController.navigate(R.id.loginScreen)
            }
        }
    }
}