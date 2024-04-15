package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tugas.platform.aplikasiabsensi.databinding.ActivityAbsenBinding

class AbsenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbsenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnJammasuk.setOnClickListener {
            val masukIntent = Intent(this, ScanQrActivity::class.java)
            startActivity(masukIntent)
        }

        binding.btnJampulang.setOnClickListener {
            val pulangIntent = Intent(this, ScanQrActivity::class.java)
            startActivity(pulangIntent)
        }
    }
}