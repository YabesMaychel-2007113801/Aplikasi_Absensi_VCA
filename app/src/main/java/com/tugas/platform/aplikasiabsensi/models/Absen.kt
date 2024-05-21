package com.tugas.platform.aplikasiabsensi.models

import com.google.gson.annotations.SerializedName

data class Absen(
    @SerializedName("id")
    var id: String,

    @SerializedName("user_id")
    var user_id: String,

    @SerializedName("jenis")
    var jenis: String,

    @SerializedName("foto")
    var foto: String,

    @SerializedName("lokasi")
    var lokasi: String,

    @SerializedName("jam")
    var jam: String,
)
