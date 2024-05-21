package com.tugas.platform.aplikasiabsensi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityCameraBinding
import com.tugas.platform.aplikasiabsensi.models.User
import com.tugas.platform.aplikasiabsensi.models.responses.AbsenResponse
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var sessionManager: SessionManager

    private lateinit var apiClient: ApiClient


    private val tanggalFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id","ID"))
    private val waktuFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
    private var tanggal = tanggalFormat.format(Calendar.getInstance().time)


    private var waktu = waktuFormat.format(Calendar.getInstance().time)
    private var jenisAbsen: String? = null
    private lateinit var f: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        f = File(this.cacheDir, "absen.png")

        val user: User? = sessionManager.getUser()
        val userID = user?.id
        jenisAbsen = intent.getStringExtra("jenis")


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

                f.createNewFile()

                val bos = ByteArrayOutputStream()
                pic?.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)
                val bitmapdata: ByteArray = bos.toByteArray()

                //write the bytes in file
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(f)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos?.write(bitmapdata)
                    fos?.flush()
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                tanggal = tanggalFormat.format(Calendar.getInstance().time)
                waktu = waktuFormat.format(Calendar.getInstance().time)

                //  binding.imagePreview.setImageBitmap(pic)
                binding.imagePreview.setImageURI(Uri.fromFile(f))
                binding.tvDateTime.text = tanggal
            }
        }

        binding.btnSelfie.setOnClickListener {
            val c = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            c.putExtra("android.intent.extras.CAMERA_FACING", 1)
//            val outputUri: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".provider", output)
//            c.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            previewRequest.launch(c)
        }

        binding.submitAbsen.setOnClickListener {
            val map: MutableMap<String, RequestBody> = mutableMapOf()

            val inputUserID = createPartFromString(userID.toString())
            val inputJenis = createPartFromString(jenisAbsen.toString())
            val inputJam = createPartFromString(waktu)

            map.put("user_id", inputUserID)
            map.put("jenis", inputJenis)
            map.put("jam", inputJam)

            val multipartImage: MultipartBody.Part?
            val requestFile: RequestBody = f
                .asRequestBody("image/png".toMediaType())

            multipartImage = MultipartBody.Part.createFormData("foto", f.name, requestFile)

            apiClient.getApiService(this).submitAbsen(map, multipartImage)
                .enqueue(object: Callback<AbsenResponse> {
                    override fun onResponse(
                        call: Call<AbsenResponse>,
                        response: Response<AbsenResponse>
                    ) {
                        val camIntent = Intent(this@CameraActivity, RiwayatAbsensiActivity::class.java)
                        startActivity(camIntent)
                    }

                    override fun onFailure(call: Call<AbsenResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
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

    private fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}