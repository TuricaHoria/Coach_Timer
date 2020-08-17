package com.zignyl.coachtimer.activities

import android.os.Bundle
import com.zignyl.coachtimer.R
import com.zignyl.coachtimer.fragments.AthletesFragment
import com.zignyl.coachtimer.fragments.DetailedAthleteFragment
import cst.baseappsetup.activity.BaseFragmentActivity

class MainActivity : BaseFragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onReplaceFragment(AthletesFragment.TAG)
    }

    override fun getFragmentContainer(): Int? {
        return R.id.fl_frame
    }

    override fun getFragmentByTag(TAG: String) = when (TAG) {

        DetailedAthleteFragment.TAG -> {
            DetailedAthleteFragment.newInstance()
        }

        AthletesFragment.TAG -> {
            AthletesFragment.newInstance()
        }

        else -> null
    }
}