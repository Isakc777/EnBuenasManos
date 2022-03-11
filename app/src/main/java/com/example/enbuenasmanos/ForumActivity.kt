package com.example.enbuenasmanos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.enbuenasmanos.databinding.ActivityAddQuestionsBinding
import com.example.enbuenasmanos.databinding.ActivityForumBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ForumActivity : AppCompatActivity() {

    //view biding
    private lateinit var binding: ActivityForumBinding

    //firebase auth

    private lateinit var firebaseAuth: FirebaseAuth


    //lista de arreglos para contener preguntas
    private lateinit var questionArrayList: ArrayList<ModelQuestion>
    //adaptador
    private lateinit var adapterQuestion: AdapterQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicio, autorizacion de firebase
        firebaseAuth = FirebaseAuth.getInstance()
        loadQuestions()

        //busqueda
        binding.searchEt1.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //llamado como y cuando el usuario escribe algo
                try{
                    adapterQuestion.filter.filter(s)

                }catch (e:Exception){

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        //click, para abrir la pagina para a agregar pregunta
        binding.addQuestionBtn.setOnClickListener{
            startActivity(Intent(this, AddQuestionsActivity::class.java ))
        }

    }

    private fun loadQuestions() {
        //iniciar lista de arreglos
        questionArrayList = ArrayList()

        // obtener todas las preguntas de la base de datos de firebase... Firebase DB >Questions
        val ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //borrar la lista antes de comenzar a agregarle datos
                questionArrayList.clear()
                for (ds in snapshot.children){
                    // obtener datos como modelo
                    val model = ds.getValue(ModelQuestion::class.java)

                    //agregar a la lista de arreglos
                    questionArrayList.add(model!!)
                }
                //configuracion del adaptador
                adapterQuestion = AdapterQuestion(this@ForumActivity, questionArrayList)
                // adaptador enviado a recyclerview
                binding.questionsRv.adapter = adapterQuestion


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}