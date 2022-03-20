package com.example.enbuenasmanos.presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.ActivityPrincipalBinding
import com.example.enbuenasmanos.databinding.ActivityTipousuarioBinding
import com.google.firebase.firestore.FirebaseFirestore

class TipousuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipousuarioBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipousuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle:Bundle? = intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        val password:String? = bundle?.getString("password")

        //recupera los datos del usuario
        db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {
            binding.txtTipoUsuario.setText(it.get("tipo_user") as String?)
            binding.textView7.setText("Bienvenido "+ it.get("nombre") as String?)
            binding.textView11.setText("Hemos detectado que eres un usuario")
        }

        //Guardado de datos
        val prefs = this.getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.putString("contraseña", password)
        prefs.apply()


        binding.btnSiguiente.setOnClickListener {
            if(binding.txtTipoUsuario.text.toString() == "normal"){
                var intent = Intent(this, PrincipalActivity::class.java).apply {
                    putExtra("email",email)
                    putExtra("contraseña", password)
                    putExtra("provider", ProviderType.BASIC)
                }
                startActivity(intent)
            }
            if(binding.txtTipoUsuario.text.toString() == "profesional"){
                var intent = Intent(this, ProfesionalActivity::class.java).apply {
                    putExtra("email",email)
                    putExtra("contraseña", password)
                    putExtra("provider", ProviderType.BASIC)
                }
                startActivity(intent)
            }
        }
    }
}