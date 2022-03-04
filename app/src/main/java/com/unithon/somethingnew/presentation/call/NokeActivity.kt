package com.unithon.somethingnew.presentation.call

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import com.bumptech.glide.Glide
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityNokeBinding
import com.unithon.somethingnew.presentation.base.BaseActivity

class NokeActivity(override val layoutResId: Int = R.layout.activity_noke) :
    BaseActivity<ActivityNokeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("name")
        val channelId = intent.getStringExtra("channelId")
        val profileUrl = intent.getStringExtra("profileUrl")

        with(binding) {
            val span = StringBuilder("$name 님이\n노크했어요.") as Spannable
            span.setSpan(
                RelativeSizeSpan(1.5f),
                0,
                name?.length!!,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            nameTextView.text = span.toString()
            Glide.with(this@NokeActivity).load(profileUrl).into(profileImageView)

            acceptCallBtn.setOnClickListener {
                finish()
                startActivity(
                    Intent(
                        this@NokeActivity,
                        CallActivity::class.java
                    ).putExtra("channelId", channelId)
                )
            }

            deceptCallBtn.setOnClickListener {
                finish()
            }
        }
    }
}