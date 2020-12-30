package com.szhua.pagedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val hos :NavHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        bnv_view.setupWithNavController(hos.navController)

        hos.navController.addOnDestinationChangedListener { _, destination, _ ->
            hos.navController.saveState()
            when(destination.id){
                R.id.meFragment -> iv_camera.visibility = View.VISIBLE
                else -> iv_camera.visibility = View.GONE
            }
        }
    }
}