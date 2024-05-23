package com.tugas.platform.aplikasiabsensi

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.maps.android.SphericalUtil
import com.tugas.platform.aplikasiabsensi.api.ApiClient
import com.tugas.platform.aplikasiabsensi.databinding.ActivityScanQrBinding
import net.lsafer.bcrypt.BCryptKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanQrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanQrBinding
    private lateinit var codeScanner: CodeScanner

    private lateinit var apiClient: ApiClient
    private lateinit var qrPassword: String

    private var isInArea: Boolean = false

    companion object {
        private const val GEOFENCE_LAT = 0.5086603359302369
        private const val GEOFENCE_LONG = 101.44668177634597
        private const val GEOFENCE_RADIUS = 25.00
    }

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val jenisAbsen = intent.getStringExtra("jenis")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askLocationPermission()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        currentLocation = Location(null)

        apiClient = ApiClient()

        apiClient.getApiService(this).getSettings("qr_password")
            .enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    qrPassword = response.body().toString()
//                    Toast.makeText(this@ScanQrActivity, qrPassword, Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@ScanQrActivity, "Tidak dapat terhubung ke server!", Toast.LENGTH_LONG).show()
                    finish()
                }
            })

        val scannerView = binding.scannerView

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback { qr ->
            runOnUiThread {
                val task: Task<Location> = fusedLocationProviderClient.lastLocation
                Log.d(ContentValues.TAG, "getLastLocation: lastLocation")
                task.addOnSuccessListener { loc ->
                    if (loc != null) {
                        currentLocation = loc
                        Log.d(ContentValues.TAG, "getLastLocation: success")
                        isInArea = checkForGeoFenceEntry(currentLocation, GEOFENCE_LAT, GEOFENCE_LONG, GEOFENCE_RADIUS)
                    }

                    if (!BCryptKt.verify(qrPassword, qr.text)) {
                        builder
                            .setMessage("Absensi tidak dapat dilakukan!")
                            .setTitle("QR Code yang discan tidak sesuai!")
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                                val intent = Intent(this@ScanQrActivity, MainActivity::class.java)
                                startActivity(intent)
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }

                    if (BCryptKt.verify(qrPassword, qr.text) && isInArea) {
                        val intent = Intent(this@ScanQrActivity, CameraActivity::class.java)
                        intent.putExtra("jenis", jenisAbsen)
                        intent.putExtra("latitude", currentLocation.latitude)
                        intent.putExtra("longitude", currentLocation.longitude)
                        startActivity(intent)
                        finish()
                    }

                    if (!isInArea) {
                        builder
                            .setMessage("Absensi tidak dapat dilakukan!")
                            .setTitle("Anda tidak berada di Area Sekolah!")
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                                val intent = Intent(this@ScanQrActivity, MainActivity::class.java)
                                startActivity(intent)
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@ScanQrActivity, AbsenActivity::class.java))
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun getLastLocation() {
        Log.d(ContentValues.TAG, "getLastLocation: onProcess")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askLocationPermission()
        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        Log.d(ContentValues.TAG, "getLastLocation: lastLocation")
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                Log.d(ContentValues.TAG, "getLastLocation: success")
                isInArea = checkForGeoFenceEntry(currentLocation, GEOFENCE_LAT, GEOFENCE_LONG, GEOFENCE_RADIUS)
            }
        }
    }

    private fun checkForGeoFenceEntry(userLocation: Location, geofenceLat: Double, geofenceLong: Double, radius: Double) : Boolean {
        val startLatLng = LatLng(userLocation.latitude, userLocation.longitude)
        val geofenceLatLng = LatLng(geofenceLat, geofenceLong)

        val distanceInMeters = SphericalUtil.computeDistanceBetween(startLatLng, geofenceLatLng)

        if (distanceInMeters < radius) {
            // User is inside the Geo-fence
            return true
        } else {
            return false
        }
    }

    // Ask Location Permission
    private val gadgetQ = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 3 // random unique value
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 4

    private fun authorizedLocation(): Boolean {
        val formalizeForeground = (
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) &&
                        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ))
        val formalizeBackground =
            if (gadgetQ) {
                PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                true
            }
        return formalizeForeground && formalizeBackground
    }

    private fun askLocationPermission() {
        if (authorizedLocation())
            return
        var grantingPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val customResult = when {
            gadgetQ -> {
                grantingPermission += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        Log.d(ContentValues.TAG, "askLocationPermission")

        ActivityCompat.requestPermissions(
            this, grantingPermission, customResult
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        } else {
            Toast.makeText(this, "Location Permission is denied, please allow the permisson", Toast.LENGTH_SHORT).show()
        }
    }
}