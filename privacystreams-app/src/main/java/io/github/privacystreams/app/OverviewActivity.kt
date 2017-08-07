package io.github.privacystreams.app

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import io.github.privacystreams.app.databinding.OverviewBinding
import io.github.privacystreams.app.databinding.DataMonitorBinding
import io.github.privacystreams.app.db.PStreamDBHelper


class OverviewActivity : AppCompatActivity() {
    internal lateinit var binding: OverviewBinding
    val dbHelper = PStreamDBHelper.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.overview)
        binding = DataBindingUtil.setContentView<OverviewBinding>(this, R.layout.overview)
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
