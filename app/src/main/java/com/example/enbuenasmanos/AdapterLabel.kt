package com.example.enbuenasmanos

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class AdapterLabel(
    var context:Context,
    var labelArrayList: ArrayList<ModelLabel>
) : RecyclerView.Adapter<AdapterLabel.HolderLabel>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderLabel {
        //Diseño row_label.xml
        val view = LayoutInflater.from(context).inflate(R.layout.row_label, parent, false)

        return HolderLabel(view)
    }

    override fun onBindViewHolder(holder: HolderLabel, position: Int) {
       //obtener datos
        val modelLabel = labelArrayList[position]
        val label = modelLabel.label

        //colocae datos
        holder.labelTv.text = label

    }

    override fun getItemCount(): Int {
        return labelArrayList.size //devolvemos el numero de registros/lista y tamaño
    }

    /*la clase ViewHolder que retendrá/iniciará las vistas de la interfaz de usuario de row_label.xml*/
    inner class HolderLabel(itemView: View) :RecyclerView.ViewHolder(itemView){

        //vista de la interfaz row_label.xml
        var labelTv: TextView = itemView.findViewById(R.id.labelTv)

    }

}