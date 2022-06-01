package com.example.medr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.medr.core.Constants
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
            saveInternalData()
            val i  = Intent(this@InfoActivity,MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun saveInternalData() {
        val shared = getSharedPreferences("firstTime", Context.MODE_PRIVATE)
        val edit = shared.edit()
        edit.apply{
            putBoolean(Constants.KEY_FIRST_TIME,true)
        }.apply()
    }
}