package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.controladores.UsuarioController
import com.example.enbuenasmanos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //test
        binding.btnForget.setOnClickListener() {
            Toast.makeText(this, "Pantalla en construcción", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener()
        {
            val access = UsuarioController().LoginUser(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString()
            )
            if (access) {
                binding.emailField.error = getString(R.string.error)
            } else {
                binding.emailField.error = null
                var intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
        }
        binding.loginPrincipal.setOnClickListener() {
            hiddenIME(binding.root)
        }
    }
    fun hiddenIME(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}