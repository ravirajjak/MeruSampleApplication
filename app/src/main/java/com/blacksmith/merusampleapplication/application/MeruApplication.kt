package com.blacksmith.merusampleapplication.application

import android.app.Application
import android.content.Context

class MeruApplication : Application() {

    companion object {
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

    }
}