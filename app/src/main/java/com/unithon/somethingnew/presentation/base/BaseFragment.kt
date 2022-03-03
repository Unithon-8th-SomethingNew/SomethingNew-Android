package com.unithon.somethingnew.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private var viewDataBinding: T? = null

    abstract val layoutResId: Int
    val binding: T by lazy {
        getViewDataBinding()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Override될 layoutResId로 data binding 객체 생성
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        // Live data를 사용하기 위한 lifecycleOwner 지정
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        return viewDataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding = null
    }

    // binding 객체를 가져오는 메소드
    fun getViewDataBinding(): T? = viewDataBinding

    // 토스트 띄우기
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    // 스낵바 띄우기
    fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }
}