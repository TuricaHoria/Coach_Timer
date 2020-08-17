package com.zignyl.coachtimer

import cst.baseappsetup.ApplicationController

class ApplicationController : ApplicationController() {

    companion object {
        lateinit var instance: ApplicationController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}