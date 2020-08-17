package cst.baseappsetup.helpers.extensions

import android.content.Context
import cst.baseappsetup.data.models.Session
import cst.baseappsetup.helpers.UtilsSharedPreferences

fun Context.saveSession(session: Session) {
    UtilsSharedPreferences.saveSessionToken(this, session)
}

fun Context.removeSession() {
    UtilsSharedPreferences.removeSessionToken(this)
}