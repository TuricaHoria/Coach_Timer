package cst.baseappsetup.interfaces

import androidx.appcompat.app.AlertDialog
import cst.baseappsetup.ApplicationController
import cst.baseappsetup.R

interface OnActivityAlert {
    fun showProgressDialog()
    fun hideProgressDialog()

    fun showToast(message: String)

    fun showAlert(
        message: String,
        isPositiveButton: Boolean = true,
        isNegativeButton: Boolean = true,
        positiveButtonText: String = ApplicationController.instance.getString(R.string.alert_ok),
        negativeButtonText: String = ApplicationController.instance.getString(R.string.alert_cancel),
        actionListener: OnRequestAction? = null,
        isCancelable: Boolean = false
    ): AlertDialog?
}