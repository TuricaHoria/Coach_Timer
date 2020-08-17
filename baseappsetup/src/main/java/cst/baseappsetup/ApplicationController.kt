package cst.baseappsetup

import android.app.Application
import cst.baseappsetup.helpers.LifecycleHandler

abstract class ApplicationController : Application() {

    companion object {
        lateinit var instance: ApplicationController
    }

    val lifecycleHandler by lazy {
        LifecycleHandler()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        registerActivityLifecycleCallbacks(lifecycleHandler)
    }

}
