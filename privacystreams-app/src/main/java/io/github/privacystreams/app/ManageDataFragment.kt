package io.github.privacystreams.app

import android.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.privacystreams.app.databinding.DataManageBinding
import io.github.privacystreams.app.databinding.TableItemBinding
import io.github.privacystreams.app.db.PStreamDBHelper


class ManageDataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dbHelper = PStreamDBHelper.getInstance(this.activity)
        val binding: DataManageBinding = DataBindingUtil.inflate(inflater, R.layout.data_manage, container, false)
        val controllers = Controllers(this.activity)
        binding.controllers = controllers
        for (dbTable in dbHelper.tables) {
            dbTable.initStatus()
            val itemBinding = TableItemBinding.inflate(inflater, binding.dataDetailList, true)
            itemBinding.dbTable = dbTable
            itemBinding.dataIcon.setImageResource(dbTable.iconResId)
        }
        return binding.root
    }
}
