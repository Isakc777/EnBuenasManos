package com.example.enbuenasmanos;

import android.content.Context
import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import java.text.SimpleDateFormat

class AdapterPost(
    //agregamos contructor incluyendo contexto y clase
    private val context: Context,
    private val postArrayList: ArrayList<ModelPost>

) : RecyclerView.Adapter<AdapterPost.HolderPost>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPost {
        //diseño row_post.xml
        val view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false)
        return HolderPost(view)
    }

    override fun onBindViewHolder(holder: HolderPost, position: Int) {
        //datos, establecer datos, formato de datos, manejar clic, etc
        val model = postArrayList[position]//obtener datos de una posicionn especificada de la lista / indice de la lista
        //obtener datos
        val authorName = model.authorName
        val content = model.content // esto esta en formato HTM, convertiremos este a texto simple usando JSOUP 'org.jsoup:jsoup:1.14.3'
        val id = model.id
        val published = model.published //fecha de publicacion, se necesita dar formato
        val selftLink = model.selfLink
        val title = model.title
        val updated = model.updated//fecha editada/actualizada
        val url = model.url

        //Convertimos contenido HTML a texto simple
        val document = Jsoup.parse(content)
        try{
            //obtener la imagen, puede haber varias o ninguna imagen en una publicacion, intentamos obtener la primera
            val elements = document.select("img")
            val image = elements[0].attr("src")
            //establecer imagen
            Picasso.get().load(image).placeholder(R.drawable.ic__image_black).into(holder.imageTv)


        }catch (e:Exception){
            //excepcion al obtener la imagen, pude deberse a que no hay ninguna magen en la publicacion, configrua la imgen predeterminada
            holder.imageTv.setImageResource(R.drawable.ic__image_black)
        }

        //formato de fecha
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // convertir de 2022-02-20T15:13:00-08:00
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy K:mm a")// convertir a 20/10/2022 13:00 PM
        val formattedDate = ""

        try {
            val date = dateFormat.parse(published)
           // formattedDate = dateFormat2.format(date) reviar luego xd
        }catch (e:Exception){
            //en caso de excepcion establecemos lo mismo que obtengamos con la API
            //formattedDate = published
            e.printStackTrace()
        }

        holder.titleTv.text = title
        holder.descriptionTv.text =document.text()
        holder.publishInfoTv.text = "Por $authorName $formattedDate" // ejemplo Por Isaac Hernandez, 20/102/2022 13:00 Pm
    }



   override fun getItemCount(): Int {
        //devuelve el numero de elementos/registros/list_size
        return postArrayList.size
   }
    /*ver clase de carpeta, contiene sus vistas de interfaz de usuario de row_post.xml*/

    inner class HolderPost(itemView: View) : RecyclerView.ViewHolder(itemView){
        //iniciar vistas de interfaz de usuario
        var moreBtn:ImageButton = itemView.findViewById(R.id.moreBtn)
        var titleTv:TextView = itemView.findViewById(R.id.titleTv)
        var publishInfoTv:TextView = itemView.findViewById(R.id.publishInfoTv)
        var imageTv:ImageView = itemView.findViewById(R.id.imageTv)
        var descriptionTv:TextView = itemView.findViewById(R.id.descriptionTv)


    }


}
