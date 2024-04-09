package com.tugas.platform.aplikasiabsensi.api

import com.tugas.platform.aplikasiabsensi.utils.Constants
import com.tugas.platform.aplikasiabsensi.models.requests.LoginRequest
import com.tugas.platform.aplikasiabsensi.models.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}