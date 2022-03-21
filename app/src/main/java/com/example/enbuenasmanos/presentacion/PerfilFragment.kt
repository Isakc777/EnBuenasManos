package com.example.enbuenasmanos.presentacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PerfilFragment : Fragment() {
    private lateinit var binding: FragmentPerfilBinding
    private val db = FirebaseFirestore.getInstance()
    private var cont:Int = 0

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

        //recupera los datos del usuario
        db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {
            binding.txtNombre.setText(it.get("nombre") as String?)
            binding.txtPass.setText(it.get("contraseña") as String?)
            binding.txtTelf.setText(it.get("telf") as String?)
            binding.txtTipoUs.setText(it.get("tipo_user") as String?)
        }
        binding.txtNombre.isEnabled = false
        binding.txtTelf.isEnabled = false

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
        binding.btnEditar.setOnClickListener {
            cont += 1
            cambioIcon(cont)
            if(cont % 2 == 0){
                db.collection("usuarios").document(email).set(
                    hashMapOf("provider" to provider,
                        "contraseña" to binding.txtPass.text.toString(),
                        "nombre" to binding.txtNombre.text.toString(),
                        "telf" to binding.txtTelf.text.toString(),
                        "tipo_user" to binding.txtTipoUs.text.toString())
                )
                Toast.makeText(activity, "Datos de Usuario editado", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun cambioIcon(cont:Int){
        if((cont % 2) !=0 ){
            binding.btnEditar.setImageResource(R.drawable.ic_save_24)
            binding.txtNombre.isEnabled = true
            binding.txtTelf.isEnabled = true
        } else {
            binding.btnEditar.setImageResource(R.drawable.ic_edit_24)
            binding.txtNombre.isEnabled = false
            binding.txtTelf.isEnabled = false
        }
    }
}