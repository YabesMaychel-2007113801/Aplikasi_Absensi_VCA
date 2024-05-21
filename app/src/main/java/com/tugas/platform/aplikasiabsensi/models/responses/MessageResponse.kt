package com.tugas.platform.aplikasiabsensi.models.responses

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    var message: String
)
