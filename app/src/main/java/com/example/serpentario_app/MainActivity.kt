package com.example.serpentario_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.serpentario_app.Model.CardViewModel
import com.example.serpentario_app.Model.MyAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser


        val hView: View = nav_view.inflateHeaderView(R.layout.nav_header)
        val ImageView = hView.findViewById(R.id.imageView) as ImageView
        val tv1 = hView.findViewById(R.id.NombreApp) as TextView
        val tv2 = hView.findViewById(R.id.NombreUsuario) as TextView

        tv1.text = "Serpientes de Antioquia"
        tv2.text = currentUser?.displayName
        ImageView.setImageResource(R.mipmap.icono_splash_foreground)

        val arrayList = ArrayList<CardViewModel>()

        arrayList.add(
            CardViewModel(
                "Acerca de la aplicación",
                "Informacion.Entidades paticipantes.Justificación de la aplicación.Guías de uso",
                R.mipmap.icono_splash_foreground
            )
        )

        arrayList.add(
            CardViewModel(
                "Serpientes venenosas",
                "Guía de identificación. Descripción de especies venenosas y distribución.",
                R.mipmap.icono_serpiente_login_foreground
            )
        )

        arrayList.add(
            CardViewModel(
                "Serpientes no venenosas",
                "Guía de identificación. Descripción de especies venenosas y distribución.Importancia de la conservación de especies.",
                R.mipmap.icono_splash_foreground
            )
        )

        arrayList.add(
            CardViewModel(
                "Registro de avistamientos",
                "Informe de un avistamiento en su zona de hogar o trabajo.",
                R.mipmap.icono_serpiente_login_foreground
            )
        )

        arrayList.add(
            CardViewModel(
                "Alerta de avistamiento",
                "Zonas con avistamientos reportados por los usuarios.",
                R.mipmap.icono_splash_foreground
            )
        )

        arrayList.add(
            CardViewModel(
                "Accidente ofídico",
                "Manifestaciones clínicas del accidente ofídico. Información y prevención del accidente ofídico.",
                R.mipmap.icono_serpiente_login_foreground
            )
        )

        val myAdapter = MyAdapter(arrayList, this)

        RecyclerViewMain.layoutManager = LinearLayoutManager(this)
        RecyclerViewMain.adapter = myAdapter


        //Usuario Firebase
        mAuth = FirebaseAuth.getInstance()


        //Descripción navigation Drawer.....................
        setSupportActionBar(tool_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            tool_bar,
            R.string.drawer_open,
            R.string.drawer_close
        ){
            override fun onDrawerClosed(drawerView: View){
                super.onDrawerClosed(drawerView)
                setTitle(R.string.app_name)
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerClosed(drawerView)
                setTitle(R.string.option)
            }

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener {
                item: MenuItem ->
            when (item.itemId) {
                R.id.Inicio -> {
                    goToMainActivity()
                }
                R.id.InfoApp -> {
                    goToInfoAppActivity()
                }
                R.id.EntiResp -> {
                    goToEntiRespActivity()
                }
                R.id.Configuracion -> {
                    goToConfiguracionActivity()
                }
                R.id.cerrarSesion -> {
                    mAuth.signOut()
                    Toast.makeText(this, "Sesión Terminada", Toast.LENGTH_SHORT).show()
                    goToLoginActivity()
                }
                else -> super.onOptionsItemSelected(item)
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true

        }
        //.............................


    }

    private fun goToMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToInfoAppActivity() {
        var intent = Intent(this, InfoAppActivity::class.java)
        startActivity(intent)
    }

    private fun goToEntiRespActivity() {
        var intent = Intent(this, EntiRespActivity::class.java)
        startActivity(intent)
    }

    private fun goToConfiguracionActivity() {
        var intent = Intent(this,ConfiguracionActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        var intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}