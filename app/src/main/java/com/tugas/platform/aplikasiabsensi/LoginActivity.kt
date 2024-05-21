package com.tugas.platform.aplikasiabsensi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityLoginBinding
import com.tugas.platform.aplikasiabsensi.models.requests.LoginRequest
import com.tugas.platform.aplikasiabsensi.models.responses.LoginResponse
import com.tugas.platform.aplikasiabsensi.utils.SessionManager
import org.json.JSONObject
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

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            apiClient.getApiService(this).login(LoginRequest(username, password))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                        val loginResponse = response.body()

                        if (response.code() == 200 && loginResponse?.authToken != null) {
                            Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()

                            sessionManager.saveAuthToken(loginResponse.authToken)
                            sessionManager.saveUser(loginResponse.user)

                            val dialog: AlertDialog = builder.create()
                            dialog.show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            val errorMsg = JSONObject(response.errorBody()!!.string()).getString("error")
                            builder
                                .setMessage(errorMsg)
                                .setTitle("Login Gagal!")
                                .setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                }

                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }

                    override fun onFailure(p0: Call<LoginResponse>, p1: Throwable) {
                        Toast.makeText(this@LoginActivity, p1.toString(), Toast.LENGTH_LONG).show()
                    }

                })

            // for debugging on phone
//            if (sessionManager.getUser() == null) {
//                val dummy: User = User("0", "dummy", "dummy@mail.com", "dummy user", "dummy")
//                sessionManager.saveAuthToken("dummy")
//                sessionManager.saveUser(dummy)
//
//                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        }
    }
}