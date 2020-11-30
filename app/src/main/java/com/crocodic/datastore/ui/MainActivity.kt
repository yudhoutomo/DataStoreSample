package com.crocodic.datastore.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.crocodic.datastore.R
import com.crocodic.datastore.base.BaseActivity
import com.crocodic.datastore.util.Cons
import com.crocodic.datastore.util.getString
import com.crocodic.datastore.util.text
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity<MainViewModel>(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListener()

        saveStorePreference()
        saveProtoPreference()
        saveSessionPreference()

        observeData()
    }

    private fun observeData(){
        dataStorePreference.getValue(Cons.SHAREDPREF.KEY_NAME).asLiveData().observe(this){
            Log.e("storeData", it.getString())
        }

        protoPreferenceManager.getValue().asLiveData().observe(this){
            Log.e("protoData", it.getString())
            tvAge text "${it.age}"
        }
    }

    private fun saveStorePreference(){
        dataStorePreference.apply {
            setValue(Cons.SHAREDPREF.KEY_NAME, "Yudho Utomo")
            setAge(22)
            setBoolean(true)
        }
    }

    private fun saveProtoPreference(){
        protoPreferenceManager.setValue("Yudho Utomo", 22)
    }

    private fun saveSessionPreference(){
        session.apply {
            name = "Yudho Utomo"
            age = 22
            isBoolean = true
        }
    }

    private fun initListener(){
        btnUpdateAge.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btnUpdateAge -> {
                val random = Random().nextInt(61) + 20 // [0, 60] + 20 => [20, 80]
                protoPreferenceManager.setValue("Yudho Utomo",  random)
            }
        }
    }
}