package com.example.feedback6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegistroActivity: AppCompatActivity() {
    private lateinit var btnAlta2: Button
    private lateinit var btnCancelar2: Button
    private lateinit var editmail2: EditText
    private lateinit var editpassword2: EditText
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnAlta2 = findViewById(R.id.btnAlta)
        btnCancelar2 = findViewById(R.id.btnCancelar2)
        editmail2 = findViewById(R.id.editMail2)
        editpassword2 = findViewById(R.id.editPassword2)
        editpassword2.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        btnAlta2.setOnClickListener {
            registro()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnCancelar2.setOnClickListener {
            finish()
        }
    }

    private fun registro() {
        val mail = editmail2.text.toString()
        val password = editpassword2.text.toString()

        if (mail.isNotEmpty() && password.isNotEmpty()) {
            db.collection("dbUsuarios")
                .whereEqualTo("mail", mail)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        val nuevoUsuario = Usuario(mail, password, false)
                        db.collection("dbUsuarios")
                            .add(nuevoUsuario)
                            .addOnSuccessListener {
                                Toast.makeText(this, "El usuario ${nuevoUsuario.mail} se ha registrado correctamente", Toast.LENGTH_SHORT).show()
                                LoginActivity.mail = mail

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                            }
                    } else {

                        Toast.makeText(this, "El mail ${mail} ya está registrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al comprobar el usuario", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Por favor ingresa un correo y contraseña válidos", Toast.LENGTH_SHORT).show()
        }
    }
}