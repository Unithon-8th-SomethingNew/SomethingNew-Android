package com.unithon.somethingnew.presentation.call

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import com.bumptech.glide.Glide
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityNokeBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.utility.setStatusBarTransparent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NokeActivity(override val layoutResId: Int = R.layout.activity_noke) :
    BaseActivity<ActivityNokeBinding>(), CoroutineScope {

    var timeCount = 7
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.FullScreen)
        setStatusBarTransparent(this, binding.rootView)
        job = Job()

        val name = intent.getStringExtra("name")
        val channelId = intent.getStringExtra("channelId")
        val profileUrl = intent.getStringExtra("profileUrl")

        with(binding) {

            nameTextView.text = "$name 님이\n노크했어요"
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

        launch {
            while(timeCount > 0) {
                delay(1000)
                timeCount -= 1

                launch(Dispatchers.Main) {
                    binding.acceptTextView.text = "대화 참여하기 (${timeCount}초 뒤 자동 연결)"
                }
            }

            startActivity(
                Intent(
                    this@NokeActivity,
                    CallActivity::class.java
                ).putExtra("channelId", channelId)
            )
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}