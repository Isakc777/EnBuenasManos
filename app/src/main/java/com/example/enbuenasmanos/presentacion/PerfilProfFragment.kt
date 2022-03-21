package com.example.enbuenasmanos.presentacion

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.FragmentPerfilBinding
import com.example.enbuenasmanos.databinding.FragmentPerfilProfBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class PerfilProfFragment : Fragment() {

    private lateinit var binding: FragmentPerfilProfBinding
    private val db = FirebaseFirestore.getInstance()
    private val db2 = FirebaseFirestore.getInstance()
    private var cont:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilProfBinding.inflate(inflater, container, false)
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

        }
        db2.collection("profesional").document(email.toString()).get().addOnSuccessListener {
            binding.txtCargo.setText(it.get("cargo") as String?)
            binding.txtTipoUs.setText(it.get("img") as String?)
            binding.txtDescProf.setText(it.get("descripcion") as String?)
            var url:String = binding.txtTipoUs.text.toString()
            Log.d("myTag",url)
            Picasso.get().load(url).into(binding.imageViewProf)
        }


        binding.txtNombre.isEnabled = false
        binding.txtTelf.isEnabled = false
        binding.txtprovider.isEnabled = false
        binding.txtPass.isEnabled = false
        binding.txtemail.isEnabled = false
        binding.txtTipoUs.isEnabled = false
        binding.txtTipoUs.isVisible = false
        binding.txtCargo.isEnabled = false
        binding.txtDescProf.isEnabled = false

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
        binding.txtemail.setText(email)
        binding.txtprovider.setText(provider)
        binding.txtPass.setText(password)
        binding.btnEditar.setOnClickListener {
            cont += 1
            cambioIcon(cont)
            if(cont % 2 == 0){
                db.collection("usuarios").document(email).set(
                    hashMapOf("provider" to provider,
                        "contraseña" to binding.txtPass.text.toString(),
                        "nombre" to binding.txtNombre.text.toString(),
                        "telf" to binding.txtTelf.text.toString(),
                        "tipo_user" to "profesional")
                )
                db2.collection("profesional").document(email).set(
                    hashMapOf("provider" to provider,
                        "contraseña" to binding.txtPass.text.toString(),
                        "nombre" to binding.txtNombre.text.toString(),
                        "telf" to binding.txtTelf.text.toString(),
                        "tipo_user" to "profesional",
                        "cargo" to binding.txtCargo.text.toString(),
                        "descripcion" to binding.txtDescProf.text.toString(),
                        "img" to binding.txtTipoUs.text.toString()
                    )
                )
            }
        }

    }

    private fun cambioIcon(cont:Int){
        if((cont % 2) !=0 ){
            binding.btnEditar.setImageResource(R.drawable.ic_save_24)
            binding.txtNombre.isEnabled = true
            binding.txtTelf.isEnabled = true
            binding.txtCargo.isEnabled = true
            binding.txtDescProf.isEnabled = true
        } else {
            binding.btnEditar.setImageResource(R.drawable.ic_edit_24)
            binding.txtNombre.isEnabled = false
            binding.txtTelf.isEnabled = false
            binding.txtprovider.isEnabled = false
            binding.txtPass.isEnabled = false
            binding.txtemail.isEnabled = false
            binding.txtTipoUs.isEnabled = false
            binding.txtCargo.isEnabled = false
            binding.txtDescProf.isEnabled = false
        }
    }
}