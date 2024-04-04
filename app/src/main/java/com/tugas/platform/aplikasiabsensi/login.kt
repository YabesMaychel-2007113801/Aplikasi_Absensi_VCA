package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class login : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnClick: Button = findViewById(R.id.btnLogin)
        btnClick.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){
                R.id.btnLogin -> {
                    val pindahIntent = Intent(this, MainActivity::class.java)
                    startActivity(pindahIntent)
                }
            }
        }
    }
}