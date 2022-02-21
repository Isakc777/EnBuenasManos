package com.example.enbuenasmanos

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var url = "" //completamos con la url para recuperar la publicacion
    private var nextToken = "" //token de la pagina siguiente para liderar proximas y mas publicaciones

    private lateinit var postArrayList: ArrayList<ModelPost>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}