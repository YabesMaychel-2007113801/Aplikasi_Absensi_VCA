package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.tugas.platform.aplikasiabsensi.databinding.ActivityAbsenBinding

class AbsenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbsenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.linlayAbsenmasuk.setOnClickListener {
            val masukIntent = Intent(this, ScanQrActivity::class.java)
            masukIntent.putExtra("jenis", "Absen Masuk")
            startActivity(masukIntent)
        }

        binding.linlayAbsenpulang.setOnClickListener {
            val pulangIntent = Intent(this, ScanQrActivity::class.java)
            pulangIntent.putExtra("jenis", "Absen Pulang")
            startActivity(pulangIntent)
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@AbsenActivity, MainActivity::class.java))
                finish()
            }
        })
    }
}