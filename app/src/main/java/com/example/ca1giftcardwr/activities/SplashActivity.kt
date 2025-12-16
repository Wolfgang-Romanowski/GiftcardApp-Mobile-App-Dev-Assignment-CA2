package com.example.ca1giftcardwr.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.ca1giftcardwr.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        animateSplash()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GiftcardList::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500)
    }

    private fun animateSplash() {
        val icon = binding.splashIcon
        icon.scaleX = 0f
        icon.scaleY = 0f
        icon.alpha = 0f

        val scaleX = ObjectAnimator.ofFloat(icon, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, 0f, 1f)
        val alpha = ObjectAnimator.ofFloat(icon, View.ALPHA, 0f, 1f)

        val iconAnimator = AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 600
            interpolator = OvershootInterpolator(1.5f)
        }

        val title = binding.splashTitle
        title.translationY = 50f
        title.alpha = 0f

        val titleTranslate = ObjectAnimator.ofFloat(title, View.TRANSLATION_Y, 50f, 0f)
        val titleAlpha = ObjectAnimator.ofFloat(title, View.ALPHA, 0f, 1f)

        val titleAnimator = AnimatorSet().apply {
            playTogether(titleTranslate, titleAlpha)
            duration = 500
            startDelay = 300
        }

        val subtitle = binding.splashSubtitle
        subtitle.translationY = 30f
        subtitle.alpha = 0f

        val subtitleTranslate = ObjectAnimator.ofFloat(subtitle, View.TRANSLATION_Y, 30f, 0f)
        val subtitleAlpha = ObjectAnimator.ofFloat(subtitle, View.ALPHA, 0f, 1f)

        val subtitleAnimator = AnimatorSet().apply {
            playTogether(subtitleTranslate, subtitleAlpha)
            duration = 500
            startDelay = 500
        }

        val progress = binding.splashProgress
        progress.alpha = 0f

        val progressAlpha = ObjectAnimator.ofFloat(progress, View.ALPHA, 0f, 1f).apply {
            duration = 400
            startDelay = 800
        }
        iconAnimator.start()
        titleAnimator.start()
        subtitleAnimator.start()
        progressAlpha.start()
    }
}