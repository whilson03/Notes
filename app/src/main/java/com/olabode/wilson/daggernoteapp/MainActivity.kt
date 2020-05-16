package com.olabode.wilson.daggernoteapp


import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), IMainActivity,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAdView: AdView

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var viewModel: MainActivityViewModel

    @Inject
    lateinit var factory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        viewModel =
            ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mAdView = findViewById(R.id.adView)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val menu = navView.menu
        val labelSubMenu = menu.addSubMenu(getString(R.string.labels))

        val navController = findNavController(R.id.nav_host_fragment)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val topLevelDestinations = setOf(
            R.id.nav_home, R.id.favourites, R.id.trashFragment, R.id.settings, R.id.labelFragment
            , R.id.labeledNoteView
        )
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations, drawerLayout
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)



        setUpAds()
        setFirstItemChecked()

        navView.setNavigationItemSelectedListener(this)



        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (topLevelDestinations.contains(destination.id)) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START)
                mAdView.visibility = View.VISIBLE
            } else {
                drawerLayout.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    GravityCompat.START
                )
                mAdView.visibility = View.GONE
            }
        }

        viewModel.allLabels.observe(this, Observer {
            labelSubMenu.clear()
            val list = viewModel.fixedNavItemsLiveData()
            it.forEach { l ->
                labelSubMenu.add(0, l.labelId.toInt(), 0, l.title)
            }
        })

    }

    private fun setUpAds() {
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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        hideKeyBoard()
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

    /**
     * navigation for labels that will be added to the navigation drawer
     */
    private fun navigateFromDynamicMenu(id: Int, title: String) {
        if (isValidDestination(id)) {
            Handler().postDelayed({
                val args = bundleOf(
                    "title" to title, "labelId" to id.toLong()
                )
                Navigation.findNavController(this, R.id.nav_host_fragment)
                    .navigate(R.id.labeledNoteView, args)

            }, 305)
        }
    }

    private fun navigateDestinationFromHost(toDestination: Int) {
        Handler().postDelayed({
            Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(toDestination)
        }, 316)
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

    override fun hideKeyBoard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }


    private fun performDrawerClicks(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_home -> {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, true)
                    .build()
                if (isValidDestination(R.id.nav_home)) {
                    Handler().postDelayed({
                        Navigation.findNavController(
                            this@MainActivity,
                            R.id.nav_host_fragment
                        ).navigate(
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

            else -> navigateFromDynamicMenu(menuItem.itemId, menuItem.title.toString())
        }

        drawerLayout.closeDrawer(GravityCompat.START)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        performDrawerClicks(item)
        return true
    }

    private fun setFirstItemChecked() {
        val menu: Menu = nav_view.menu
        menu.findItem(R.id.nav_home).isChecked = true
    }

}


