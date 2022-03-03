package com.unithon.somethingnew.presentation.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dnd.sixth.lmsservice.data.preference.PreferenceManager
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.unithon.somethingnew.R
import com.unithon.somethingnew.data.network.MainApi
import com.unithon.somethingnew.databinding.ActivityLoginBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity(override val layoutResId: Int = R.layout.activity_login) :
    BaseActivity<ActivityLoginBinding>() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this)

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

        //카카오톡 api 초기화
        KakaoSdk.init(this, getString(R.string.KAKAO_NATIVE_APP_KEY))

        binding.KakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
    }

    //카카오톡 로그인 callback
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    private fun kakaoLogin() {
        //카카오 hash 키 등록을 위한 log
        // Log.d("id", Utility.getKeyHash(this))

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    preferenceManager.putAccessToken(token.accessToken) // Access Token을 저장합니다.
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${preferenceManager.getAccessToken()}")

                    CoroutineScope(Dispatchers.IO).launch {
                        val isLoginSuccess = MainApi().login()
                        if (isLoginSuccess) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        } else {
                            showSnackBar("로그인에 실패하였어요!")
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }


    /**
     * OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            // 토큰 -> NaverIdLoginSDK.getAccessToken()
            NaverIdLoginSDK.getAccessToken()?.let { preferenceManager.putAccessToken(it) }
            Log.i(ContentValues.TAG, "네이버로 로그인 성공 ${preferenceManager.getAccessToken()}")

            CoroutineScope(Dispatchers.IO).launch {
                val isLoginSuccess = MainApi().login()
                if (isLoginSuccess) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    showSnackBar("로그인에 실패하였어요!")
                }
            }
        }

        override fun onFailure(httpStatus: Int, message: String) {
            Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            NaverIdLoginSDK.getLastErrorDescription()?.let { Log.d("login failed", it) }
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }
}