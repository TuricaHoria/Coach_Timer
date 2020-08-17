package cst.baseappsetup.helpers

import android.content.Context
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi



object UtilsPermissions {

    private const val CAMERA_PERMISSION = "camera_permission"
    private const val STORAGE_PERMISSION = "storage_permission"

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        val prevShouldShowStatus = getPermissionPreference(activity, permission)
        val currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission)
        return prevShouldShowStatus != currShouldShowStatus
    }

    fun savePermissionPreference(context: Context, permission: String) {
        val editor = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        editor.putBoolean(permission, true)
        editor.apply()
    }

    fun getPermissionPreference(context: Context?, permission: String): Boolean? {
        val ctx = context ?: return null
        val sharedPreferences = ctx.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(permission, false)
    }
}