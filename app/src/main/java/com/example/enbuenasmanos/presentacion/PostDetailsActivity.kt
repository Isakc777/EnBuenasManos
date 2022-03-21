package com.example.enbuenasmanos.presentacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.enbuenasmanos.Constants
import com.example.enbuenasmanos.Model.ModelLabel
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.controladores.adapters.AdapterLabel
import kotlinx.android.synthetic.main.activity_post_details.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import javax.xml.transform.OutputKeys

class PostDetailsActivity : AppCompatActivity() {

    private var postId:String? = null // obtendremos el intento, y pasamos a AdparterPost
    private val TAG ="POST_DETAILS_TAG"

    //actionbar
    private lateinit var actionBar:ActionBar

    private lateinit var labelArrayList: ArrayList<ModelLabel>
    private lateinit var adapterLabel: AdapterLabel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        //barra de acción de inicio
        actionBar = supportActionBar!!
        actionBar.title="Detalles Publicación"
        // añadimos el botón de retroceso
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // obtener la identificación de la publicación
        postId = intent.getStringExtra("postId")
        Log.d(TAG, "onCreate: $postId")

        //configuracion webview
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        loadPostDetails()
    }

    private fun loadPostDetails() {
        val url = ("https://www.googleapis.com/blogger/v3/blogs/${Constants.BLOG_ID}/posts/$postId?key=${Constants.API_KEY}")
        Log.d(TAG,"loadPostDetails: $url")

        // solicitud a la API
        val stringRequest = StringRequest(Request.Method.GET, url,{ response ->
            //respuesta recibida con éxito
            Log.d(TAG,"loadPostDetails: $response")

            //La respuesta está en el objeto JSON
            try{
                val jsonObject =JSONObject(response)

                // Obtener Datos
                var title = jsonObject.getString("title")
                val published = jsonObject.getString("published")
                val content = jsonObject.getString("content")
                val url = jsonObject.getString("url")
                val displayName = jsonObject.getJSONObject("author").getString("displayName")

                //convertir el formato adecuado de hora GMT
                val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // convertir de 2022-02-20T15:13:00-08:00
                val dateFormat2 = SimpleDateFormat("dd/MM/yyyy K:mm a")// convertir a 20/10/2022 13:00 PM
                var formattedDate = ""

                try {
                    val date = dateFormat.parse(published)
                    formattedDate = dateFormat2.format(date)
                }catch (e:Exception){
                    //en caso de excepcion establecemos lo mismo que obtengamos con la API
                    formattedDate = published
                    e.printStackTrace()
                }

                //establecer datos
                actionBar.subtitle=title
                titleTv.text = title
                publishInfoTv.text="Por $displayName $formattedDate"// ejemplo por Isaac H 5/3/2022
                //el contenido contiene una página web como html, así que cargue en la vista web
                webView.loadDataWithBaseURL(null, content, "text/html", OutputKeys.ENCODING, null)

                // obtenemos las etiquetas, respuesta del detalle de la publicación: la API de publicación específica también contiene una variedad de etiquetas de esa publicación
                try {
                    labelArrayList = ArrayList()
                    labelArrayList.clear()
                    val jsonArray = jsonObject.getJSONArray("labels")
                    for (i in 0 until jsonArray.length()){
                       //obtenemos las etiquetas
                        val label = jsonArray.getString(i)
                        // agregar datos del modelo
                        val modelLabel = ModelLabel(label)
                        //agragar modelo a la listA
                        labelArrayList.add(modelLabel)
                    }
                    //CONFIGURACION DEL ADAPTADOR para etiquetas
                    adapterLabel = AdapterLabel(this@PostDetailsActivity, labelArrayList)
                    //establecer el adaptador
                    labelsRv.adapter = adapterLabel


                }catch (e:Exception){
                    Log.d(TAG, "loadPostDetails: ${e.message}")
                }

            } catch (e:Exception){
                Log.d(TAG, "loadPostDetails: ${e.message}")
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }){error ->
            //error al recibir respuesta, mostramos el mensaje de error
            Log.d(TAG, "loadPostDetails: ${error.message}")
            Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()

        }

        //agregar solicitud a la cola
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//ir a la actividad anterior de donde vinimos, cuando se hizo clic en el botón Atrás en la barra de acción
        return super.onSupportNavigateUp()
    }
}
