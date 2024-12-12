package com.example.feedback6

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Calendar
import java.util.Locale

class NuevaNovelaActivity: AppCompatActivity() {
    private lateinit var btnGuardarNovela: Button
    private lateinit var btnCancelar: Button
    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editAño: EditText
    private lateinit var editSinopsis: EditText
    private lateinit var editPais: EditText
    private val db: FirebaseFirestore = Firebase.firestore
    private lateinit var mMap: GoogleMap
    //creamos todas las variables necesarias para hacer la activity funcional

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_novela) //asignamos el correspodinete layput

        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editAño = findViewById(R.id.editAño)
        editSinopsis = findViewById(R.id.editSinopsis)
        editPais = findViewById(R.id.editPais)
        btnGuardarNovela = findViewById(R.id.btnGuardarNovela)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnGuardarNovela.setOnClickListener {
            guardarNovela()
            finish()
            //boton encargado de guardar una novela nueva
        }

        btnCancelar.setOnClickListener {
            finish()
            //vuelve a la actividad inicial
        }

        editAño.inputType = InputType.TYPE_NULL
        editAño.isFocusable = false

        editAño.setOnClickListener {
            mostrarCalendario()
        }

    }

    fun mostrarCalendario() {
        val dialog = AlertDialog.Builder(this)
        val numberPicker = NumberPicker(this)

        val añoActual = Calendar.getInstance().get(Calendar.YEAR)
        numberPicker.minValue = 1000
        numberPicker.maxValue = añoActual + 100
        numberPicker.value = añoActual

        dialog.setTitle("Selecciona un año")
        dialog.setView(numberPicker)

        dialog.setPositiveButton("Aceptar") { _, _ ->
            editAño.setText(numberPicker.value.toString())
        }

        dialog.setNegativeButton("Cancelar", null)
        dialog.show()
    }

    fun guardarNovela(){

        val titulo = editTitulo.text.toString()
        val autor = editAutor.text.toString()
        val año = editAño.text.toString().toInt()
        val sinopsis = editSinopsis.text.toString()
        val pais = editPais.text.toString()
        val posicion = calcularUbicacionDesdeCiudad(pais)
        if (posicion == null) {
            Toast.makeText(this, "No se pudo determinar la ubicación para $pais.", Toast.LENGTH_SHORT).show()
            return
        }
        val lat = posicion.latitude
        val lon = posicion.longitude
        val nuevaNovela = Novela(titulo, autor, año, sinopsis, false, pais, lat, lon)
        //creamos una nueva novela con sus correspondientes atributos

        db.collection("dbNovelas")
            .add(nuevaNovela)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Novela guardada: ${nuevaNovela.titulo}", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar la novela: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun calcularUbicacionDesdeCiudad(ciudad: String): LatLng? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val direcciones = geocoder.getFromLocationName(ciudad, 1)
            if (direcciones?.isNotEmpty() == true) {
                val location = direcciones?.get(0)
                location?.let { LatLng(it.latitude, location.longitude) }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}