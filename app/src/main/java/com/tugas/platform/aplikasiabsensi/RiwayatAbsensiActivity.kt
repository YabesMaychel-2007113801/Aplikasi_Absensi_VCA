package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.tugas.platform.aplikasiabsensi.adapters.AbsensAdapter
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityRiwayatAbsensiBinding
import com.tugas.platform.aplikasiabsensi.models.Absen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatAbsensiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatAbsensiBinding
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatAbsensiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()

        apiClient.getApiService(this).allAbsen()
            .enqueue(object: Callback<List<Absen>> {
                override fun onResponse(call: Call<List<Absen>>, response: Response<List<Absen>>) {
                    binding.rvAbsen.adapter = AbsensAdapter(response.body(), this@RiwayatAbsensiActivity)
                }

                override fun onFailure(call: Call<List<Absen>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        binding.fab.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@RiwayatAbsensiActivity, MainActivity::class.java))
                finish()
            }
        })
    }
}