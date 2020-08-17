package com.zignyl.coachtimer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zignyl.coachtimer.R
import cst.baseappsetup.data.network.NetworkEventBus
import cst.baseappsetup.data.network.NetworkState
import cst.baseappsetup.fragment.BaseFragment
import cst.baseappsetup.helpers.extensions.logErrorMessage
import cst.baseappsetup.helpers.extensions.showDebugToast
import io.reactivex.functions.Consumer

class DetailedAthleteFragment : BaseFragment() {

    companion object {
        const val TAG = "TAG_DETAILED_USER_FRAGMENT"
        fun newInstance() = DetailedAthleteFragment()
    }

    override fun registerNetworkEventBus() =
        NetworkEventBus.register(this, Consumer { networkState ->
            when (networkState) {
                NetworkState.NoInternet ->
                    showInternetAlert()

                NetworkState.Unauthorized -> {
                    //redirect to login screen - if session expired
                }

                NetworkState.OK, NetworkState.Created, NetworkState.Accepted -> {
                    val successMessage =
                        "SUCCESS: Request result state -> ${networkState.name}. API: ${networkState.api}"
                    context?.showDebugToast(successMessage)

                    networkState.message?.let { message ->
                        mAlertCallback?.showAlert(message)
                    }
                }

                else -> {
                    "Request result state: ${networkState.name}".logErrorMessage()
                    mAlertCallback?.showAlert(
                        message = networkState.message ?: getString(R.string.alert_message_unknown),
                        isCancelable = true
                    )
                }
            }

            hideProgressDialog()
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }
}