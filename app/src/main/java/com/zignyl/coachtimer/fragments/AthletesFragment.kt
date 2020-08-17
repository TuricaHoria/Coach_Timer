package com.zignyl.coachtimer.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zignyl.coachtimer.R
import com.zignyl.coachtimer.adapters.AthletesAdapter
import com.zignyl.coachtimer.api.APIClientRepository
import com.zignyl.coachtimer.models.User
import cst.baseappsetup.data.network.NetworkEventBus
import cst.baseappsetup.data.network.NetworkState
import cst.baseappsetup.fragment.BaseFragment
import cst.baseappsetup.helpers.extensions.addTo
import cst.baseappsetup.helpers.extensions.logErrorMessage
import cst.baseappsetup.helpers.extensions.showDebugToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_page.*

class AthletesFragment : BaseFragment() {

    companion object {
        const val TAG = "TAG_ATHLETES_FRAGMENT"
        fun newInstance() = AthletesFragment()
    }

    private lateinit var viewAdapter: AthletesAdapter


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

    override fun onResume() {
        super.onResume()
        getAthletes()
    }


    private fun getAthletes() {
        showProgressDialog()
        APIClientRepository.getAthletes()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                setupRecyclerView(it.results)
            },
                { error ->
                    Log.e(TAG, error.message)
                }
            ).addTo(autoDisposable)
    }


    private fun setupRecyclerView(athletes: MutableList<User>) {
        viewAdapter = AthletesAdapter(athletes) {
            val bundle = Bundle()
            bundle.putInt("athleteId", it)
            onReplaceFragmentByTAG(DetailedAthleteFragment.TAG, bundle)
        }
        val viewManager = LinearLayoutManager(context)

        rv_athletes.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}