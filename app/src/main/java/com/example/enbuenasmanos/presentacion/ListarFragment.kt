package com.example.enbuenasmanos.presentacion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.enbuenasmanos.Profesional
import com.example.enbuenasmanos.controladores.adapters.ProfesionalAdapter
import com.example.enbuenasmanos.databinding.FragmentListarBinding
import com.example.enbuenasmanos.logica.ProfesionalBL
import com.google.firebase.firestore.*

class ListarFragment : Fragment(){

    private lateinit var binding: FragmentListarBinding
    private lateinit var profesionalArrayList: ArrayList<Profesional>
    private lateinit var profesionalAdapter: ProfesionalAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        val bundle:Bundle? = activity?.intent?.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        val password:String? = bundle?.getString("password")
        binding.listRecyclerView.layoutManager = LinearLayoutManager(binding.listRecyclerView.context)
        profesionalArrayList = arrayListOf()
        profesionalAdapter = ProfesionalAdapter(profesionalArrayList)
        binding.listRecyclerView.adapter = profesionalAdapter

        EventChangeListener()
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("profesional").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firetore error", error.message.toString())
                    return
                }

                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        profesionalArrayList.add(dc.document.toObject(Profesional::class.java))
                    }
                }
                profesionalAdapter.notifyDataSetChanged()
            }
        })
    }
}