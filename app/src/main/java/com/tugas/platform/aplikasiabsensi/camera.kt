package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class camera : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val btnKamera: Button = findViewById(R.id.submitAbsen)
        btnKamera.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){
                R.id.submitAbsen -> {
                    val camIntent = Intent(this, riwayatAbsensi::class.java)
                    startActivity(camIntent)
                }
            }
        }

    }
}