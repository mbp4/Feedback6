package com.example.feedback6

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.feedback6.databinding.ActivityMapaBinding
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapaActivity: AppCompatActivity(){
    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapaBinding
    private lateinit var btnVolver: Button
    private var currentMarker: Marker? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var pais: String = ""
    private var titulo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, applicationContext.getSharedPreferences("osm_prefs", MODE_PRIVATE))
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
        btnVolver.setOnClickListener { finish() }

        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)

        val startPoint = GeoPoint(lat, lon)
        mapView.controller.setZoom(10.0)
        mapView.controller.setCenter(startPoint)

        addMarker(startPoint, "$pais - $titulo")

        mapView.setOnClickListener { view ->
            val geoPoint = mapView.mapCenter
            addMarker(geoPoint, "Nueva Ubicación")
            binding.edittextLatitud.setText(geoPoint.latitude.toString())
            binding.edittextLongitud.setText(geoPoint.longitude.toString())
        }
    }

    private fun addMarker(location: IGeoPoint, title: String) {
        currentMarker?.let { mapView.overlays.remove(it) }
        currentMarker = Marker(mapView).apply {
            position = location as GeoPoint?
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            this.title = title
        }
        mapView.overlays.add(currentMarker)
        mapView.invalidate()
    }

}