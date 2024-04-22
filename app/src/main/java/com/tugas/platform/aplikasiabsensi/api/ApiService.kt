package com.tugas.platform.aplikasiabsensi.api

import com.tugas.platform.aplikasiabsensi.utils.Constants
import com.tugas.platform.aplikasiabsensi.models.requests.LoginRequest
import com.tugas.platform.aplikasiabsensi.models.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("settings/{key}")
    fun getSettings(@Path("key") key : String): Call<String>
}