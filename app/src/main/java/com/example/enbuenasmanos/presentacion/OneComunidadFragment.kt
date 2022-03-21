package com.example.enbuenasmanos.presentacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.enbuenasmanos.controladores.ComunidadController
import com.example.enbuenasmanos.databinding.FragmentOneComunidadBinding
import com.squareup.picasso.Picasso

class OneComunidadFragment : Fragment() {

    /*private lateinit var binding : FragmentOneComunidadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneComunidadBinding.inflate(inflater, container, false)

        binding.listParentLayout.setOnClickListener() {
            val comunidad = ComunidadController().getOneComunidad()
            binding.txtAutor.text = comunidad.author
            binding.txtDesc.text = comunidad.desc
            binding.txtTitulo.text = comunidad.title

            Picasso.get().load(comunidad.img).into(binding.imgComunidad);
        }

        return binding.root
    }*/
}