package com.example.enbuenasmanos

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.enbuenasmanos.databinding.ActivityDetailQuestionBinding
import com.example.enbuenasmanos.databinding.DialogCommentAddBinding
import com.example.enbuenasmanos.databinding.RowQuestionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class DetailQuestionActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityDetailQuestionBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private lateinit var commentArrayList: ArrayList<ModelComment>

    private lateinit var adapterComment: AdapterComment

    //pregunta id, detalle
    private var questionId = ""
    private var question = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Cuadro de diálogo de progreso de la configuración
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere....")
        progressDialog.setCanceledOnTouchOutside(false)

        //inicio, autorizacion de firebase
        firebaseAuth = FirebaseAuth.getInstance()




        //obtener la intencion que pasamos al adaptador
        val intent = intent
        questionId = intent.getStringExtra("questionId")!!
        question = intent.getStringExtra("question")!!



        //establecer la cateogira

        binding.subTitleTv.text = question

        //dar clic, volver
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        //click para abrir cuadro de dialogo
        binding.addCommentBtn.setOnClickListener{
            addCommentDialog()
        }

        showComments()

    }

    private fun showComments() {
       //iniciando arraylist
        commentArrayList = ArrayList()

        //path para cargar los comentarios de la base de datos

        val ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.child(questionId).child("Comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //limpiar lista
                    commentArrayList.clear()
                    for (ds in snapshot.children){
                        //obterner datos del modelo
                        val model = ds.getValue(ModelComment::class.java)
                        //agregar la lista
                        commentArrayList.add(model!!)

                    }

                    //configuracion del adaptador
                    adapterComment = AdapterComment(this@DetailQuestionActivity, commentArrayList)
                    //establecer adaptador de recyclerview
                    binding.commentsRv.adapter = adapterComment

                }


                override fun onCancelled(error: DatabaseError) {

                }


            })

    }

    private var comment = ""


    private fun addCommentDialog() {
        //vicular vista para dialog_comment_add.xml
        val commentAddBinding = DialogCommentAddBinding.inflate(LayoutInflater.from(this))
        //configuracion de alerta de dialogo
        val builder = AlertDialog.Builder(this,R.style.CustomDialog)
        builder.setView(commentAddBinding.root)
        //crear y mostrar alerta de dialogo
        val alertDialog = builder.create()
        alertDialog.show()
        //dar clic, descartar cuadro de diálogo
        commentAddBinding.backBtn.setOnClickListener{alertDialog.dismiss()}
        //dar click, para agregar comentario
        commentAddBinding.submitBtn.setOnClickListener{
            //obtener datos
            comment = commentAddBinding.commentEt.text.toString().trim()
            //validando datos
            if(comment.isEmpty()){
                Toast.makeText(this, "Ingrese su respuesta...", Toast.LENGTH_SHORT).show()
            }else{
                alertDialog.dismiss()
                addComment()

            }

        }

    }

    private fun addComment() {
        //mostrar progreso
        progressDialog.setMessage("Añadiendo Respuesta")
        progressDialog.show()
        //marca de tiempo para comentar, marca de tiempo de comentario, etc.
        val timestamp = "${System.currentTimeMillis()}"

        //datos de configuración para agregar en db las respuestas
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["questionId"] = "$questionId"
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = "$comment"
        hashMap["uid"] = "${firebaseAuth.uid}"

        //path de la base de datos para ingresar la ifnromacion

        //Question > quiestionID> comments >commentsId > CommentsData

        val ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.child(questionId).child("Comments").child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Respuesta agregada!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo al agregarse la respuesta ${e.message}", Toast.LENGTH_SHORT).show()
            }



    }
}