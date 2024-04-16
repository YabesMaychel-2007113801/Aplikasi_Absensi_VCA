package com.tugas.platform.aplikasiabsensi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.tugas.platform.aplikasiabsensi.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnSelfie.isEnabled =true

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            binding.btnSelfie.isEnabled = true
        }

        val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val list = it.data
                val pic: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    list?.getParcelableExtra("data", Bitmap::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    list?.getParcelableExtra("data")
                }
                binding.imagePreview.setImageBitmap(pic)
            }
        }

        binding.btnSelfie.setOnClickListener {
            val c = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            previewRequest.launch(c)
        }



        binding.submitAbsen.setOnClickListener {
            val camIntent = Intent(this, RiwayatAbsensiActivity::class.java)
            startActivity(camIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            binding.btnSelfie.isEnabled = true
        }
    }
}