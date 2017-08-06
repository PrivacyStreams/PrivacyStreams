package io.github.privacystreams.app.db

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import java.util.*

class PStreamTableStatus {
    val message = ObservableField<String>("")
    val isCollecting = ObservableBoolean(false)
    val numItems = ObservableInt(0)

    fun increaseNumItems() {
        numItems.set(numItems.get() + 1)
    }
}
