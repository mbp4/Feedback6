package com.example.feedback6

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class NuevaNovelaActivity: AppCompatActivity() {
    private lateinit var btnGuardarNovela: Button
    private lateinit var btnCancelar: Button
    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editAño: EditText
    private lateinit var editSinopsis: EditText
    private val db: FirebaseFirestore = Firebase.firestore
    //creamos todas las variables necesarias para hacer la activity funcional

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_novela) //asignamos el correspodinete layput

        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editAño = findViewById(R.id.editAño)
        editSinopsis = findViewById(R.id.editSinopsis)
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

    }

    fun guardarNovela(){

        val titulo = editTitulo.text.toString()
        val autor = editAutor.text.toString()
        val año = editAño.text.toString().toInt()
        val sinopsis = editSinopsis.text.toString()
        val nuevaNovela = Novela(titulo, autor, año, sinopsis, false)
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
    //creamos un metodo que permita al usuario guardar la novela en la base de datos y que se le notifique de su acción
}