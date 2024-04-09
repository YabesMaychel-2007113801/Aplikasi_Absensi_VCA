package com.tugas.platform.aplikasiabsensi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tugas.platform.aplikasiabsensi.databinding.ActivityLoginBinding
import com.tugas.platform.aplikasiabsensi.databinding.ActivityMainBinding
import com.tugas.platform.aplikasiabsensi.models.User
import com.tugas.platform.aplikasiabsensi.utils.SessionManager

class MainActivity : AppCompatActivity() {

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

    }
}