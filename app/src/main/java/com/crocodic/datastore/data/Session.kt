package com.crocodic.datastore.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.crocodic.datastore.util.Cons

const val PREF_NAME = "preferenceSession"

class Session(context: Context) {

    var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var name: String
        get() = pref.getString(Cons.SHAREDPREF.KEY_NAME, "")!!
        set(s) {
            editor!!.putString(Cons.SHAREDPREF.KEY_NAME, Base64.encodeToString(s.toByteArray(), Base64.NO_WRAP))
            editor!!.apply()
        }

    var age: Int
        get() = pref.getInt(Cons.SHAREDPREF.KEY_AGE, 0)
        set(s) {
            editor!!.putInt(Cons.SHAREDPREF.KEY_AGE, s)
            editor!!.apply()
        }

    var isBoolean: Boolean
        get() = pref.getBoolean(Cons.SHAREDPREF.KEY_ISBOOLEAN, false)
        set(s) {
            editor!!.putBoolean(Cons.SHAREDPREF.KEY_ISBOOLEAN, s)
            editor!!.apply()
        }

    private var editor: SharedPreferences.Editor? = pref.edit()

}