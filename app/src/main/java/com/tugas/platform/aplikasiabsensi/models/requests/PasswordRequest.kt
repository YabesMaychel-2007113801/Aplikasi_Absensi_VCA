package com.tugas.platform.aplikasiabsensi.models.requests

import com.google.gson.annotations.SerializedName

data class PasswordRequest(
    @SerializedName("oldpass")
    var oldpass: String,

    @SerializedName("newpass")
    var newpass: String
)
