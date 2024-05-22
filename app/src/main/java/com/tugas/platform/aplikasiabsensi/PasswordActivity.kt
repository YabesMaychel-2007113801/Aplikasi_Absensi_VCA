package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityEditpasswordBinding
import com.tugas.platform.aplikasiabsensi.models.requests.PasswordRequest
import com.tugas.platform.aplikasiabsensi.models.responses.MessageResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditpasswordBinding
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditpasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()

        binding.btnsimpanpass.setOnClickListener {
            val oldpass = binding.etOldpass.text.toString()
            val newpass = binding.etNewpass.text.toString()

            val passreq = PasswordRequest(oldpass, newpass)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            apiClient.getApiService(this).changePassword(passreq)
                .enqueue(object: Callback<MessageResponse> {
                    override fun onResponse(
                        call: Call<MessageResponse>,
                        response: Response<MessageResponse>
                    ) {
                        if (response.code() == 200) {
                            builder
                                .setMessage("Password berhasil diganti!")
                                .setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                    val intent = Intent(this@PasswordActivity, ProfilActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                        else {
                            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("error")
                            builder
                                .setMessage(errorMsg)
                                .setTitle("Gagal Mengubah Password!")
                                .setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                }

                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                        Toast.makeText(this@PasswordActivity, "Tidak dapat terhubung ke server!", Toast.LENGTH_LONG).show()
                        finish()
                    }

                })
        }
    }
}