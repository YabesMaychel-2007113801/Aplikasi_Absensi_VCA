package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class PasswordActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editpassword)

        val btnSimpan:Button=findViewById(R.id.btnsimpanpass)
        btnSimpan.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){
                R.id.btnsimpanpass -> {
                    val simpanIntent = Intent(this, login::class.java)
                    startActivity(simpanIntent)
                }
            }
        }
    }
}