package com.unithon.somethingnew.presentation.setting

import android.os.Bundle
import android.view.View
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.CALL_AVAILABLE
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_UID
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.databinding.FragmentSettingsBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SettingsFragment(override val layoutResId: Int = R.layout.fragment_settings) :
    BaseFragment<FragmentSettingsBinding>(), CoroutineScope {
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // 저장되는 데이터에 접근하기 위한 PreferenceManager
    private lateinit var prefs: PreferenceManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferenceManager(requireContext())
        job = Job()

        with(binding) {
            switchCallAvailable.isChecked = prefs.getBoolean(CALL_AVAILABLE, true)
            switchCallAvailable.setOnCheckedChangeListener { buttonView, isChecked ->
                prefs.setBoolean(CALL_AVAILABLE, isChecked)
            }

            areaTextView.text = prefs.getString("address")
        }

    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch {
            MainApi().setCall(prefs.getLong(KEY_UID), prefs.getBoolean(CALL_AVAILABLE))
        }
    }

}