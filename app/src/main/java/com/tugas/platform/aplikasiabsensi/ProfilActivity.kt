package com.tugas.platform.aplikasiabsensi

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityProfilBinding
import com.tugas.platform.aplikasiabsensi.models.User
import com.tugas.platform.aplikasiabsensi.models.responses.MessageResponse
import com.tugas.platform.aplikasiabsensi.utils.Constants
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var sessionManager: SessionManager

    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)
        val user: User? = sessionManager.getUser()

        binding.tvNama.text = user?.nama
        binding.tvUsername.text = user?.username
        binding.tvDepartemen.text = user?.mataPelajaran

        apiClient.getApiService(this).getPhoto()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val foto = response.body().toString()
                    Glide.with(this@ProfilActivity).load(Constants.PROFILE_IMG_URL+foto).signature(
                        ObjectKey(System.currentTimeMillis())
                    ).into(binding.profileImage)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@ProfilActivity, "Tidak dapat terhubung ke server!", Toast.LENGTH_LONG).show()
                }

            })


        binding.btnEditpass.setOnClickListener {
            val editIntent = Intent(this, PasswordActivity::class.java)
            startActivity(editIntent)
        }

        binding.profileImage.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@ProfilActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                val file = File(fileUri.path!!)

                val multipartImage: MultipartBody.Part?
                val requestFile: RequestBody = file
                    .asRequestBody("image/*".toMediaType())

                multipartImage = MultipartBody.Part.createFormData("foto", file.name, requestFile)

                apiClient.getApiService(this).changePhoto(multipartImage)
                    .enqueue(object: Callback<MessageResponse> {
                        override fun onResponse(
                            call: Call<MessageResponse>,
                            response: Response<MessageResponse>
                        ) {
                            recreate()
                        }

                        override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                            Toast.makeText(this@ProfilActivity, "Tidak dapat terhubung ke server!", Toast.LENGTH_LONG).show()
                        }

                    })

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}