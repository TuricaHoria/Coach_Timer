package cst.baseappsetup.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cst.baseappsetup.R
import cst.baseappsetup.fragment.BaseFragment
import cst.baseappsetup.helpers.Constants.FragmentRelated.TARGET_FRAGMENT_REQUEST_CODE
import cst.baseappsetup.helpers.extensions.showCustomAlert
import cst.baseappsetup.interfaces.OnActivityAlert
import cst.baseappsetup.interfaces.OnActivityFragmentCommunication
import cst.baseappsetup.interfaces.OnRequestAction
import cst.baseappsetup.widget.LoadingDialog

abstract class BaseFragmentActivity : AppCompatActivity(), OnActivityAlert,
    OnActivityFragmentCommunication {

    private var mProgressDialog: LoadingDialog? = null
    protected var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        setupViews()

        mFragmentManager = this.supportFragmentManager
    }

    override fun onBackPressed() {
        val fragmentsCount = mFragmentManager?.backStackEntryCount ?: 0

        if (fragmentsCount <= 1) {
            finish()
            return
        }

        super.onBackPressed()
    }

    /**
     * Function used to initialize the activity_welcome's views
     */
    private fun initViews() {
        mProgressDialog = LoadingDialog(this)
    }

    private fun setupViews() {
        val pd = mProgressDialog
        if (pd != null) {
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pd.setContentView(R.layout.item_alert_progress)
            pd.setCancelable(false)

            pd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /*************************************************************
     * Mark: ALERT
     *************************************************************/

    /**
     * Function called to show the progress spinner - [.mProgressDialog]
     */
    override fun showProgressDialog() {
        val pd = mProgressDialog
        if (pd != null && !pd.isShowing) {
            runOnUiThread { pd.show() }
        }
    }

    /**
     * Function called to hide the progress spinner - [.mProgressDialog]
     */
    override fun hideProgressDialog() {
        val pd = mProgressDialog
        if (pd != null && pd.isShowing) {
            runOnUiThread { pd.dismiss() }
        }
    }

    override fun showAlert(
        message: String,
        isPositiveButton: Boolean,
        isNegativeButton: Boolean,
        positiveButtonText: String,
        negativeButtonText: String,
        actionListener: OnRequestAction?,
        isCancelable: Boolean
    ) = this.showCustomAlert(
        message = message,
        isPositiveButton = isPositiveButton,
        isNegativeButton = isNegativeButton,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        actionListener = actionListener,
        isCancelable = isCancelable
    )

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /*************************************************************
     * Mark: OnActivityFragmentCommunication
     */
    private enum class FragmentActions {
        ADD,
        REPLACE,
        REMOVE
    }

    override fun onAddFragment(
        TAG: String,
        bundle: Bundle?,
        animated: Boolean,
        addToBackStack: Boolean
    ) {
        onCreateFragmentAction(TAG, FragmentActions.ADD, bundle, animated, addToBackStack = addToBackStack)
    }

    override fun onReplaceFragment(
        TAG: String,
        bundle: Bundle?,
        animated: Boolean,
        targetFragment: Fragment?,
        targetFragmentRequestCode: Int?,
        addToBackStack: Boolean
    ) {
        onCreateFragmentAction(
            TAG,
            FragmentActions.REPLACE,
            bundle,
            animated,
            targetFragment,
            targetFragmentRequestCode,
            addToBackStack
        )
    }

    override fun onRemoveFragment(TAG: String, animated: Boolean) {
        onCreateFragmentAction(TAG, FragmentActions.REMOVE, animated = animated)
    }

    override fun onPopFragment() {
        mFragmentManager?.popBackStack()
    }

    override fun setActionBarTitle(title: String, isForced: Boolean) {
        if(isForced) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        supportActionBar?.title = title
    }

    /*************************************************************
     * Mark: Fragment actions
     *************************************************************/

    protected abstract fun getFragmentContainer(): Int?

    private fun onAddFragment(fragment: BaseFragment, TAG: String, animated: Boolean, addToBackStack: Boolean) {
        val fm = mFragmentManager ?: return
        val fragmentContainer = getFragmentContainer() ?: return

        val transaction = fm.beginTransaction()

        if (animated) {
            transaction.setCustomAnimations(
                getFragmentTransitionAnimRightToLeft()[0],
                getFragmentTransitionAnimRightToLeft()[1],
                getFragmentTransitionAnimLeftToRight()[0],
                getFragmentTransitionAnimLeftToRight()[1]
            )
        }

        val addTransaction = transaction.add(
            fragmentContainer,
            fragment,
            TAG
        )

        when (addToBackStack) {
            true -> addTransaction.addToBackStack(TAG)
        }
        addTransaction.commit()
    }

    private fun onReplaceFragment(fragment: BaseFragment, TAG: String, animated: Boolean, addToBackStack: Boolean) {
        val fm = mFragmentManager ?: return
        val fragmentContainer = getFragmentContainer() ?: return

        val transaction = fm.beginTransaction()
        if (animated) {
            transaction.setCustomAnimations(
                getFragmentTransitionAnimRightToLeft()[0],
                getFragmentTransitionAnimRightToLeft()[1],
                getFragmentTransitionAnimLeftToRight()[0],
                getFragmentTransitionAnimLeftToRight()[1]
            )
        }

        val replaceTransaction = transaction.replace(
            fragmentContainer,
            fragment,
            TAG
        )

        when (addToBackStack) {
            true -> replaceTransaction.addToBackStack(TAG)
        }
        replaceTransaction.commit()
    }

    private fun onRemoveFragment(fragment: BaseFragment, animated: Boolean) {
//        val fm = mFragmentManager ?: return
//
//        val mFragmentTransaction =
//            fm.beginTransaction().remove(fragment)
//
//        mFragmentTransaction.commit()

        mFragmentManager?.popBackStack()
    }

    private fun onCreateFragmentAction(
        TAG: String,
        fragmentAction: FragmentActions,
        bundle: Bundle? = null,
        animated: Boolean,
        targetFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    ) {
        val fragment = getFragmentByTag(TAG) ?: return
        fragment.arguments = bundle
        targetFragment?.let { target ->
            fragment.setTargetFragment(
                target,
                targetFragmentRequestCode ?: TARGET_FRAGMENT_REQUEST_CODE
            )
        }

        when (fragmentAction) {
            FragmentActions.ADD -> onAddFragment(fragment, TAG, animated, addToBackStack)
            FragmentActions.REPLACE -> onReplaceFragment(fragment, TAG, animated, addToBackStack)
            FragmentActions.REMOVE -> onRemoveFragment(fragment, animated)
        }
    }

    protected abstract fun getFragmentByTag(TAG: String): BaseFragment?

    protected fun getFragmentTransitionAnimRightToLeft(): IntArray =
        intArrayOf(R.anim.enter_from_right, R.anim.exit_to_left)

    protected fun getFragmentTransitionAnimLeftToRight(): IntArray =
        intArrayOf(R.anim.enter_from_left, R.anim.exit_to_right)

    /*************************************************************
     * Mark: Keyboard
     *************************************************************/

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus
        currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}