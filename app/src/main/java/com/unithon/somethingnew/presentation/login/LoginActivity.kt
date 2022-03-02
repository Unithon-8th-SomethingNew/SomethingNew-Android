package com.unithon.somethingnew.presentation.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            getString(R.string.client_name)
        )
        with(binding) {
            // 네이버 로그인 콜백
            buttonOAuthLoginImg.setOAuthLoginCallback(oauthLoginCallback)
        }

    }


    /**
     * OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            // 토큰 -> NaverIdLoginSDK.getAccessToken()
        }
        override fun onFailure(httpStatus: Int, message: String) {
            Toast.makeText(this@LoginActivity,"로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            NaverIdLoginSDK.getLastErrorDescription()?.let { Log.d("login failed", it) }
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }
}