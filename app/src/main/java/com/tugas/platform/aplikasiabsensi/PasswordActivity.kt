package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tugas.platform.aplikasiabsensi.databinding.ActivityEditpasswordBinding

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditpasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnsimpanpass.setOnClickListener {
            val simpanIntent = Intent(this, LoginActivity::class.java)
            startActivity(simpanIntent)
        }
    }
}