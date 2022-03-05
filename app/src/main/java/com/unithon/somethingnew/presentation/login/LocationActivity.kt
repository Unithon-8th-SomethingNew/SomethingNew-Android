package com.unithon.somethingnew.presentation.login

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
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
            addBtn.setOnClickListener(this@LocationActivity)
            doneBtn.setOnClickListener(this@LocationActivity)

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
                    doneTextView.text = "확인"
                    doneBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#007AFF"))
                    locationEditText.setText(it)
                    doneBtn.isClickable = true
                    doneBtn.isEnabled = true
                } else {
                    doneTextView.text = "건너뛰기"
                    doneBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#DCDCDC"))
                    locationEditText.text = null
                    doneBtn.isClickable = false
                    doneBtn.isEnabled = false
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_btn -> {
                val intent = Intent(this, AddressApiWebView::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                activityLauncher.launch(intent) // 주소 검색 액티비티 실행
            }
            R.id.done_btn -> {
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
                    }
                }

            }
        }
    }
}
