
package com.example.enbuenasmanos.presentacion

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.example.enbuenasmanos.MainActivity
//import com.example.enbuenasmanos.ProviderType
import com.example.enbuenasmanos.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validar()
        sesion()

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

    private fun validar(){
        binding.btnLogin.setOnClickListener{
            if (binding.txtEmailAddress.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.txtEmailAddress.text.toString(),
                    binding.txtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email?:"", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }

            }
        }
    }

    //metodo para probar si existe una sesión activa
    private fun sesion(){
        val prefs = getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE)
        val email= prefs.getString("email",null)
        val provider = prefs.getString("provider",null)
        val password = prefs.getString("password",null)
        if(email!=null && provider!=null){
            //binding.login.visibility = View.INVISIBLE
            showHome(email,ProviderType.valueOf(provider))
        }

    }

    private fun  showAlert (){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al Usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =  builder.create()
        dialog.show()
    }

    private fun showHome(email:String,provider:ProviderType){
        val password:String = binding.txtPassword.text.toString()
        var intent = Intent(this, PrincipalActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
            putExtra("password",password)
        }
        startActivity(intent)
    }



}
