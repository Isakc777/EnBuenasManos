package com.example.enbuenasmanos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enbuenasmanos.databinding.ActivityDetailQuestionBinding
import com.example.enbuenasmanos.databinding.RowQuestionsBinding

class DetailQuestionActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityDetailQuestionBinding

    //pregunta id, detalle
    private var questionId = ""
    private var question = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //obtener la intencion que pasamos al adaptador
        val intent = intent
        questionId = intent.getStringExtra("questionId")!!
        question = intent.getStringExtra("question")!!

        //establecer la cateogira

        binding.subTitleTv.text = question

    }
}