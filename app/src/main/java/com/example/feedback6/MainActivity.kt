package com.example.feedback6

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var btnAlta: Button
    private lateinit var btnMapa: Button
    private lateinit var btnTema: ToggleButton
    private lateinit var btnConfig: ImageButton
    private lateinit var recyclerNovelas: RecyclerView
    private lateinit var novelasAdapter: NovelasAdapter
    private var listadoNovelasF: MutableList<Novela> = mutableListOf()
    private val db: FirebaseFirestore = Firebase.firestore
    //creamos todas las variables necesarias para hacer la activity funcional

    companion object {
        const val ACCION_VER = 1
        const val ACCION_BORRAR = 2
        const val ACCION_FAV = 3
        const val ACCION_XFAV = 4
        const val ACCION_MAPA = 5
    }
    //declaramos todas las variables necesarias para hacer la aplicación funcional

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //se establece la vista de la actividad

        btnAlta = findViewById(R.id.btnAlta)
        btnMapa = findViewById(R.id.btnAcercaDe)
        recyclerNovelas = findViewById(R.id.recyclerNovelas)
        btnTema = findViewById(R.id.btnTema)
        btnConfig = findViewById(R.id.btnAjustes)
        //asociamos a los botones el identificador del boton del layout

        btnTema.setChecked(LoginActivity.modoOscuro)

        btnTema.setOnClickListener {
            if (btnTema.isChecked()) {
                activarModo(true)
            } else {
                activarModo(false)
            }
        }

        btnAlta.setOnClickListener {
            val intent = Intent(this, NuevaNovelaActivity::class.java)
            startActivity(intent)
        } //boton que nos lleva a la actividad de alta de novelas

        btnMapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            /*
            val ubicaciones = arrayOf(
                Triple(40.4168, -3.7038, "Madrid"),
                Triple(40.486268319403564, -3.8984147831797604, "Las Rozas - Cien años de Soledad"),
                Triple(41.6511, -0.8892, "Valencia - La Vuelta al Mundo en 80 Días"),
            )
            intent.putExtra("ubicaciones", ubicaciones)

             */
            startActivity(intent)
            //boton que nos lleva a la actividaddel mapa
        }

        btnConfig.setOnClickListener {
            val intent = Intent(this, AjustesActivity::class.java)
            startActivity(intent)
        }

        mostrarNovelas()

    }

    private fun activarModo(modoOscuro: Boolean) {
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        delegate.applyDayNight()
        LoginActivity.modoOscuro = modoOscuro
        db.collection("dbUsuarios")
            .whereEqualTo("mail", LoginActivity.mail)
            .get()
            .addOnSuccessListener { documentos ->
                val id = documentos.documents[0].id
                db.collection("dbUsuarios")
                    .document(id)
                    .update("modo", modoOscuro)
            }
    }

    override fun onResume() {
        super.onResume()
        mostrarNovelas()
    }
    //creamos una función que haga que la lista se actualice al volver a la actividad

    private fun mostrarNovelas() {
        db.collection("dbNovelas")
            .get()
            .addOnSuccessListener { documentos ->
                listadoNovelasF.clear()
                for (documento in documentos) {
                    val novela = documento.toObject(Novela::class.java)
                    listadoNovelasF.add(novela)
                }
                prepararRecyclerView()
            }
            .addOnFailureListener({ exception ->
                Toast.makeText(this, "Error al obtener las novelas", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error al obtener las novelas de la base de datos", exception)
            }) //mandamos un error a la logcat y al usuario en el caso de que no se pueda obtener la lista de novelas de la base de datos
        //creamos un metodo que hace que muestre la lista de novelas de la base de datos y las añada a una lista de novelas para uqe se muestren en la principal
    }

    private fun prepararRecyclerView() {
        recyclerNovelas.layoutManager = LinearLayoutManager(this)
        //configuramos el recycler para que sea una lista vertical
        novelasAdapter = NovelasAdapter(listadoNovelasF) { novela, accion ->
            if (accion == ACCION_VER) {
                verNovela(novela)
            } else if (accion == ACCION_BORRAR) {
                borrarNovela(novela)
            } else if (accion == ACCION_FAV) {
                añadirFavorita(novela)
            } else if (accion == ACCION_XFAV){
                xFav(novela)
            } else if (accion == ACCION_MAPA){
                val intent = Intent(this, MapaActivity::class.java)
                intent.putExtra("latitud", novela.latitud)
                intent.putExtra("longitud", novela.longitud)
                intent.putExtra("titulo", novela.titulo)
                intent.putExtra("pais", novela.pais)
                startActivity(intent)
            }
            //hacemos que el metodo identifique si el usuario quiere borrar, ver la novela o añadir o borrar de favoritos y se ejecuta la accion elegida

        }
        recyclerNovelas.adapter = novelasAdapter //asignamos el recycler a la vista
        novelasAdapter.notifyDataSetChanged() //notificamos al adaptador que los datos han cambiado
    }

    private fun xFav(novela: Novela) {
        db.collection("dbNovelas")
            .whereEqualTo("titulo", novela.titulo)
            .get()
            .addOnSuccessListener { documentos ->
                for (documento in documentos) {
                    documento.reference.update("fav", false)
                }
                mostrarNovelas()
                Toast.makeText(this, "Novela eliminada de favoritos", Toast.LENGTH_SHORT).show()
            }
    }
    //metodo para borrar de favoritos la novela

    private fun verNovela(novela: Novela) {
        val intent = Intent(this, VerNovelaActivity::class.java)
        intent.putExtra("Titulo", novela.titulo)
        intent.putExtra("Autor", novela.autor)
        intent.putExtra("Año", novela.año)
        intent.putExtra("Sinopsis", novela.sinopsis)
        startActivity(intent)
        //mostramos todos los datos de la novela que el usuario ha elegido en una nueva pantalla
    }

    private fun borrarNovela(novela: Novela) {
        db.collection("dbNovelas")
            .whereEqualTo("titulo", novela.titulo)
            .get()
            .addOnSuccessListener { documentos ->
                for (documento in documentos) {
                    documento.reference.delete()
                }
                mostrarNovelas()
                Toast.makeText(this, "Novela eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al borrar la novela", Toast.LENGTH_SHORT).show()
            }
    }
    //con este metodo buscamos la novela que el usuario ha elegido y la borramos de la base de datos dandole a la vez un mensaje para que sepa que se ha eliminado correctamente

    private fun añadirFavorita(novela: Novela) {
        db.collection("dbNovelas")
            .whereEqualTo("titulo", novela.titulo)
            .get()
            .addOnSuccessListener { documentos ->
                for (documento in documentos) {
                    documento.reference.update("fav", true)
                }
                mostrarNovelas()
                Toast.makeText(this, "Novela añadida a favoritos", Toast.LENGTH_SHORT).show()
            }
    }
    //metodo para cambiar el atributo de la novela y añadirlo a favoritos
}


