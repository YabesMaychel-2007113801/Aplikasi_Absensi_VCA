package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class absen : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        val btnAbsenDatang: Button = findViewById(R.id.btn_jammasuk)
        val btnAbsenPulang: Button = findViewById(R.id.btn_jampulang)
        btnAbsenDatang.setOnClickListener(this)
        btnAbsenPulang.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_jammasuk -> {
                    val masukIntent = Intent(this, scanQR::class.java)
                    startActivity(masukIntent)
                }
                R.id.btn_jampulang -> {
                    val pulangIntent = Intent(this, scanQR::class.java)
                    startActivity(pulangIntent)
                }
            }
        }
    }
}