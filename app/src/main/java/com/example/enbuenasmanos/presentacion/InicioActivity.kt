package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.enbuenasmanos.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciarSesion.setOnClickListener()
        {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnRegistroCuidador.setOnClickListener()
        {
            var intent = Intent(this,RegistroUsuarioActivity::class.java)
            startActivity(intent)
        }
        binding.inicio.setOnClickListener() {
            hiddenIME(binding.root)
        }
    }
    fun hiddenIME(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}