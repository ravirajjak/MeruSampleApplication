package io.sugarbox.sbapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.blacksmith.merusampleapplication.application.MeruApplication

class SharedPreferences {

    fun setStringPreference(key: String, value: String) {


        val sp = getPreferenceModePrivate(MeruApplication.mContext, key)
        val editor = sp.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setBooleanPreference(key: String, value: Boolean) {

        val sp = getPreferenceModePrivate(MeruApplication.mContext, key)
        val editor = sp.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }


    fun getBooleanPreference(key: String): Boolean {

        //        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        val sp = getPreferenceModePrivate(MeruApplication.mContext, key)
        return sp.getBoolean(key, false)

    }


    private fun getPreferenceModePrivate(mContext: Context, key: String): SharedPreferences {
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    fun getStringPreference(key: String): String? {

        val shared = getPreferenceModePrivate(MeruApplication.mContext, key)
        return shared.getString(key, null)

    }

    fun removePref(key: String) {
        val shared = getPreferenceModePrivate(MeruApplication.mContext, key)
        shared.edit().remove(key).apply()

    }
}