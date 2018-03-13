package io.github.privacystreams.app.knowledge

import android.content.Context
import io.github.privacystreams.app.db.TableNotification
import io.github.privacystreams.core.Callback
import io.github.privacystreams.core.Item
import io.github.privacystreams.core.UQI
import io.github.privacystreams.utils.Logging

/**
 * Personal knowledge extraction from push notifications
 */

class NotificationKnowledgeExtractor(context: Context) : KnowledgeExtractor(context) {
    override fun run() {
        UQI(context).getData(TableNotification.PROVIDER(), myPurpose)
                .groupBy(TableNotification.PACKAGE_NAME)
                .forEach(object : Callback<Item>() {
                    override fun onInput(input: Item) {
                        val packageName = input.getAsString(TableNotification.PACKAGE_NAME)
                        val logMsg = "Extracting knowledge from app: $packageName"
                        Logging.debug(logMsg)
                        // TODO implement this
                    }

                })

    }

}
