package com.crocodic.datastore.base

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.crocodic.datastore.data.Session
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerAppCompatActivity

open class NoViewModelActivity : DaggerAppCompatActivity() {

    lateinit var session : Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = Session(this)

    }

    fun getScreenSize(): HashMap<String, Int> {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val hashMap = HashMap<String, Int>()
        hashMap["width"] = size.x
        hashMap["height"] = size.y

        return hashMap
    }

    fun textOf(e: EditText?): String {
        return e?.text.toString().trim()
    }


    fun snacked(view: View, message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, message, duration).show()
    }

    fun snacked(view: View, message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
        message?.let { Snackbar.make(view, it, duration).show() }
    }

}