package cst.baseappsetup.helpers.extensions

import android.util.Log
import cst.baseappsetup.BuildConfig
import cst.baseappsetup.ApplicationController

fun String.logErrorMessage(TAG: String = ApplicationController.instance.packageName) {
    when (BuildConfig.DEBUG) {
        true -> Log.e(TAG, this)
    }
}
