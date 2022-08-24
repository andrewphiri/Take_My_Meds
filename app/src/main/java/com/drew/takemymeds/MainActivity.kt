package com.drew.takemymeds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View

import androidx.core.view.isGone

import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.drew.takemymeds.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inflate activity main layout
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bind views
        val toolbar = binding.mainToolbar.materialToolbar
        val collapsingToolbarLayout = binding.mainToolbar.collapsingToolbar
        setSupportActionBar(toolbar)
        val fab = binding.addMedFab
        val appBar = binding.bottomAppBar
        val bottomNavigation = binding.bottomNavigationView
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navigationView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        collapsingToolbarLayout.title = title
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.treatmentFragment, R.id.profileFragment, R.id.moreFragment), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)
        navigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener{_,destination, _ ->
            when(destination.id) {
                R.id.homeFragment -> {
                    val currentDate = Calendar.getInstance().timeInMillis
                    val format = SimpleDateFormat.getDateInstance()
                    val formatted: String = format.format(currentDate)
                    binding.mainToolbar.currentDayTextView.text = formatted
                    showToolbarOptions()
                    fab.show()
                    fab.setImageResource(R.drawable.ic_add_24)
                    fab.setOnClickListener {
                        hideNavigationBar(bottomNavigation, appBar)
                        val action = HomeFragmentDirections.actionHomeFragmentToAddMedFragment()
                        findNavController(R.id.nav_host_fragment).navigate(action)
                    }
                    showNavigation(bottomNavigation, appBar)
                }
                R.id.treatmentFragment -> {
                    hideToolbarOptions(destination.label.toString())
                    fab.show()
                    fab.setImageResource(R.drawable.ic_add_24)
                    fab.setOnClickListener {
                    hideNavigationBar(bottomNavigation, appBar)

                        val action = TreatmentFragmentDirections.actionTreatmentFragmentToAddMedFragment()
                        findNavController(R.id.nav_host_fragment).navigate(action)
                    }
                    showNavigation(bottomNavigation, appBar)
                }
                R.id.profileFragment -> {
                    hideToolbarOptions(destination.label.toString())
                    fab.animate().rotationBy(360f).start()
                    fab.show()
                    fab.setImageResource(R.drawable.ic_person_add_24)
                    showNavigation(bottomNavigation, appBar)
                }
                R.id.moreFragment -> {
                    hideToolbarOptions(destination.label.toString())
                    fab.hide()
                    showNavigation(bottomNavigation, appBar)
                }
                R.id.addMedFragment -> {
                    hideToolbarOptions(destination.label.toString())
                    fab.hide()
                    hideNavigationBar(bottomNavigation, appBar)
                    hideToolbarOptions(destination.label.toString())
                }
                R.id.setScheduleFragment -> {
                    hideToolbarOptions(destination.label.toString())
                    fab.hide()
                    hideNavigationBar(bottomNavigation, appBar)
                    hideToolbarOptions(destination.label.toString())
                }
                else -> {
                    hideToolbarOptions(null)
                    fab.hide()

                }
            }
        }

    }

    private fun hideNavigationBar(
        bottomNavigation: BottomNavigationView,
        appBar: BottomAppBar
    ) {
        bottomNavigation.visibility = View.GONE
        appBar.visibility = View.GONE
    }

    private fun hideToolbarOptions(title: String?) {
        binding.mainToolbar.currentDayTextView.text = title
        binding.mainToolbar.calendarTextView.visibility = View.GONE
    }
    private fun showToolbarOptions() {
        if (binding.mainToolbar.currentDayTextView.visibility == View.GONE
            || binding.mainToolbar.currentDayTextView.visibility == View.INVISIBLE) {
            binding.mainToolbar.currentDayTextView.visibility = View.VISIBLE
            binding.mainToolbar.calendarTextView.visibility = View.VISIBLE
        }
    }

    private fun showNavigation(
        bottomNavigation: BottomNavigationView,
        appBar: BottomAppBar
    ) {
        if (bottomNavigation.isGone) {
            bottomNavigation.visibility = View.VISIBLE
            appBar.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }

}