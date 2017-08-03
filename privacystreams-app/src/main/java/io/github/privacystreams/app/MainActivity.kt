package io.github.privacystreams.app

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
