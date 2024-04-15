package com.tugas.platform.aplikasiabsensi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tugas.platform.aplikasiabsensi.databinding.ActivityRiwayatAbsensiBinding

class RiwayatAbsensiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatAbsensiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatAbsensiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}