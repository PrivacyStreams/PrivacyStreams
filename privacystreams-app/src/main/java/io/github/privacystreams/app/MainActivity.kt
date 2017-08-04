package io.github.privacystreams.app

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.privacystreams.app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val controllers = Controllers(this)
        binding.controllers = controllers
    }
}
