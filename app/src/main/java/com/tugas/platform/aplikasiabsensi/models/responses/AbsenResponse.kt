package com.tugas.platform.aplikasiabsensi.models.responses

import com.google.gson.annotations.SerializedName
import com.tugas.platform.aplikasiabsensi.models.Absen

data class AbsenResponse(
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("data")
    var absen: Absen,

    @SerializedName("message")
    var message: String,

    @SerializedName("error")
    var error: String,
)
