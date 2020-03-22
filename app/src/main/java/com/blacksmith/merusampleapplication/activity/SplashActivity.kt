package com.blacksmith.merusampleapplication.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.blacksmith.merusampleapplication.R


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {

        Handler().postDelayed({
            openIntent(MainActivity.newInstance())
        }, 3000)
    }

    private fun openIntent(mClass: Class<*>) {


        val intent = Intent(this, mClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}
