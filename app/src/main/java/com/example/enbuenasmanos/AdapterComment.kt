package com.example.enbuenasmanos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.enbuenasmanos.databinding.RowCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

import org.w3c.dom.Text

class AdapterComment: RecyclerView.Adapter<AdapterComment.HolderComment> {


    //contexto
    val context: Context

    //arraylistde comentarios
    val commentArrayList: ArrayList<ModelComment>

    //view binding row_comment.xml >>> Row CommentBinding

    private lateinit var binding: RowCommentBinding
    // firebase Auth= autenticacion
     private lateinit var firebaseAuth: FirebaseAuth

   // private val db= FirebaseFirestore.getInstance()
    //obtener nombre
    //val intent: Intent?


    //val bundle: Bundle?=this.intent?.extras
   // val email: String? = bundle?.getString("email")
   // db.collection("usuarios").document(email.toString()).get().addOnSuccessListener {
    //    binding.
  //  }


    //constructor
    constructor(context: Context, commentArrayList: ArrayList<ModelComment>) {
        this.context = context
        this.commentArrayList = commentArrayList
        //iniciar la autenticacion de firebase
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderComment {
        //enlazar a  row_comment.xml
        binding = RowCommentBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderComment(binding.root)

    }

    override fun onBindViewHolder(holder: HolderComment, position: Int) {
        /*Obtener datos, etc*/

        //obtener datos
        var model= commentArrayList[position]
        val id = model.id
        val questionId = model.questionId
        val comment = model.comment
        val uid = model.uid
        val timestamp = model.timestamp
        //formato de timestamp = marca de tiempo
        val date = MyDate.formatTimeStamp(timestamp.toLong())
        //establecer datos
        holder.dateTv.text = date
        holder.commentTv.text = comment

        //cargaremos el uid del usaurio /////////////////////////////////////////////////////////////////
        /*loadUserDetails(model, holder)*/
        //al presionar mostramos la opcion para borrar la respuesta
        //al dar clic abrimos los detalles de la pregunta


         holder.itemView.setOnClickListener{
            /*lo que se requiere para borrar el comentario
            * 1)el usuario debe estar registrado
            * 2)el uid del usuario registrado debe ser igual al Uid del usuario actual ya qie solo puede eliminar su propio comentario*/
           /* if (firebaseAuth.currentUser !=null && firebaseAuth.uid == uid){
                deleteCommentDialog(model, holder)
            }*/

            deleteCommentDialog(model, holder)

        }




    }

    private fun deleteCommentDialog(model: ModelComment, holder: AdapterComment.HolderComment) {
        //alerta
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Borrar Respuesta")
            .setMessage("Esta seguro que quiere borrar esta respuesta?")
            .setPositiveButton("BORRAR"){d,e->
                // boorar comentario

                val questionId = model.questionId
                val commentId = model.id

                val ref = FirebaseDatabase.getInstance().getReference("Questions")
                ref.child(questionId).child("Comments").child(commentId)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Respuesta Borrada...", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener{ e ->
                        //fallo al borrar
                        Toast.makeText(context, "No se purdo borrar la respuesta ${e.message}", Toast.LENGTH_SHORT).show()

                    }

            }
            .setNegativeButton("CANCELAR"){d,e->
                d.dismiss()
            }
            .show()
    }

    /*
    private fun loadUserDetails(model: ModelComment, holder: AdapterComment.HolderComment) {
        val uid = model.uid
        /////////////////////////////////////////////
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")//////////////////////////////////////////
        ref.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //obtener nombre del perfil
                    val name = "${snapshot.child("nombre").value}"
                    //tambien se prodira obtener la forto de perfil

                    //establecer datos
                    holder.nameTv.text = name


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
*/
    override fun getItemCount(): Int {
        return commentArrayList.size // retorno el tamaño de la lista es decir numero de items en la lista
    }

    /*View Holder clase para row_comment.xml*/
    inner class HolderComment(itemView: View): RecyclerView.ViewHolder(itemView) {
        //iniciando ui de row_comment.xml
        //val profileIv = binding.profileIv
        //val nameTv = binding.nameTv
        val dateTv = binding.dateTv
        val commentTv = binding.commentTv



    }



}