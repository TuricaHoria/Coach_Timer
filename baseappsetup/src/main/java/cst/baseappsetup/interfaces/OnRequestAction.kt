package cst.baseappsetup.interfaces

interface OnRequestAction {
    fun requestPositiveAction()
    fun requestNegativeAction() {}

    fun requestNoInternetAction(): Unit? = null
}