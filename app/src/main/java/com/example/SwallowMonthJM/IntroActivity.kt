package com.example.SwallowMonthJM

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.SwallowMonthJM.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var startIntroThread : StartIntroThread? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topText.animation = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_left)
        binding.midText.animation = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_right)
        binding.mainIntro.animation = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.wave)
        binding.bottomIntro.animation = AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_up)

        startIntroThread = StartIntroThread()
        startIntroThread?.start()

        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }
    inner class StartIntroThread : Thread(){
        override fun run() {
            super.run()
            try {
                sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}