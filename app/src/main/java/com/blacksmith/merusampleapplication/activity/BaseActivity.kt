package com.blacksmith.merusampleapplication.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blacksmith.merusampleapplication.utils.Util
import com.google.gson.Gson
import io.sugarbox.sbapp.utils.SharedPreferences

open class BaseActivity : AppCompatActivity() {

    val mUtil by lazy { Util() }
    val mPref by lazy { SharedPreferences() }
    val gson by lazy { Gson() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
