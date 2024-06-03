package com.tugas.platform.aplikasiabsensi.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugas.platform.aplikasiabsensi.R
import com.tugas.platform.aplikasiabsensi.databinding.ItemAbsenBinding
import com.tugas.platform.aplikasiabsensi.models.Absen
import com.tugas.platform.aplikasiabsensi.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AbsensAdapter(val data: List<Absen>?, private val context: Context) : RecyclerView.Adapter<AbsensAdapter.MyHolder>() {
    private val tanggalFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id","ID"))
    private val jamFormat = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
    private val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemAbsenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding.root)
    }
    override fun getItemCount(): Int = data?.size ?: 0
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder) {
            binding.tvJenis.text = data?.get(position)?.jenis
            binding.tvTanggal.text = data?.get(position)?.jam?.let { it -> sourceFormat.parse(it)?.let { tanggalFormat.format(it) } }
            binding.tvJam.text = data?.get(position)?.jam?.let { it -> sourceFormat.parse(it)?.let { jamFormat.format(it) } }
            binding.tvLokasi.text = data?.get(position)?.lokasi
            Glide.with(context).load(Constants.ABSEN_IMG_URL+data?.get(position)?.foto).into(binding.ivAbsen)

            val time: Date? = data?.get(position)?.jam?.let { it -> sourceFormat.parse(it)?.let { jamFormat.format(it) } }
                ?.let { it1 -> jamFormat.parse(it1) }

            val waktuMasuk: Date? = jamFormat.parse("07:30:00")
            val waktuPulang: Date? = jamFormat.parse("15:30:00")

            if (time != null) {
                if (data?.get(position)?.jenis == "Absen Masuk") {
                    if (time.compareTo(waktuMasuk) == 0 || time.compareTo(waktuMasuk) < 0) {
                        binding.icStatus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                    } else {
                        binding.icStatus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))

                    }
                }

                if (data?.get(position)?.jenis == "Absen Pulang") {
                    if (time.compareTo(waktuPulang) == 0 || time.compareTo(waktuPulang) > 0) {
                        binding.icStatus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                    } else {
                        binding.icStatus.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey))
                    }
                }
            }
        }
    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemAbsenBinding.bind(view)
    }
}