package com.iti.itiproject.ui.setup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.iti.itiproject.R
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.ActivityMainBinding
import com.iti.itiproject.ui.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var dataStore: DataStoreImplementation
    private lateinit var splashScreen: androidx.core.splashscreen.SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dataStore = DataStoreImplementation(appContext = this)
        splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }
        handleDirections()
        setContentView(binding.root)
    }

    private fun handleDirections() {
        GlobalScope.launch(Dispatchers.Main) {
            if (dataStore.isLoggedIn()) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                this@MainActivity.finish()
            }
            if (dataStore.isPassedOnBoarding()) {
                setFindNavController(R.id.loginFragment)
                splashScreen.setKeepOnScreenCondition { false }

            } else {
                setFindNavController(R.id.onBoardingFragment)
                splashScreen.setKeepOnScreenCondition { false }

            }
        }
    }

    private fun setFindNavController(host: Int) {
        val navInflater = findNavController(R.id.nav_host_fragment).navInflater
        val graph: NavGraph = navInflater.inflate(R.navigation.setup_nav)
        graph.setStartDestination(host)
        val navController: NavController = findNavController(R.id.nav_host_fragment)
        navController.graph = graph

    }
}