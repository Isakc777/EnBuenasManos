package com.example.enbuenasmanos.presentacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.ActivityPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType{
    BASIC,
    GOOGLE
}

class PrincipalActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //metodo setup
        val bundle:Bundle? =intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        val password:String? = bundle?.getString("password")
        setup(email?:"",provider?:"",password?:"")

        //Guardado de datos
        val prefs = getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }
    private fun setup(email: String, provider:String, password:String){
        binding.topAppBar.setTitle(email)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exit -> {
                    //Borrado de Datos
                    val prefs = getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE).edit()
                    prefs.clear()
                    prefs.apply()
                    FirebaseAuth.getInstance().signOut()
                    onBackPressed()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itComunidad -> {
                    true
                }
                R.id.itConsejos -> {
                    true
                }
                R.id.itPerfil -> {
                    true
                }
                R.id.itAyudaProfesional -> {
                    true
                }
                else -> false
            }
        }

    }
}