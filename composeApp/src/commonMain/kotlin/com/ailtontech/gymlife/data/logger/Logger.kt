package com.ailtontech.gymlife.data.logger

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object Logger {
    fun debugBuild() {
        Napier.base(DebugAntilog())
    }

    fun logDebug(message: String) {
        Napier.d(message)
    }

    fun logError(message: String) {
        Napier.e(message)
    }

    fun logVerbose(message: String) {
        Napier.v(message)
    }

    fun logWarning(message: String) {
        Napier.w(message)
    }

    fun logInfo(message: String) {
        Napier.i(message)
    }
}
