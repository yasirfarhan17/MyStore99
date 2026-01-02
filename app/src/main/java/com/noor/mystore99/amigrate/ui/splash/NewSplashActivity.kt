package com.noor.mystore99.amigrate.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.noor.mystore99.R
import com.noor.mystore99.amigrate.ui.auth.login.LoginActivity

class NewSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_splash)
        
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        // Animation
        val textView = findViewById<android.widget.TextView>(R.id.text)
        textView.alpha = 0f
        textView.translationY = 50f
        textView.animate().alpha(1f).translationY(0f).setDuration(1000).setStartDelay(300).start()

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        },4000)
    }
}