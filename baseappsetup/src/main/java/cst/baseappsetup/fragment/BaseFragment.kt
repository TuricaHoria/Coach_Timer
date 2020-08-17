package cst.baseappsetup.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import cst.baseappsetup.R
import cst.baseappsetup.data.lifecycle.AutoDisposable
import cst.baseappsetup.data.network.NetworkEventBus
import cst.baseappsetup.helpers.utils.KeyboardEventListener
import cst.baseappsetup.interfaces.OnActivityAlert
import cst.baseappsetup.interfaces.OnActivityFragmentCommunication

abstract class BaseFragment : Fragment() {

    protected var mAlertCallback: OnActivityAlert? = null
    private var mHandleFragmentsCallback: OnActivityFragmentCommunication? = null

    protected val autoDisposable by lazy {
        AutoDisposable()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnActivityFragmentCommunication) {
            mHandleFragmentsCallback = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }

        if (context is OnActivityAlert) {
            mAlertCallback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mHandleFragmentsCallback = null
        mAlertCallback = null
    }

    override fun onStart() {
        super.onStart()

        autoDisposable.bindTo(this.lifecycle)
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()

        registerNetworkEventBus()
    }

    override fun onPause() {
        super.onPause()

        NetworkEventBus.unregister(this)
    }

    /**
     * Handle fragments by tag
     */
    protected fun onAddFragmentByTAG(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = false,
        addToBackStack: Boolean = true
    ) =
        mHandleFragmentsCallback?.onAddFragment(TAG, bundle, animated, addToBackStack)

    protected fun onReplaceFragmentByTAG(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = true,
        targetFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    ) =
        mHandleFragmentsCallback?.onReplaceFragment(
            TAG,
            bundle,
            animated,
            targetFragment,
            targetFragmentRequestCode,
            addToBackStack
        )

    protected fun onRemoveFragmentByTAG(TAG: String) =
        mHandleFragmentsCallback?.onRemoveFragment(TAG)

    protected fun onPopFragment() =
        mHandleFragmentsCallback?.onPopFragment()

    protected fun setActionBarTitle(title: String, isForced: Boolean = false) =
        mHandleFragmentsCallback?.setActionBarTitle(title, isForced)

    /**
     * Show alerts
     */
    protected fun showProgressDialog() =
        mAlertCallback?.showProgressDialog()

    protected fun hideProgressDialog() =
        mAlertCallback?.hideProgressDialog()

    protected fun showInternetAlert() = mAlertCallback?.showAlert(
        message = getString(R.string.alert_message_no_internet),
        isNegativeButton = false
    )


    /**
     * Hide keyboard
     */
    fun hideKeyboardFrom(view: View) {
        val ctx = context ?: return
        val imm = ctx.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun registerKeyboardListener(consumeKeyboardVisibility: (Boolean) -> Unit) =
        activity?.run {
            KeyboardEventListener(this) { isOpen ->
                consumeKeyboardVisibility.invoke(isOpen)
            }
        }

    /**
     * Handle network errors
     */
    protected abstract fun registerNetworkEventBus()
}