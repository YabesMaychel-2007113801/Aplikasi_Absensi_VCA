package com.tugas.platform.aplikasiabsensi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.service.voice.VoiceInteractionSession.ActivityId
import androidx.core.app.ActivityCompat
import com.tugas.platform.aplikasiabsensi.databinding.ActivityCameraBinding
import kotlinx.android.synthetic.main.activity_camera.btnSelfie
import kotlinx.android.synthetic.main.activity_camera.imagePreview

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        btnSelfie.isEnabled =true;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            btnSelfie.isEnabled = true;
        }

        btnSelfie.setOnClickListener {
            val c = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(c, 101)
        }



        binding.submitAbsen.setOnClickListener {
            val camIntent = Intent(this, RiwayatAbsensiActivity::class.java)
            startActivity(camIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101){
            var pic : Bitmap? = data?.getParcelableExtra<Bitmap>("data")
            imagePreview.setImageBitmap(pic)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            btnSelfie.isEnabled = true
        }
    }
}