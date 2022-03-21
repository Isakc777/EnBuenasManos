package com.example.enbuenasmanos.presentacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.enbuenasmanos.ForumActivity
import com.example.enbuenasmanos.MainActivity
import com.example.enbuenasmanos.R
import com.example.enbuenasmanos.databinding.ActivityPrincipalBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class PrincipalActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityPrincipalBinding
    private var lstFragments = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragment(R.id.itComunidad, ForumActivity())
        lstFragments.add(R.id.itComunidad)

        //metodo setup
        val bundle:Bundle? =intent.extras
        val email:String? = bundle?.getString("email")
        val provider:String? = bundle?.getString("provider")
        val password:String? = bundle?.getString("password")
        setup(email?:"",provider?:"",password?:"")



        //Guardado de datos
        val prefs = getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed();
        if (lstFragments.size > 1) {
            lstFragments.removeLast()
            binding.bottomNavigation.menu.findItem(lstFragments.last()).isChecked = true
        }
    }

    private fun changeFragment(tagToChange: Int, fragment: Fragment? = null){
        var addStack: Boolean = false
        val ft = supportFragmentManager.beginTransaction()

        if(lstFragments.isNotEmpty()){
            val currentFragment =
                supportFragmentManager.findFragmentByTag(lstFragments.last().toString())!!
            val toChangeFragment = supportFragmentManager.findFragmentByTag(tagToChange.toString())
            currentFragment.onPause()

            if(toChangeFragment != null){
                if(currentFragment != toChangeFragment){
                    addStack = true
                    ft.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    );
                    ft.hide(currentFragment).show(toChangeFragment)
                    toChangeFragment.onResume()
                }
            } else {
                if(fragment != null) {
                    addStack = true
                    ft.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    );
                    ft.hide(currentFragment)
                        .add(binding.FrameLayout.id, fragment, tagToChange.toString())
                }
            }
        } else {
            if(fragment != null) {
                ft.add(binding.FrameLayout.id, fragment, tagToChange.toString())
                addStack = true
            }
        }

        if(addStack) {
            ft.commit()
            ft.addToBackStack(tagToChange.toString())
            lstFragments.add(tagToChange)
        }
    }
    private fun setup(email: String, provider:String, password:String){
        binding.topAppBar.setTitle(email)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exit -> {
                    //Borrado de Datos
                    val prefs = getSharedPreferences("com.example.pruebafirebase.PREFERENCE_FILE_KEY",MODE_PRIVATE).edit()
                    prefs.clear()
                    prefs.apply()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itComunidad -> {
                    changeFragment(R.id.itComunidad, ForumActivity())
                    true
                }
                R.id.itConsejos -> {
                    changeFragment(R.id.itConsejos, MainActivity())
                    true
                }
                R.id.itPerfil -> {
                    changeFragment(R.id.itPerfil, PerfilFragment())
                    true
                }
                R.id.itAyudaProfesional -> {
                    changeFragment(R.id.itAyudaProfesional, ListarFragment())
                    true
                }
                else -> false
            }
        }

    }
}