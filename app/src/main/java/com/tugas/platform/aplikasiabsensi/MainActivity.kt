package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tugas.platform.aplikasiabsensi.databinding.ActivityLoginBinding
import com.tugas.platform.aplikasiabsensi.databinding.ActivityMainBinding
import com.tugas.platform.aplikasiabsensi.models.User
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sessionManager = SessionManager(this)

        val user: User = sessionManager.getUser()
        val greet: String = "Halo, " + user.nama + "!"

        binding.greet.text = greet

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