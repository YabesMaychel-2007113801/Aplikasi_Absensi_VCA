package com.tugas.platform.aplikasiabsensi.models

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("token")
    var authToken: String,

    @SerializedName("user")
    var user: User
)
