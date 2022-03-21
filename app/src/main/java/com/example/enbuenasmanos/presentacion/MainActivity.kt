package com.example.enbuenasmanos.presentacion

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.enbuenasmanos.Constants
import com.example.enbuenasmanos.Model.ModelPost
import com.example.enbuenasmanos.controladores.adapters.AdapterPost
import com.example.enbuenasmanos.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : Fragment() {

    private var url = "" //completamos con la url para recuperar la publicacion
    private var nextToken = "" //token de la pagina siguiente para liderar proximas y mas publicaciones

    private lateinit var postArrayList: ArrayList<ModelPost>
    private lateinit var adapterPost: AdapterPost

    private lateinit var progressDialog: ProgressDialog
    private var isSearch = false
    private val TAG = "MAIN_TAG"
    private lateinit var binding: ActivityMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        //Cuadro de diálogo de progreso de la configuración
        progressDialog = ProgressDialog(this.activity)
        progressDialog.setTitle("Por favor espere....")

        //Inicie y borre la lista antes de agregarle datos.
        postArrayList = ArrayList()
        postArrayList.clear()

        loadPosts()

        // manejar clic, cargar más publicaciones
        loadMoreBtn.setOnClickListener {
            val query = searchEt.text.toString().trim()
            if(TextUtils.isEmpty(query)){
                loadPosts()
            }else{
                searchPosts(query)
            }
        }
        //buscar
        searchBtn.setOnClickListener{
            nextToken=""
            url=""

            postArrayList = ArrayList()
            postArrayList.clear()
            loadMoreBtn.isEnabled = true
            val query = searchEt.text.toString().trim()
            if(TextUtils.isEmpty(query)){
                loadPosts()
            }else{
                searchPosts(query)
            }
        }
    }

    private fun loadPosts(){
        isSearch = false

        progressDialog.show()

        url = when(nextToken){
            "" -> {
                Log.d(TAG, "loadPosts: NextPage Tokens está vacío, más publicaciones")
                ("https://www.googleapis.com/blogger/v3/blogs/${Constants.BLOG_ID}/posts?maxResults=${Constants.MAX_RESULTS}&key=${Constants.API_KEY}")
            }
            "end" -> {
                Log.d(TAG, "loadPosts: El token de la página siguiente ha terminado, no hay mas publicaciones, es decir, se cargaron todas las publicaciones")
                Toast.makeText(activity, "No más publicaciones...", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                return
            }
            else -> {
                Log.d(TAG, "loadPosts: NextPage token: $nextToken")
                ("https://www.googleapis.com/blogger/v3/blogs/${Constants.BLOG_ID}/posts?maxResults=${Constants.MAX_RESULTS}&pageToken=$nextToken&key=${Constants.API_KEY}")
            }
        }

        Log.d(TAG, "loadPosts: URL: $url")

        //Solicitar datos, Metodo GET
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            //recibimos respuesta, para descartar el diálogo primero
            progressDialog.dismiss()
            Log.d(TAG, "loadPosts: $response")
            /*Los datos JSON son parámetros/variables de respuesta, pueden causar una excepción al analizar/formatear, así que intente capturar*/
            try {
                //Nosotros tenemos respuesta como Objeto JSON
                val jsonObject = JSONObject(response)
                try {
                    nextToken = jsonObject.getString("nextPageToken")
                    Log.d(TAG, "loadPosts: NextPageToken: $nextToken")
                }catch (e:Exception){
                    Toast.makeText(activity, "Llegó al final de la página...", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "loadPosts: Llegó al final de la página...")
                    nextToken="end"
                }

                //obtener datos de matriz json del objeto json
                var jsonArray = jsonObject.getJSONArray("items")
                //continuar obteniendo datos hasta completartodo
                for(i in 0 until jsonArray.length()){
                    try {
                        val jsonObject01 = jsonArray.getJSONObject(i)

                        val id = jsonObject01.getString("id");
                        val title = jsonObject01.getString("title");
                        val content = jsonObject01.getString("content");
                        val published = jsonObject01.getString("published");
                        val updated = jsonObject01.getString("updated");
                        val url = jsonObject01.getString("url");
                        val selfLink = jsonObject01.getString("selfLink");
                        val authorName = jsonObject01.getJSONObject("author").getString("displayName");
                        // val image = jsonObject01.getJSONObject("author").getString("image");


                        //Establecer datos
                        val modelPost = ModelPost(
                            "$authorName",
                            "$content",
                            "$id",
                            "$published",
                            "$selfLink",
                            "$title",
                            "$updated",
                            "$url"

                        )
                        //AGREGAR DATOS A LA LISTA

                        postArrayList.add(modelPost)
                    }catch (e:java.lang.Exception){
                        Log.d(TAG, "loadPosts: 1 ${e.message}")
                        Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                //adaptador de configuracion
                adapterPost = AdapterPost(activity, postArrayList)
                // configurar adaptador para recyclerview
                postsRv.adapter =adapterPost
                //findViewById<RecyclerView>(R.id.postsRv).adapter =adapterPost
                progressDialog.dismiss()

            }catch (e:Exception){
                Log.d(TAG, "loadPosts: 2 ${e.message} ")
                Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, { error ->
            Log.d(TAG, "loadPosts: ${error.message}")
            Toast.makeText(activity, "${error.message}", Toast.LENGTH_SHORT).show()
        })


        //agregar solicitud en cola
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(stringRequest)
    }

    private fun searchPosts(query: String){
        isSearch = true
        progressDialog.show()

        url = when(nextToken){
            "" -> {
                Log.d(TAG, "searchPosts: NextPage Tokens está vacío, más publicaciones")
                ("https://www.googleapis.com/blogger/v3/blogs/${Constants.BLOG_ID}/posts/search?q=$query&key=${Constants.API_KEY}")
            }
            "end" -> {
                Log.d(TAG, "searchPosts: El token de la página siguiente ha terminado, no hay mas publicaciones, es decir, se cargaron todas las publicaciones")
                Toast.makeText(activity, "No más publicaciones...", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                return
            }
            else -> {
                Log.d(TAG, "searchPosts: NextPage token: $nextToken")
                ("https://www.googleapis.com/blogger/v3/blogs/${Constants.BLOG_ID}/posts/search?q=$query&pageToken=$nextToken&key=${Constants.API_KEY}")
            }
        }

        Log.d(TAG, "searchPosts: URL: $url")

        //Solicitar datos, Metodo GET
        val stringRequest = StringRequest(Request.Method.GET, url, {response ->
            //recibimos respuesta, para descartar el diálogo primero
            progressDialog.dismiss()
            Log.d(TAG, "searchPosts: $response")
            /*Los datos JSON son parámetros/variables de respuesta, pueden causar una excepción al analizar/formatear, así que intente capturar*/
            try {
                //Nosotros tenemos respuesta como Objeto JSON
                val jsonObject = JSONObject(response)
                try {
                    nextToken = jsonObject.getString("nextPageToken")
                    Log.d(TAG, "searchPosts: NextPageToken: $nextToken")
                }catch (e:Exception){
                    Toast.makeText(activity, "Llegó al final de la página...", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "searchPosts: Llegó al final de la página...")
                    nextToken="end"
                }

                //obtener datos de matriz json del objeto json
                var jsonArray = jsonObject.getJSONArray("items")
                //continuar obteniendo datos hasta completartodo
                for(i in 0 until jsonArray.length()){
                    try {
                        val jsonObject01 = jsonArray.getJSONObject(i)

                        val id = jsonObject01.getString("id");
                        val title = jsonObject01.getString("title");
                        val content = jsonObject01.getString("content");
                        val published = jsonObject01.getString("published");
                        val updated = jsonObject01.getString("updated");
                        val url = jsonObject01.getString("url");
                        val selfLink = jsonObject01.getString("selfLink");
                        val authorName = jsonObject01.getJSONObject("author").getString("displayName");
                        // val image = jsonObject01.getJSONObject("author").getString("image");


                        //Establecer datos
                        val modelPost = ModelPost(
                            "$authorName",
                            "$content",
                            "$id",
                            "$published",
                            "$selfLink",
                            "$title",
                            "$updated",
                            "$url"

                        )
                        //AGREGAR DATOS A LA LISTA

                        postArrayList.add(modelPost)
                    }catch (e:java.lang.Exception){
                        Log.d(TAG, "loadPosts: 1 ${e.message}")
                        Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                //adaptador de configuracion
                adapterPost = AdapterPost(activity, postArrayList)
                // configurar adaptador para recyclerview
                postsRv.adapter =adapterPost
                //findViewById<RecyclerView>(R.id.postsRv).adapter =adapterPost
                progressDialog.dismiss()

            }catch (e:Exception){
                Log.d(TAG, "loadPosts: 2 ${e.message} ")
                Toast.makeText(activity, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        }, { error ->
            Log.d(TAG, "loadPosts: ${error.message}")
            Toast.makeText(activity, "${error.message}", Toast.LENGTH_SHORT).show()
        })


        //agregar solicitud en cola
        val requestQueue = Volley.newRequestQueue(activity)
        requestQueue.add(stringRequest)
    }
}