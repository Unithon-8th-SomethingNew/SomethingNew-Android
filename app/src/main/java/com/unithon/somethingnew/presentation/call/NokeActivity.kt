package com.unithon.somethingnew.presentation.call

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.core.content.ContextCompat
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

            nameTextView.text = "$name  님이\n노크했어요"
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

            binding.countText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        launch {
            while (timeCount > 0) {
                delay(1000)
                timeCount -= 1

                if (timeCount <= 3) {
                    binding.buttonContainer.setBackgroundResource(R.drawable.ic_red_box)
                    binding.nokeBackground.setBackgroundResource(R.drawable.ic_bg_noke2)
                    binding.countText.setTextColor(ContextCompat.getColor(this@NokeActivity, R.color.black))
                }
                launch(Dispatchers.Main) {
                    binding.countText.text = "${timeCount}초 뒤 들어옵니다!"
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