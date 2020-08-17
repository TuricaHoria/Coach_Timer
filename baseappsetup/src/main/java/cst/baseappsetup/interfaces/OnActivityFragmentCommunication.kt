package cst.baseappsetup.interfaces

import android.os.Bundle
import androidx.fragment.app.Fragment

interface OnActivityFragmentCommunication {
    fun onAddFragment(TAG: String, bundle: Bundle? = null, animated: Boolean = false, addToBackStack: Boolean = true)
    fun onReplaceFragment(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = true,
        targetFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    )

    fun onRemoveFragment(TAG: String, animated: Boolean = true)
    fun onPopFragment()
    fun setActionBarTitle(title: String, isForced: Boolean = false)
}