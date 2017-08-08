package io.github.privacystreams.app

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val CURRENT_NAV_KEY = "currentNavId"
    var currentNavId = R.id.nav_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        if (savedInstanceState != null)
            currentNavId = savedInstanceState.getInt(CURRENT_NAV_KEY, currentNavId)
        loadFragment(currentNavId)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (currentNavId != R.id.nav_home) {
            loadFragment(R.id.nav_home)
        } else {
            super.onBackPressed()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_NAV_KEY, currentNavId)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val navId = item.itemId

        loadFragment(navId)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(navId: Int) {
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (navId == R.id.nav_home) {
            this.setTitle(R.string.home)
        } else if (navId == R.id.nav_timeline) {
            this.setTitle(R.string.timeline)
        } else if (navId == R.id.nav_insight) {
            this.setTitle(R.string.insight)
        } else if (navId == R.id.nav_market) {
            this.setTitle(R.string.market)
        } else if (navId == R.id.nav_account) {
            this.setTitle(R.string.account)
        } else if (navId == R.id.nav_data) {
            this.setTitle(R.string.data)
            val fragment = ManageDataFragment()
            fragmentTransaction.replace(R.id.content_main, fragment);
        }
        fragmentTransaction.commit();
        currentNavId = navId
    }
}
