package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
//import com.example.enbuenasmanos.ProviderType
import com.example.enbuenasmanos.databinding.ActivityRegistroUsuarioBinding
import com.google.firebase.auth.FirebaseAuth

class RegistroUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExisteCuenta.setOnClickListener()
        {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        registrar()
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

    private fun registrar (){
        binding.btnRegistrar.setOnClickListener(){
            if (binding.txtEmailAddress.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty()){
                if(binding.txtPassword.text.toString() == binding.txtConfirmarPassword.text.toString()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.txtEmailAddress.text.toString(),
                        binding.txtPassword.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email?:"", ProviderType.BASIC)
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        }else{
                            showAlert()
                        }
                    }
                }else{
                    showAlert()
                }
            }
        }
    }

    private fun showHome(email:String,provider:ProviderType){
        var intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(intent)
    }

    private fun  showAlert (){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("La contraseñas no coinciden intente nuevamente")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =  builder.create()
        dialog.show()
    }
}