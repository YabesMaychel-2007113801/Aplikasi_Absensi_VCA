package com.tugas.platform.aplikasiabsensi.models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    var id: String,

    @SerializedName("username")
    var username: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("nama")
    var nama: String,

    @SerializedName("mata_pelajaran")
    var mataPelajaran: String
)