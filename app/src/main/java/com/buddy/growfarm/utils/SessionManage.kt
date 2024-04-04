package com.buddy.growfarm.utils

import android.content.Context
import com.buddy.growfarm.utils.Constants.IS_LOGIN
import com.buddy.growfarm.utils.Constants.PREFS_KEY
import com.buddy.growfarm.utils.Constants.USER_ID
import com.buddy.growfarm.utils.Constants.USER_PHONE
import com.buddy.growfarm.utils.Constants.USER_VERIFY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManage @Inject constructor(@ApplicationContext context : Context) {

    private var prefs = context.getSharedPreferences(PREFS_KEY,Context.MODE_PRIVATE)

    fun saveUserID(userid : String){
        val editor = prefs.edit()
        editor.putString(USER_ID,userid)
        editor.apply()
    }

    fun getUserID() : String? {
        return prefs.getString(USER_ID,null)
    }

    fun saveUserPhone(phone : String){
        val editor = prefs.edit()
        editor.putString(USER_PHONE,phone)
        editor.apply()
    }

    fun getPhone() : String? {
        return prefs.getString(USER_PHONE,null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(IS_LOGIN, isLoggedIn).apply()
    }

    fun sessionClear(){
        val editor = prefs.edit()
        editor.remove(USER_ID)
        editor.remove(USER_PHONE)
        editor.putBoolean(IS_LOGIN, false)
        editor.apply()
    }

//    fun saveVerify(verify : String){
//        val editor = prefs.edit()
//        editor.putString(USER_VERIFY,verify)
//        editor.apply()
//    }
//
//    fun getVerify() : String? {
//        return prefs.getString(USER_VERIFY, null)
//    }
}