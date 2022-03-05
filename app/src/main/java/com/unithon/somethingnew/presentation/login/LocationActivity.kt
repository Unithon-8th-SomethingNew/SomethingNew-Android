package com.unithon.somethingnew.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.databinding.ActivityLocationBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.main.MainActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LocationActivity(override val layoutResId: Int = R.layout.activity_location) :
    BaseActivity<ActivityLocationBinding>(), View.OnClickListener, CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private var address = MutableLiveData<String>()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        with(binding) {
            locationTextView.setOnClickListener(this@LocationActivity)
            doneBtn.setOnClickListener(this@LocationActivity)
            deleteBtn.setOnClickListener(this@LocationActivity)
            researchBtn.setOnClickListener(this@LocationActivity)

            preferenceManager = PreferenceManager(this@LocationActivity)
            activityLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK) {
                        address.value = result?.data?.getStringExtra("address")
                        Log.d("Main", address.toString())
                    }
                }

            address.observe(this@LocationActivity) {
                if (it.isNullOrEmpty().not()) {
                    locationTextView.text = it
                    divider.visibility = View.GONE
                    deleteBtn.visibility = View.VISIBLE
                    doneBtn.isClickable = true
                    doneBtn.isEnabled = true
                    searchIcon.visibility = View.GONE
                    researchBtn.visibility = View.VISIBLE
                } else {
                    locationTextView.text = null
                    divider.visibility = View.VISIBLE
                    deleteBtn.visibility = View.GONE
                    doneBtn.isClickable = false
                    doneBtn.isEnabled = false
                    searchIcon.visibility = View.VISIBLE
                    researchBtn.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.delete_btn -> {
                address.value = ""
            }
            R.id.location_text_view -> {
                val intent = Intent(this, AddressApiWebView::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activityLauncher.launch(intent) // 주소 검색 액티비티 실행
            }
            R.id.research_btn -> {
                address.value = ""
                val intent = Intent(this, AddressApiWebView::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activityLauncher.launch(intent) // 주소 검색 액티비티 실행
            }
            R.id.done_btn -> {
                binding.progressBar.visibility = View.VISIBLE
                if (preferenceManager.getString(PreferenceManager.KEY_LOGIN_TYPE) == "NAVER") {
                    launch(Dispatchers.IO) {
                        val isLoginSuccess =
                            MainApi().loginNaver(
                                preferenceManager.getAccessToken(),
                                preferenceManager.getFcmAccessToken(),
                                address.value!!
                            )

                        if (isLoginSuccess) {
                            finish()
                            startActivity(Intent(this@LocationActivity, MainActivity::class.java))
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    launch(Dispatchers.IO) {
                        val isLoginSuccess =
                            MainApi().loginKakao(
                                preferenceManager.getAccessToken(),
                                preferenceManager.getFcmAccessToken(),
                                address.value!!
                            )

                        if (isLoginSuccess) {
                            finish()
                            startActivity(Intent(this@LocationActivity, MainActivity::class.java))
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                }

            }
        }
    }
}
