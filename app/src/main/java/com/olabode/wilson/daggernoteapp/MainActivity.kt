package com.olabode.wilson.daggernoteapp


import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import com.olabode.wilson.daggernoteapp.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var mAdView: AdView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            setFirstItemChecked()
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val topLevelDestinations = setOf(
            R.id.nav_home, R.id.favourites, R.id.trashFragment, R.id.settings, R.id.labelFragment
        )
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations, drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener(this)

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                mAdView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                mAdView.visibility = View.GONE
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (topLevelDestinations.contains(destination.id)) {
                drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_UNLOCKED,
                    GravityCompat.START
                )
            } else {
                drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    GravityCompat.START
                )
            }


            when (destination.id) {
                R.id.noteFragment -> {
                    mAdView.visibility = View.GONE
                }
                else -> {
                    mAdView.visibility = View.VISIBLE
                }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Called when leaving the activity
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    // Called before the activity is destroyed
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .build()
                if (isValidDestination(R.id.nav_home)) {
                    Handler().postDelayed({
                        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                            R.id.nav_home, null, navOptions
                        )
                    }, 305)
                }

            }
            R.id.nav_settings -> {
                if (isValidDestination(R.id.settings)) {
                    navigateDestinationFromHost(R.id.settings)
                }
            }

            R.id.nav_favourites -> {
                if (isValidDestination(R.id.favourites)) {
                    navigateDestinationFromHost(R.id.favourites)
                }
            }

            R.id.nav_trash -> {
                if (isValidDestination(R.id.trashFragment)) {
                    navigateDestinationFromHost(R.id.trashFragment)
                }
            }
            R.id.label -> {
                if (isValidDestination(R.id.labelFragment)) {
                    navigateDestinationFromHost(R.id.labelFragment)
                }

            }

        }
        item.isChecked = true
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateDestinationFromHost(toDestination: Int) {
        Handler().postDelayed({
            Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(toDestination)
        }, 305)
    }


    private fun isValidDestination(destination: Int): Boolean {
        return destination != Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        ).currentDestination!!.id
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun setFirstItemChecked() {
        val menu: Menu = nav_view.menu
        menu.findItem(R.id.nav_home).isChecked = true
    }
}


