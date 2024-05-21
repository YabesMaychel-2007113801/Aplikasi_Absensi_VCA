package com.tugas.platform.aplikasiabsensi.api

import com.tugas.platform.aplikasiabsensi.models.Absen
import com.tugas.platform.aplikasiabsensi.models.requests.LoginRequest
import com.tugas.platform.aplikasiabsensi.models.requests.PasswordRequest
import com.tugas.platform.aplikasiabsensi.models.responses.AbsenResponse
import com.tugas.platform.aplikasiabsensi.models.responses.LoginResponse
import com.tugas.platform.aplikasiabsensi.models.responses.MessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {

    @POST("user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("settings/{key}")
    fun getSettings(@Path("key") key : String): Call<String>

    @POST("user/change_password")
    fun changePassword(@Body request: PasswordRequest): Call<MessageResponse>

    @Multipart
    @POST("user/absen")
    fun submitAbsen(
        @PartMap partMap: MutableMap<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<AbsenResponse>

    @GET("user/absen/last")
    fun lastAbsen(): Call<Absen>

    @GET("user/absen/all")
    fun allAbsen(): Call<List<Absen>>
}