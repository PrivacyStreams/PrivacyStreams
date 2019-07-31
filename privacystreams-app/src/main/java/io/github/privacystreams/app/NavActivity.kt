package io.github.privacystreams.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import io.github.privacystreams.app.db.PStreamCollectService
import io.github.privacystreams.audio.Audio
import io.github.privacystreams.audio.AudioOperators
import io.github.privacystreams.core.UQI
import io.github.privacystreams.core.exceptions.PSException
import io.github.privacystreams.core.purposes.Purpose
import java.util.*

class NavActivity : Activity() {

    companion object {
        val NAV_ID_KEY = "navId"
        val DEFAULT_NAV_ID = R.id.nav_data
        val LOG_TAG = "NavActivity"
    }
    var navId = DEFAULT_NAV_ID

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        loadFragment(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
/*
        val toolbar : android.widget.Toolbar = findViewById(R.id.toolbar)
        setActionBar(toolbar)

        //PStreamCollectService.start(this)

        val navigation : BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        onNewIntent(intent)
*/
        Log.d("yile", "entering");
        try {
            val a = UQI(this).getData(Audio.record(1000), Purpose.HEALTH(""))
                    .setField("loudness", AudioOperators.getMaxAmplitude(Audio.AUDIO_DATA))
                    .getFirst<Int>("loudness")
            Log.d("yile", "loudess" + a);
        } catch (e: PSException) {
            e.printStackTrace()
        }

        try {
            val x: List<Array<Double>>
            x = UQI(this).getData(Audio.record(1000), Purpose.HEALTH(""))
                    .setField("MFCC", AudioOperators.calcMFCC(Audio.AUDIO_DATA))
                    .getFirst("MFCC")
            for (A in x){
                Log.d("yile", Arrays.toString(A));
            }

        } catch (e: PSException) {
            e.printStackTrace()
        }

        try {
            val x: List<Double>
            x = UQI(this).getData(Audio.record(1000), Purpose.HEALTH(""))
                    .setField("Zero-Crossing-Rate", AudioOperators.calcZeroCrossingRate(Audio.AUDIO_DATA))
                    .getFirst("Zero-Crossing-Rate")

            for (A in x){
                Log.d("yile", "zcr" + A);
            }


        } catch (e: PSException) {
            e.printStackTrace()
        }

        try {
            val x: List<Double>
            x = UQI(this).getData(Audio.record(1000), Purpose.HEALTH(""))
                    .setField("Frequency", AudioOperators.calcFrequency(Audio.AUDIO_DATA, 2000))
                    .getFirst("Frequency")

            for (e in x){
                Log.d("yile", "pitch double" + e);
            }
        } catch (e: PSException) {
            e.printStackTrace()
        }




    }

    override fun onNewIntent(intent: Intent) {
        val bundle = intent.extras
        if (bundle != null)
            navId = bundle.getInt(NAV_ID_KEY, navId)
        loadFragment(navId)
    }

    private fun loadFragment(navId: Int) {
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        when (navId) {
            R.id.nav_timeline -> {
                this.setTitle(R.string.timeline)
            }
            R.id.nav_market -> {
                this.setTitle(R.string.market)
            }
            R.id.nav_notification -> {
                this.setTitle(R.string.notification)
            }
            R.id.nav_account -> {
                this.setTitle(R.string.account)
            }
            R.id.nav_data -> {
                this.setTitle(R.string.data)
                val fragment = ManageDataFragment()
                fragmentTransaction.replace(R.id.content, fragment)
            }
        }
        fragmentTransaction.commit()
        val navigation : BottomNavigationView = findViewById(R.id.navigation)
        navigation.menu.findItem(navId).isChecked = true
        this.navId = navId
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
