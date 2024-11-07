package com.pxy.visaz.ui.authentication

import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.pxy.visaz.R
import com.pxy.visaz.core.BaseActivity
import com.pxy.visaz.databinding.ActivityPreAuthBinding

class PreAuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = this.installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = ActivityPreAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashScreen.setKeepOnScreenCondition {
            // This condition can be a boolean variable that indicates whether
            // initialization is complete. Return true to keep the splash screen on.
            //Thread.sleep(1000)
            binding.root.systemBars()
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            if (navHostFragment != null) {
                goNext(navHostFragment)
            } else {
                // Handle the error appropriately
                Log.e("PreAuthActivity", "NavHostFragment is null")
            }
            false // Return false to dismiss the splash screen immediately
        }
    }

    private fun goNext(navHostFragment: NavHostFragment) {
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_main, intent.extras)
    }
}