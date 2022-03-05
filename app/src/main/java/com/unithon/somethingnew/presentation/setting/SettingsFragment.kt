package com.unithon.somethingnew.presentation.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.CALL_AVAILABLE
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager.Companion.KEY_UID
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.databinding.FragmentSettingsBinding
import com.unithon.somethingnew.presentation.base.BaseFragment
import com.unithon.somethingnew.presentation.login.AddressApiWebView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SettingsFragment(override val layoutResId: Int = R.layout.fragment_settings) :
    BaseFragment<FragmentSettingsBinding>(), CoroutineScope {
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>


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

            activityLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == AppCompatActivity.RESULT_OK) {
                        val address = result?.data?.getStringExtra("address")
                        prefs.setString("address", address)
                        launch {
                            MainApi().updateStreet(prefs.getLong(KEY_UID), address!!)
                            areaTextView.text = prefs.getString("address")
                        }
                    }
                }

            areaTextView.text = prefs.getString("address")

            callAreaBtn.setOnClickListener {
                val intent = Intent(requireContext(), AddressApiWebView::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activityLauncher.launch(intent) // 주소 검색 액티비티 실행
            }
        }

    }

    override fun onPause() {
        super.onPause()
        GlobalScope.launch {
            MainApi().setCall(prefs.getLong(KEY_UID), prefs.getBoolean(CALL_AVAILABLE))
        }
    }

}