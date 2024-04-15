package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityLoginBinding
import com.tugas.platform.aplikasiabsensi.models.requests.LoginRequest
import com.tugas.platform.aplikasiabsensi.models.responses.LoginResponse
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        binding.btnLogin.setOnClickListener {
            val username = binding.inputNama.text.toString()
            val password = binding.inputPassword.text.toString()
//            Toast.makeText(this, username, Toast.LENGTH_SHORT).show()

            apiClient.getApiService(this).login(LoginRequest(username, password))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                        val loginResponse = response.body()

                        if (response.code() == 200 && loginResponse?.authToken != null) {
                            Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                            sessionManager.saveAuthToken(loginResponse.authToken)
                            sessionManager.saveUser(loginResponse.user)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login gagal! (" + response.code().toString() + ")", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(p0: Call<LoginResponse>, p1: Throwable) {

                    }

                })
        }
    }
}