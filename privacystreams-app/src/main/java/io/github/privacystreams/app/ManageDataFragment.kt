package io.github.privacystreams.app

import android.app.Fragment
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.privacystreams.app.databinding.DataManageBinding
import io.github.privacystreams.app.databinding.TableItemBinding
import io.github.privacystreams.app.db.PStreamDBHelper


class ManageDataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val dbHelper = PStreamDBHelper.getInstance(this.activity)
        val binding: DataManageBinding = DataBindingUtil.inflate(inflater, R.layout.data_manage, container, false)
        binding.dbHelper = dbHelper
        for (dbTable in dbHelper.tables) {
            dbTable.initStatus()
            val itemBinding = TableItemBinding.inflate(inflater, binding.tableList, true)
            itemBinding.dbTable = dbTable
            itemBinding.tableIcon.setImageResource(dbTable.iconResId)
        }
        return binding.root
    }
}
