package com.example.SwallowMonthJM

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.SwallowMonthJM.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private var startIntroThread2 : StartIntroThread2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        initAni()
        startIntroThread2 = StartIntroThread2()
        startIntroThread2?.start()
    }
    private fun initAni(){
        binding.topText.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_left))
        binding.midText.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_right))
        binding.mainIntro.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.wave))
        binding.bottomIntro.startAnimation(AnimationUtils.loadAnimation(this@IntroActivity, R.anim.enter_up))
    }
    inner class StartIntroThread2: Thread(){
        override fun run() {
            super.run()
            try {
                sleep(3000)
                val intent = Intent(applicationContext,LoginActivity::class.java)
                startActivity(intent)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}