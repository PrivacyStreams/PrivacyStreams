package io.github.privacystreams.app

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import io.github.privacystreams.app.databinding.ActivityMainBinding
import io.github.privacystreams.app.databinding.DataMonitorBinding
import io.github.privacystreams.app.db.PStreamDBHelper


class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding
    val dbHelper = PStreamDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val controllers = Controllers(this)
        binding.controllers = controllers

        val inflater: LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        for (dbTable in dbHelper.tables) {
            dbTable.initStatus()
            val dataMonitorBinding: DataMonitorBinding = DataMonitorBinding.inflate(inflater, binding.dataDetailList, true)
            dataMonitorBinding.dbTable = dbTable
            dataMonitorBinding.dataIcon.setImageResource(dbTable.iconResId)
        }
    }
}
