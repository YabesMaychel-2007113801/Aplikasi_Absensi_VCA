package com.tugas.platform.aplikasiabsensi.models.responses

import com.google.gson.annotations.SerializedName
import com.tugas.platform.aplikasiabsensi.models.User

data class LoginResponse (
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("token")
    var authToken: String,

    @SerializedName("user")
    var user: User,

    @SerializedName("error")
    var error: String,
)
