package com.example.medr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medr.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        clicks()
    }

    private fun clicks() {
        binding.imgContinue.setOnClickListener {
            val i  = Intent(this@InfoActivity,MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}