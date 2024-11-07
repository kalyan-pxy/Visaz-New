package com.pxy.visaz.core

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.pxy.visaz.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Common setup code (e.g. toolbar setup, logging, etc.)
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setUpToolbar(toolbar: Toolbar, title: String, showBackButton: Boolean = false) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
    }

    // Add other common functionalities here
    fun View.systemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        /*this.window?.let {
            val drawable: Drawable? =
                ContextCompat.getDrawable(this, R.drawable.bg_status_bar)
            it.statusBarColor = resources.getColor(android.R.color.transparent, null)
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.statusBarColor = resources.getColor(android.R.color.transparent, null)
            it.setBackgroundDrawable(drawable)
        }*/
    }
}
