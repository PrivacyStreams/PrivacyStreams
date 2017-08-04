package io.github.privacystreams.app.providers

import io.github.privacystreams.core.PStreamProvider

/**
 * PrivacyStreams historic data
 */
class PStreamHistory {
    companion object {
        fun locations(): PStreamProvider {
            return HistoricLocationProvider()
        }
    }
}
