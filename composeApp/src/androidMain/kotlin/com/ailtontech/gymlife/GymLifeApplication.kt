package com.ailtontech.gymlife

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class GymLifeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())
    }
}
