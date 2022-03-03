package com.unithon.somethingnew.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private var viewDataBinding: T? = null

    abstract val layoutResId: Int
    val binding: T by lazy {
        getViewDataBinding()!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override될 layoutResId로 data binding 객체 생성
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
        // Live data를 사용하기 위한 lifecycleOwner 지정
        viewDataBinding?.lifecycleOwner = this@BaseActivity
    }


    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding = null
    }

    // binding 객체를 가져오는 메소드
    private fun getViewDataBinding(): T? = viewDataBinding

    // 토스트 띄우기
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // 스낵바 띄우기
    fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }


}