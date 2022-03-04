package com.unithon.somethingnew.presentation.call

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.remotemonster.sdk.RemonCall
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityCallBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.utility.setStatusBarTransparent


class CallActivity(override val layoutResId: Int = R.layout.activity_call) :
    BaseActivity<ActivityCallBinding>() {

    var remonCall: RemonCall? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 화면 안꺼지도록 설정

        setTheme(R.style.FullScreen)
        setStatusBarTransparent(this, binding.rootView)


        with(binding) {
            backBtn.setOnClickListener {
                showToast("통화가 종료되었어요.")
                remonCall?.close()
            }
            callCloseBtn.setOnClickListener {
                showToast("통화가 종료되었어요.")
                remonCall?.close()
            }

                remonCall = RemonCall.builder()
                    .context(this@CallActivity)
                    .localView(localView)        //나의 Video Renderer
                    .remoteView(remoteView)      //상대방 video Renderer
                    .videoCodec("VP8")
                    .videoWidth(640)
                    .videoHeight(480)
                    .serviceId("SERVICEID1")    // RemoteMonster 사이트에서 등록했던 당신의 id를 입력하세요.
                    .key("1234567890")    // RemoteMonster로부터 받은 당신의 key를 입력하세요.
                    .build()
                val channelId = intent.getStringExtra("channelId")

                remonCall?.connect(channelId)
                remonCall?.onClose {
                    finish()
                }

        }

    }


    override fun onDestroy() {
        remonCall?.close()
        super.onDestroy()
    }
}