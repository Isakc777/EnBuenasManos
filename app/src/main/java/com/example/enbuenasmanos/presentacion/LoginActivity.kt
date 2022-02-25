
package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.enbuenasmanos.MainActivity
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.controladores.UsuarioController
import com.example.enbuenasmanos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener()
        {
            val access = UsuarioController().LoginUser(
                binding.txtEmailAddress.text.toString(),
                binding.txtPassword.text.toString()
            )
            if (access) {
                binding.txtEmailAddress.error = getString(R.string.error)
            } else {
                binding.txtEmailAddress.error = null
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding.login.setOnClickListener() {
            hiddenIME(binding.root)
        }
    }

    //Metodo para dar clic y ocultar el teclado
    fun hiddenIME(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }



}
