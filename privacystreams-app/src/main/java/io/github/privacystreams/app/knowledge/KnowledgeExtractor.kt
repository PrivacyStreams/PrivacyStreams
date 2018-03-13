package io.github.privacystreams.app.knowledge

import android.content.Context
import io.github.privacystreams.app.db.PStreamDBHelper
import io.github.privacystreams.core.purposes.Purpose

/**
 * A knowledge extractor that extracts personal knowledge from low-level data sources
 */
abstract class KnowledgeExtractor(val context: Context) {
    val myPurpose = Purpose.UTILITY("Generate personal knowledge.")
    abstract fun run()
}
