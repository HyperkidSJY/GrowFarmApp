package com.buddy.growfarm.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

class ValidateCredentials {

    companion object {
        fun validateRegisterCredentials(
            emailAddress: String, phoneNumber: String, password: String ,name : String
        ): Pair<Boolean, String> {

            var result = Pair(true, "")
            if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)){
                result = Pair(false, "Please provide the credentials")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                result = Pair(false, "Email is invalid")
            } else if (password.length <= 5) {
                result = Pair(false, "Password length should be greater than 5")
            }
//           }else if (Patterns.PHONE.matcher(phoneNumber).matches()){
//                result = Pair(false, "Phone number invalid")
//            }
            return result
        }
    }
}