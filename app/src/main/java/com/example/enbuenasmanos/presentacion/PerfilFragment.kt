package com.example.enbuenasmanos.presentacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.FragmentListarBinding
import com.example.enbuenasmanos.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        //setup
        val bundle:Bundle? = activity?.intent?.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        val password:String? = bundle?.getString("password")
        setup(email?:"",provider?:"",password?:"")

        //Guardado de datos
        val prefs = activity?.getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",
            AppCompatActivity.MODE_PRIVATE
        )?.edit()
        prefs?.putString("email",email)
        prefs?.putString("provider",provider)
        prefs?.apply()
    }

    private fun setup(email: String, provider:String, password:String){
        binding.txtemail.text = email
        binding.txtprovider.text = provider
        binding.txtPass.text = password
        binding.btnGuardar.setOnClickListener {
            db.collection("usuarios").document(email).set(
                hashMapOf("provider" to provider,
                    "contraseña" to binding.txtPass.text.toString(),
                    "nombre" to binding.txtNombre.text.toString())
            )
        }

    }
}