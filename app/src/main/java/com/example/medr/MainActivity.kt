package com.example.medr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.medr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        observeDestination()
    }

    private fun observeDestination() {
        navController.addOnDestinationChangedListener { n,d,a ->
            when(d.id){
                R.id.musicFragment -> {binding.bottomNavigationView.visibility = View.VISIBLE}
                R.id.notesFragment -> {binding.bottomNavigationView.visibility = View.VISIBLE}
                R.id.photosFragment -> {binding.bottomNavigationView.visibility = View.VISIBLE}
                R.id.musicCreateFragment -> {binding.bottomNavigationView.visibility = View.GONE}
                R.id.photoCreateFragment -> {binding.bottomNavigationView.visibility = View.GONE}
                R.id.noteCreateFragment -> {binding.bottomNavigationView.visibility = View.GONE}
            }
        }
    }
}