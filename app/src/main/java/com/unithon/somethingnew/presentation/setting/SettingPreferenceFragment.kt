package com.unithon.somethingnew.presentation.setting

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.unithon.somethingnew.App
import com.unithon.somethingnew.R

class SettingPreferenceFragment : PreferenceFragmentCompat() {
    // 저장되는 데이터에 접근하기 위한 SharedPreferences
    lateinit var prefs: SharedPreferences

    // Preference 객체
    var callEnablePreference: Preference? = null
    var locationPreference: Preference? = null

    // onCreate() 중에 호출되어 Fragment에 preference를 제공하는 메서드
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // preference xml을 inflate하는 메서드
        setPreferencesFromResource(R.xml.settings_preference, rootKey)

        // rootKey가 null 이라면
        if (rootKey == null) {
            // Preference 객체 초기화
            callEnablePreference = findPreference("call_enable")
            callEnablePreference = findPreference("location")

            // SharedPreferences 객체 초기화
            prefs = PreferenceManager.getDefaultSharedPreferences(App.context())

            // sound_list라는 key로 저장된 값이 비어있지 않다면
            if (prefs.getString("getaddres", "") != "") {
                locationPreference?.summary = prefs.getString("getaddres", "")
            }

        }
    }

    // 설정 변경 이벤트 처리
    val prefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences?, key: String? ->

            when (key) {
                "call_enable" -> {
                    when(prefs.getString("call_enable", "")){

                    }
                }

            }
        }

    // 리스너 등록
    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    // 리스너 해제
    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
    }
}