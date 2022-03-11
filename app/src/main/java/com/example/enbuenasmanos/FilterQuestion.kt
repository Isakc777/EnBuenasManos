package com.example.enbuenasmanos

import android.widget.Filter
class FilterQuestion: Filter {

    // lista de arreglos en la que queremos buscar
    private var filterList: ArrayList<ModelQuestion>

    //adaptador en el que se debe implementar el filtro
    private  var adapterQuestion: AdapterQuestion

    //constructor
    constructor(filterList: ArrayList<ModelQuestion>, adapterQuestion: AdapterQuestion) : super() {
        this.filterList = filterList
        this.adapterQuestion = adapterQuestion
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //el valor no debe ser nulo ni estar vacío
        if (constraint != null && constraint.isNotEmpty()){
            // el valor buscado no es nulo no está vacío


            // cambia a mayúsculas o minúsculas para evitar la distinción entre mayúsculas y minúsculas
            constraint = constraint.toString().uppercase()
            val filteredModels:ArrayList<ModelQuestion> = ArrayList()
            for (i in 0 until filterList.size){
                //validacion
                if (filterList[i].question.uppercase().contains(constraint)){
                    // agregar a la lista filtrada
                    filteredModels.add(filterList[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels

        }
        else{
            //el valor de búsqueda es nulo o está vacío
            results.count = filterList.size
            results.values = filterList
        }

        return  results //importante
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //aplicar cambios de filtro
        adapterQuestion.questionArrayList = results.values as ArrayList<ModelQuestion>

        //notificar cambios
        adapterQuestion.notifyDataSetChanged()





    }
}