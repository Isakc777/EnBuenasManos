package com.example.enbuenasmanos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.enbuenasmanos.databinding.ActivityAddQuestionsBinding
import com.example.enbuenasmanos.databinding.ActivityForumBinding
import com.google.firebase.auth.FirebaseAuth

class ForumActivity : AppCompatActivity() {

    //view biding
    private lateinit var binding: ActivityForumBinding

    //firebase auth

    private lateinit var firebaseAuth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicio, autorizacion de firebase
        firebaseAuth = FirebaseAuth.getInstance()

        //click, para abrir la pagina para a agregar pregunta
        binding.addQuestionBtn.setOnClickListener{
            startActivity(Intent(this, AddQuestionsActivity::class.java ))
        }

    }
}