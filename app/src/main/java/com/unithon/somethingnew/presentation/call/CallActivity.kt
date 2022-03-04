package com.unithon.somethingnew.presentation.call

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.remotemonster.sdk.RemonCall
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityCallBinding
import com.unithon.somethingnew.presentation.base.BaseActivity


class CallActivity(override val layoutResId: Int = R.layout.activity_call) :
    BaseActivity<ActivityCallBinding>() {

    var remonCall: RemonCall? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 화면 안꺼지도록 설정

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                // 접근허용 시 실행할 코드
                with(binding) {
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
                    val channelId = "unithon"//intent.getStringExtra("channelId")

                    remonCall?.connect(channelId)
                    remonCall?.onClose {
                        finish()
                    }
                }
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                // 접근거부 시 실행할 코드
                showToast("권한을 허용해주세요!")
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("접근 거부하셨습니다.\n[설정] - [권한]에서 권한을 허용해주세요.")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            .check()

    }


    override fun onDestroy() {
        remonCall?.close()
        super.onDestroy()
    }
}