package com.example.feedback6

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AjustesActivity:AppCompatActivity() {
    private lateinit var btnMain: Button
    private lateinit var btnCerrar: Button
    private lateinit var textoInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        btnMain = findViewById(R.id.btnMain)
        btnCerrar = findViewById(R.id.btnCerrarSesion)
        textoInfo = findViewById(R.id.textMailInfo)
        SplashActivity.sesion = false

        val mail = LoginActivity.mail
        textoInfo.setText(mail).toString()

        btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnCerrar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí") { _, _ ->
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()

        }
    }
}