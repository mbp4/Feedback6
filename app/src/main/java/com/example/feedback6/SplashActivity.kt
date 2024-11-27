package com.example.feedback6

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity(){
    private lateinit var progreso: ProgressBar
    private lateinit var imagen: ImageView
    private lateinit var texto: TextView

    companion object{
        var sesion: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imagen = findViewById(R.id.imageView)
        progreso = findViewById(R.id.progreso)
        texto = findViewById(R.id.textoSesion)

        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 3000
            fillAfter = true
        }
        imagen.startAnimation(fadeOut)

        Handler(Looper.getMainLooper()).postDelayed({
            progreso.visibility = View.GONE
            SplashActivity.sesion = true

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3 segundos

    }
}