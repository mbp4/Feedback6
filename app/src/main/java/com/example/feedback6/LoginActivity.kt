package com.example.feedback6

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.feedback6.SplashActivity.Companion.sesion
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class LoginActivity: AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    companion object{
        var modoOscuro: Boolean = false
        var mail: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mail = findViewById<EditText>(R.id.editMail)
        val password = findViewById<EditText>(R.id.editPassword)
        password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        val registro = findViewById<Button>(R.id.btnRegistro)

        val loginButton = findViewById<Button>(R.id.btnIniciar)
        loginButton.setOnClickListener {
            hacerLogin(mail.text.toString(), password.text.toString())
        }

        registro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }


        when (sesion) {
            true -> {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                SplashActivity.sesion = false
            }

            false -> {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun hacerLogin(mail: String, password: String) {
        if (mail.isNotEmpty() && password.isNotEmpty()) {
            db.collection("dbUsuarios")
                .whereEqualTo("mail", mail)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    } else {
                        LoginActivity.modoOscuro = documents.documents[0].get("modo") as Boolean
                        if (modoOscuro is Boolean) {
                            LoginActivity.modoOscuro = modoOscuro
                        } else {
                            LoginActivity.modoOscuro = false // Valor por defecto si el campo no es un Boolean o es null
                        }
                        activarModo(LoginActivity.modoOscuro)
                        LoginActivity.mail = mail
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Por favor, ingrese un correo electrónico y una contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun activarModo(modoOscuro: Boolean) {
        if (modoOscuro) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        delegate.applyDayNight()
    }
}