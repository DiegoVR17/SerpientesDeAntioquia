package com.example.serpentario_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            when(item.itemId){
                R.id.Ven_no_Ven ->{
                    goToVenNoVenActivity()
                }
                R.id.Mapa ->{
                    goToMapaActivity()
                }
                R.id.Avistamiento ->{
                    goToAvistamientoActivity()
                }
                R.id.Info ->{
                    goToInfoActivity()
                }
                R.id.Enti_Resp ->{
                    goToEntiActivity()
                }
                R.id.cerrarSesion ->{
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

    private fun goToEntiActivity() {
        var intent = Intent(this,EntiActivity::class.java)
        startActivity(intent)
    }

    private fun goToInfoActivity() {
        var intent = Intent(this,InfoActivity::class.java)
        startActivity(intent)
    }

    private fun goToAvistamientoActivity() {
        var intent = Intent(this,AvistamientoActivity::class.java)
        startActivity(intent)
    }

    private fun goToVenNoVenActivity() {
        var intent = Intent(this,VenNoVenActivity::class.java)
        startActivity(intent)
    }

    private fun goToMapaActivity() {
        var intent = Intent(this,MapaActivity::class.java)
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