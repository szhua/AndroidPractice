package com.szhua.pagedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.MessageQueue
import android.view.*
import android.widget.ScrollView
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



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
    }
}