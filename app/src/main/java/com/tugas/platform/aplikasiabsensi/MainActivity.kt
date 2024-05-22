package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.Manifest
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityMainBinding
import com.tugas.platform.aplikasiabsensi.models.Absen
import com.tugas.platform.aplikasiabsensi.models.User
import com.tugas.platform.aplikasiabsensi.utils.Constants
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            askLocationPermission()
        }

        sessionManager = SessionManager(this)

        val user: User? = sessionManager.getUser()
        val greet: String = "Halo, " + user?.nama + "!"

        binding.greet.text = greet

        apiClient.getApiService(this).getPhoto()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val foto = response.body().toString()
//                    Toast.makeText(this@MainActivity, foto, Toast.LENGTH_SHORT).show()
                    Glide.with(this@MainActivity).load(Constants.PROFILE_IMG_URL+foto).signature(
                        ObjectKey(System.currentTimeMillis())
                    ).into(binding.profileImage)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        apiClient.getApiService(this).lastAbsen()
            .enqueue(object : Callback<Absen> {
                override fun onResponse(call: Call<Absen>, response: Response<Absen>) {
                    if (response.body() != null && response.body()!!.jam != null) {
                        val tanggalFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id","ID"))
                        val jamFormat = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
                        val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
                        binding.tvJenis.text = response.body()!!.jenis
                        binding.tvTanggal.text = sourceFormat.parse(response.body()!!.jam)
                            ?.let { tanggalFormat.format(it) }
                        binding.tvWaktu.text = sourceFormat.parse(response.body()!!.jam)
                            ?.let { jamFormat.format(it) }
                        binding.tvLokasi.text = response.body()!!.lokasi

                        Glide.with(this@MainActivity).load(Constants.ABSEN_IMG_URL+response.body()!!.foto).into(binding.ivAbsen)
                    }
                }

                override fun onFailure(call: Call<Absen>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Tidak dapat terhubung ke server!", Toast.LENGTH_LONG).show()
                    finish()
                }

            })

        binding.absenHome.setOnClickListener {
            val mainIntent = Intent(this, AbsenActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        binding.riwayatHome.setOnClickListener {
            val riwayatIntent = Intent(this, RiwayatAbsensiActivity::class.java)
            startActivity(riwayatIntent)
            finish()
        }

        binding.profileImage.setOnClickListener {
            val profilIntent = Intent(this, ProfilActivity::class.java)
            startActivity(profilIntent)
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    // Ask Location Permission
    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 3 // random unique value
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 4

    private fun authorizedLocation(): Boolean {
        val formalizeForeground = (
                PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) &&
                        PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ))
        val formalizeBackground =
            if (gadgetQ) {
                PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                true
            }
        return formalizeForeground && formalizeBackground
    }

    private fun askLocationPermission() {
        if (authorizedLocation())
            return
        var grantingPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val customResult = when {
            gadgetQ -> {
                grantingPermission += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        Log.d(TAG, "askLocationPermission")

        ActivityCompat.requestPermissions(
            this, grantingPermission, customResult
        )
    }
}