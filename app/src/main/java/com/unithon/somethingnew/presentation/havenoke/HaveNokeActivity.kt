package com.unithon.somethingnew.presentation.havenoke

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import com.bumptech.glide.Glide
import com.unithon.somethingnew.R
import com.unithon.somethingnew.databinding.ActivityHaveNokeBinding
import com.unithon.somethingnew.presentation.base.BaseActivity
import com.unithon.somethingnew.presentation.call.CallActivity
import com.unithon.somethingnew.presentation.utility.setStatusBarTransparent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class HaveNokeActivity (override val layoutResId: Int = R.layout.activity_have_noke) :
    BaseActivity<ActivityHaveNokeBinding>(), CoroutineScope {

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

            nameTextView.text = "$name  님의 문을\n노크 중이에요..."
            Glide.with(this@HaveNokeActivity).load(profileUrl).into(profileImageView)
            Glide.with(this@HaveNokeActivity).load(R.raw.note).into(gifImageView)
            Glide.with(this@HaveNokeActivity).load(R.raw.ring).into(imageView2)

            cancelBtn.setOnClickListener {
                finish()
            }

            binding.countText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        launch {
            while (timeCount > 0) {
                delay(1000)
                timeCount -= 1

                launch(Dispatchers.Main) {
                    binding.countText.text = "${timeCount}초 뒤 들어갑니다!"
                }
            }

            startActivity(
                Intent(
                    this@HaveNokeActivity,
                    CallActivity::class.java
                ).putExtra("channelId", channelId)
            )
            finish()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}