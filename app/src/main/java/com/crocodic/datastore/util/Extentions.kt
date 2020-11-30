package com.crocodic.datastore.util

import android.animation.LayoutTransition
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection


infix fun ViewGroup.inflate(layoutResId: Int): View =
    LayoutInflater.from(context).inflate(layoutResId, this, false)


fun View.onClick(function: () -> Unit) {
    setOnClickListener {
        function()
    }
}


fun Activity.statusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w = this.window // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}

fun View.invisibleView() {
    this.visibility = View.INVISIBLE
}

/**
 * convert json
 */

inline fun <reified ITEM> String.toList(): List<ITEM> {
    val vList = ArrayList<ITEM>()
    val arry = JsonParser().parse(this).asJsonArray
    for (jsonElement in arry) {
        vList.add(Gson().fromJson(jsonElement, ITEM::class.java))
    }
    return vList
}

inline fun <reified ITEM> JSONArray.toList(): List<ITEM> {
    val vList = ArrayList<ITEM>()
    val arry = JsonParser().parse(this.toString()).asJsonArray
    for (jsonElement in arry) {
        vList.add(Gson().fromJson(jsonElement, ITEM::class.java))
    }
    return vList
}

inline fun <reified ITEM> String.getObject(): ITEM {
    return Gson().fromJson(this, ITEM::class.java)
}

inline fun <reified ITEM> JSONObject.getObject(): ITEM {
    return Gson().fromJson(this.toString(), ITEM::class.java)
}

inline fun <reified ITEM> ITEM.getString(): String {
    return Gson().toJson(this, ITEM::class.java)
}

fun JSONObject.string(value: String): String {
    try {
        return this.getString(value)
    } catch (e: Exception) {
        return ""
    }
}

fun JSONObject.int(value: String): Int {
    try {
        return this.getInt(value)
    } catch (e: Exception) {
        return 0
    }
}

fun JSONObject.array(value: String): JSONArray {
    try {
        return this.getJSONArray(value)!!
    } catch (e: Exception) {
        return JSONArray()
    }
}

fun JSONObject.obj(value: String): JSONObject {
    try {
        return this.getJSONObject(value)!!
    } catch (e: Exception) {
        return JSONObject()
    }
}

/**
 * extra intent
 */

infix fun Activity.extra(value: String): String {
    return intent.getStringExtra(value)!!
}

fun Activity.extra(value: String, default: Int): Int {
    return intent.getIntExtra(value, default)
}

fun Activity.extra(value: String, default: Boolean): Boolean {
    return intent.getBooleanExtra(value, default)
}


fun NestedScrollView.isScrollBottom(scrollBottom: () -> Unit){

    // TODO: 25/07/18 detect nested scroll to bottom
    this.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
        val totalHeight: Int = this.getChildAt(0).getHeight()
        val scrollY: Int = this.getScrollY()
        val isTopReached: Boolean = this.canScrollVertically(1)
        if (!isTopReached) {
            scrollBottom()
        }
//        Log.e("position", "totalHeight=" + totalHeight + "scrollY=" + scrollY)
//        Log.e("isBottomReached", "" + isBottomReached)
    })
}

fun RecyclerView.initItem(activity: Context, adapter: RecyclerView.Adapter<*>) {
    val mLayoutManager = LinearLayoutManager(activity)
    this.layoutManager = mLayoutManager
    this.setHasFixedSize(false)
    this.setItemViewCacheSize(20)
    this.itemAnimator = DefaultItemAnimator()
    this.adapter = adapter

}

fun RecyclerView.initItemHorizontal(activity: Context, adapter: RecyclerView.Adapter<*>) {
    val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    this.layoutManager = mLayoutManager
    this.setHasFixedSize(false)
    this.setItemViewCacheSize(20)
    this.itemAnimator = DefaultItemAnimator()
    this.adapter = adapter

}

fun RecyclerView.initItemGrid(activity: Context, adapter: RecyclerView.Adapter<*>, gridCount: Int) {
    val mLayoutManager = GridLayoutManager(activity, gridCount)
    this.layoutManager = mLayoutManager
    this.setHasFixedSize(false)
    this.setItemViewCacheSize(20)
    this.itemAnimator = DefaultItemAnimator()
    this.adapter = adapter

}

fun RecyclerView.onEndScroll(isGrid: Boolean, listener: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            var mLayoutManager: LinearLayoutManager

            if (isGrid)
                mLayoutManager = recyclerView.layoutManager as GridLayoutManager
            else
                mLayoutManager = recyclerView.layoutManager as LinearLayoutManager


            val visibleItemCount = recyclerView.childCount
            val totalItemCount = mLayoutManager.itemCount
            val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()


            if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                listener()
            }
        }
    })
}


/**
 * intent activity
 */

inline fun <reified T : Any> Activity.goTo(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
    fade()
}


inline fun <reified T : Any> Activity.goTo(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
    fade()
}


inline fun <reified T : Any> androidx.fragment.app.Fragment.goTo(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(activity!!)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
    activity!!.fade()
}

inline fun <reified T : Any> androidx.fragment.app.Fragment.goTo(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(activity!!)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
    activity!!.fade()
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


fun Activity.flagFullScreen() {
    if (Build.VERSION.SDK_INT >= 21) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}



fun Context.showToast(message: String) {
    if (message != "") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


fun Activity.fade() {
    try {
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun ViewGroup.lyTrans() {
    val transition = LayoutTransition()
    transition.enableTransitionType(LayoutTransition.CHANGING)
    transition.setStartDelay(LayoutTransition.APPEARING, 2000)
    transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, 2000)
    this.layoutTransition = transition
}


infix fun TextView.text(value: String?) {
    text = StringMasker().validateEmpty(value)
}


fun Throwable.isErrorServer(): Boolean {
    var message = ""
    val isErrorServer: Boolean
    when ((this as HttpException).code()) {
        HttpsURLConnection.HTTP_UNAUTHORIZED -> {
            isErrorServer = true
            message = "Unauthorised User "
        }
        HttpsURLConnection.HTTP_FORBIDDEN -> {
            isErrorServer = true
            message = "Forbidden"
        }
        HttpsURLConnection.HTTP_INTERNAL_ERROR -> {
            isErrorServer = true
            message = "Internal Server Error"
        }
        HttpsURLConnection.HTTP_NOT_FOUND -> {
            isErrorServer = true
            message = "Internal Server Error"
        }
        HttpsURLConnection.HTTP_BAD_REQUEST -> {
            isErrorServer = true
            message = "Bad Request"
        }
        HttpsURLConnection.HTTP_BAD_GATEWAY -> {
            isErrorServer = true
            message = "Bad Request"
        }
        HttpsURLConnection.HTTP_SERVER_ERROR -> {
            isErrorServer = true
            message = "Server Error"
        }
        0 -> {
            isErrorServer = false
            message = "No Internet Connection"
        }
        else -> {
            isErrorServer = true
            message = this.localizedMessage!!
        }
    }
    return isErrorServer
}

fun isErrorServer(throwable: Throwable): String {
    var message = ""
    message = if (throwable is HttpException) {
        when (throwable.code()) {
            0 -> {
                "Sorry, there is network problem. Please try again."
            }
            else -> {
                "Sorry, there is problem with server. Please try again."
            }
        }
    } else {
        "Sorry, there is network problem. Please try again."
    }
    return message
}


fun Activity.statusBarLight(){
    if (Build.VERSION.SDK_INT >= 23) {
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.WHITE
    }

}


fun View.showAnim(parentView: View, duration: Long) {
    try {
        Handler().postDelayed({

            TransitionManager.beginDelayedTransition(parentView as ViewGroup)
            this.show()

        }, duration)
    } catch (e: Exception) {
        e.printStackTrace()
        this.show()
    }

}


inline fun delay(milliseconds: Long, crossinline action: () -> Unit) {
    Handler().postDelayed({
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, milliseconds)
}


//
//fun Activity.statusBarLight(){
//    if (Build.VERSION.SDK_INT >= 23) {
//        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    }
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        val window = this.window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.WHITE
//    }
//
//}


