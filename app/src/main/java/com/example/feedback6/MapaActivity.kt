package com.example.feedback6

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.databinding.ActivityMapaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapaActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaBinding
    private var currentMarker: Marker? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var btnVolver: Button
    private var pais: String = ""
    private var titulo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val latExtra = intent.getDoubleExtra("latitud", Double.NaN)
        val lonExtra = intent.getDoubleExtra("longitud", Double.NaN)
        val paisExtra = intent.getStringExtra("pais")
        val tituloExtra = intent.getStringExtra("titulo")

        if (!latExtra.isNaN() && !lonExtra.isNaN()) {
            lat = latExtra
            lon = lonExtra
            pais = paisExtra.toString()
            titulo = tituloExtra.toString()
        } else {
            lat = 40.4168
            lon = -3.7038
            pais = "Madrid"
            titulo = ""
        }

        btnVolver = findViewById(R.id.aceptar)
        btnVolver.setOnClickListener {
            finish()
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var madridLatLong = LatLng(lat, lon)
        var posicion2 = LatLng(40.48, -3.89)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(madridLatLong, 10f))

        currentMarker = mMap.addMarker(MarkerOptions().position(madridLatLong).title(pais + " - " + titulo))
        currentMarker = mMap.addMarker(MarkerOptions().position(posicion2).title(pais + " - " + titulo))

        binding.edittextLatitud.setText(madridLatLong.latitude.toString())
        binding.edittextLongitud.setText(madridLatLong.longitude.toString())

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnCameraIdleListener {
            val zoomLevel = mMap.cameraPosition.zoom
            binding.zoomLevelTextView.text = "Zoom Level: $zoomLevel"
        }

        mMap.setOnMapClickListener { latLng ->
            currentMarker?.remove()
            currentMarker = mMap.addMarker(MarkerOptions().position(latLng).title("Nueva Ubicación"))
            binding.edittextLatitud.setText(latLng.latitude.toString())
            binding.edittextLongitud.setText(latLng.longitude.toString())
        }
    }

}