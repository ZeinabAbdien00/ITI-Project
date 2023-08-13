package com.iti.itiproject.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.dataStore
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iti.itiproject.R
import com.iti.itiproject.adapter.viewpager.ViewPagerAdapter
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.ActivityHomeBinding
import com.iti.itiproject.model.auth.AuthModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = HomeViewModel()
        resetDataStore()
        setUpViewPager()
        setupNavigation()
        onClicks()
        swipeBottomNavigationWhenViewPagerChanged()
        swipeViewPagerWhenBottomNavigationChanged()
        setUpVisibilityOfBottomBar()


        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height
            if (heightDiff > dpToPx(this@HomeActivity)) { // 200dp threshold
                bottomNavigationVisibility(false)
            } else {
                bottomNavigationVisibility(true)
            }
        }

    }

    private fun resetDataStore() {
        val dataStore = DataStoreImplementation(this)
        CoroutineScope(Dispatchers.Main).launch {

            val userId = dataStore.getUserId()
            val database = FirebaseDatabase.getInstance()
            val userReference = database.getReference("users").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(AuthModel::class.java)
                    if (userData != null) {
                        saveDataStore(userData, dataStore)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@HomeActivity,
                        error.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            //check mode
            if (dataStore.getDarkMode())
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }

    }

    private fun saveDataStore(data: AuthModel, dataStore: DataStoreImplementation) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveUserDataAndLogFlag(
                data,
                context = this@HomeActivity,
                dataStore = dataStore
            )
        }
    }

    private fun bottomNavigationVisibility(isVisible: Boolean) {
        binding.navigationView.isVisible = isVisible

    }

    private fun dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 200f, context.resources.displayMetrics
        ).toInt()
    }

    private fun swipeViewPagerWhenBottomNavigationChanged() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    binding.viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.setting -> {
                    binding.viewPager.currentItem = 3
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    binding.viewPager.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    binding.viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }

    private fun setUpVisibilityOfBottomBar() {
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.todayFragment, R.id.searchFragment, R.id.settingFragment,
                R.id.allCountriesFragment
                -> bottomNavigationVisibility(true)
                else -> bottomNavigationVisibility(false)
            }
        }

    }

    private fun swipeBottomNavigationWhenViewPagerChanged() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val menu: Menu = bottomNavigationView.menu
                when (position) {
                    0 -> {
                        menu.findItem(R.id.home).isChecked = true
                    }
                    2 -> {
                        menu.findItem(R.id.search).isChecked = true
                    }
                    1 -> {
                        menu.findItem(R.id.all).isChecked = true
                    }
                    else -> {
                        menu.findItem(R.id.setting).isChecked = true
                    }
                }
            }
        })
    }

    private fun onClicks() {
        binding.viewPager.setOnClickListener {
            binding.viewPager.currentItem = binding.navigationView.itemActiveIndicatorHeight
        }
    }

    private fun setUpViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        val colorStateList = ColorStateList.valueOf(resources.getColor(R.color.white))

        bottomNavigationView.itemIconTintList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(colorStateList.defaultColor, ContextCompat.getColor(this, R.color.gray))
        )

    }
}