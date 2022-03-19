package com.example.enbuenasmanos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.enbuenasmanos.databinding.RowQuestionsBinding
import com.google.firebase.database.FirebaseDatabase

class AdapterQuestion :RecyclerView.Adapter<AdapterQuestion.HolderQuestion>, Filterable{

    private val context: Context
    public var questionArrayList: ArrayList<ModelQuestion>
    private var filterList: ArrayList<ModelQuestion>

    private var filter: FilterQuestion? = null

    private lateinit var binding: RowQuestionsBinding
    //constructor
    constructor(context: Context, questionArrayList: ArrayList<ModelQuestion>) {
        this.context = context
        this.questionArrayList = questionArrayList
        this.filterList = questionArrayList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderQuestion {
        //inflar/vincular fila_pregunta.xml
        binding = RowQuestionsBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderQuestion(binding.root)
    }

    override fun onBindViewHolder(holder: HolderQuestion, position: Int) {
        /*Obtener datos, Establecer datos, Manejar clics, etc. */
         //Obterner datos
        val model = questionArrayList[position]
        val id = model.id
        val question = model.question
        val uid = model.uid
        val timestamp = model.timestamp

        //Establacer datos
        holder.questionTv.text = question

        // dar clic, elimina la pregunta

        holder.deleteBtn.setOnClickListener{
            //confirmar antes de borrar
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Borrar")
                .setMessage("Está seguro que desea borrar esta pregunta?")
                .setPositiveButton("Confirmar"){a, d->
                    Toast.makeText(context, "Borrando...", Toast.LENGTH_SHORT).show()
                    deleteQuestion(model, holder)
                }

                .setNegativeButton("Cancelar"){a, d->
                    a.dismiss()
                }
                .show()
        }

        //al dar clic abrimos los detalles de la pregunta
        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailQuestionActivity::class.java)
            intent.putExtra("questionId", id)
            intent.putExtra("question", question)
            context.startActivity(intent)
        }

    }

    private fun deleteQuestion(model: ModelQuestion, holder: HolderQuestion) {
        //Obtener el id de la pregunta para borrar

        val id = model.id

        //Firebase DB> Questions >questionId
        val ref = FirebaseDatabase.getInstance().getReference("Questions")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Eliminado...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context, "No se puede eliminar debido a $e.message", Toast.LENGTH_SHORT).show()
            }

    }

    override fun getItemCount(): Int {
        return questionArrayList.size // numero de elementos en la lista
    }

    //Clase ViewHolder para mantener/unidad de vistas de UI para row_questions.xml
    inner class HolderQuestion(itemView: View): RecyclerView.ViewHolder(itemView) {
        //init ui vistas
        var questionTv: TextView = binding.questionTv
        var deleteBtn:ImageButton = binding.deleteBtn
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterQuestion(filterList, this)
        }
        return filter as FilterQuestion
    }

}