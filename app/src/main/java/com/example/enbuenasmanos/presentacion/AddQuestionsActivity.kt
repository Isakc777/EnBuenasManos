package com.example.enbuenasmanos.presentacion

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.enbuenasmanos.databinding.ActivityAddQuestionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddQuestionsActivity : AppCompatActivity() {
    // view binding
    private lateinit var binding: ActivityAddQuestionsBinding
    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicio, autorizacion de firebase
        firebaseAuth = FirebaseAuth.getInstance()

        //Configuracion de progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere Porfavor")
        progressDialog.setCanceledOnTouchOutside(false)
        //dar clic, volver
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //dar clic, comenzar a cargar categoria
        binding.submitBtn.setOnClickListener{
            validateData()
        }
    }
    //question=category
    private var question= ""
    private fun validateData() {


        //obtener datos
        question = binding.questionEt.text.toString().trim()
        //validar datos
        if (question.isEmpty()){
            Toast.makeText(this, "Ingrese su pregunta", Toast.LENGTH_SHORT).show()
        }else{
            addQuestionFirebase()
        }

    }

    private fun addQuestionFirebase() {
        //mostras progresso
        progressDialog.show()
        //obtener tiempo
        val timestamp = System.currentTimeMillis()
        // configuracion de datos para ingresar a la base de datos
        val hashMap = HashMap<String, Any>()

        hashMap["id"]="$timestamp"
        hashMap["question"]=question
        hashMap["timestamp"]=timestamp
        hashMap["uid"]="${firebaseAuth.uid}"

        //agregar a firebase db : DtabaseRoot > categories > categoryId >category info
        val ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //se ha añadido exitosamente
                progressDialog.dismiss()
                Toast.makeText(this, "Se ha añadido correctamente...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                //fallo al agregar
                progressDialog.dismiss()
                Toast.makeText(this, "Error al agregar debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }
}
