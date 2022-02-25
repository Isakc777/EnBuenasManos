package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.enbuenasmanos.databinding.ActivityRegistroUsuarioBinding

class RegistroUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsuarioBinding
    //val app by lazy { applicationContext as UsuarioApp }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExisteCuenta.setOnClickListener()
        {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.registroUsuario.setOnClickListener() {
            hiddenIME(binding.root)
        }
    }

    //Metodo para dar clic y ocultar el teclado
    fun hiddenIME(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*private fun saveFavNews(usuario: Usuario?) {
        if (usuario != null) {
                lifecycleScope.launch {
                    UsuarioController().saveUsuario(usuario)
                }
            Toast.makeText(this, "registrado", Toast.LENGTH_SHORT).show()
        }
    }*/
}