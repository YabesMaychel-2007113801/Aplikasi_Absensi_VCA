package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()

        handler.postDelayed(
            {
                val x= Intent(this@splash, login::class.java)
                startActivity(x)
                finish()
            },3000
        )



    }
}