package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNgabsen: Button = findViewById(R.id.absen_home)
        val btnRiwayat: Button = findViewById(R.id.riwayat_home)
        btnNgabsen.setOnClickListener(this)
        btnRiwayat.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.absen_home -> {
                    val mainIntent = Intent(this, absen::class.java)
                    startActivity(mainIntent)
                }
                R.id.riwayat_home -> {
                    val riwayatIntent = Intent(this, riwayatAbsensi::class.java)
                    startActivity(riwayatIntent)
                }
            }
        }
    }
}