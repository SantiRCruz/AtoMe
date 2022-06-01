package com.example.medr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.medr.core.Constants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.supportActionBar?.hide()
        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                if (loadData()) {
                    val i = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    val i = Intent(this@SplashActivity, InfoActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }.start()
    }

    private fun loadData(): Boolean {
        val shared = getSharedPreferences("firstTime", Context.MODE_PRIVATE)
        return shared.getBoolean(Constants.KEY_FIRST_TIME, false)
    }
}