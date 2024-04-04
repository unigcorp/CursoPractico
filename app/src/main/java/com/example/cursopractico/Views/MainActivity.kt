package com.example.cursopractico.Views

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cursopractico.R
import com.example.cursopractico.Views.fragments.CursoFragment
import com.example.cursopractico.Views.fragments.EstudianteCursoFragment
import com.example.cursopractico.Views.fragments.EstudianteFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var menuDrawer:DrawerLayout
    private lateinit var navigationView:NavigationView
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuDrawer = findViewById(R.id.id_drawer)
        navigationView = findViewById(R.id.id_navigation)
        toolbar = findViewById(R.id.idtoolbar)
        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)
        val toogle = ActionBarDrawerToggle(this, menuDrawer, toolbar, R.string.open, R.string.close)
        menuDrawer.addDrawerListener(toogle)
        toogle.syncState()
        if(savedInstanceState == null){
            replaceFragment(EstudianteFragment(this))
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.id_android ->{
                replaceFragment(EstudianteFragment(this))
            }
            R.id.id_scanner ->{
                replaceFragment(CursoFragment())
            }
            R.id.id_phone ->{
                replaceFragment(EstudianteCursoFragment(this))
            }
        }
        menuDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.id_frame,fragment)
        fragmentTransition.commit()
    }


}