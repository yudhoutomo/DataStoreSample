package com.crocodic.datastore.data.datastore.preference

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.crocodic.datastore.util.Cons
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DataStoreManager(context: Context) {

    //Create the dataStore
    private val dataStore = context.createDataStore(name = "prefs_datastore")

    /**
     * setter
     */
    fun setValue(key : String, value: String) {
        GlobalScope.launch {
            dataStore.edit {
                it[preferencesKey<String>(key)] = Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP)
            }
        }
    }

    fun setAge(value: Int) {
        GlobalScope.launch {
            dataStore.edit {
                it[KEY_AGE] = value
            }
        }
    }

    fun setBoolean(value: Boolean) {
        GlobalScope.launch {
            dataStore.edit {
                it[KEY_BOOELAN] = value
            }
        }
    }

    /**
     * getter
     */
    fun getValue(key : String) : Flow<String> {
        return dataStore.data.map {
            it[preferencesKey<String>(key)] ?: ""
        }
    }

    fun getAge(key : Int) : Flow<Int> {
        return dataStore.data.map {
            it[KEY_AGE] ?: 0
        }
    }

    fun getBoolean(key : String) : Flow<Boolean> {
        return dataStore.data.map {
            it[KEY_BOOELAN] ?: false
        }
    }

    companion object {
        val KEY_AGE = preferencesKey<Int>(Cons.SHAREDPREF.KEY_AGE)
        val KEY_BOOELAN = preferencesKey<Boolean>(Cons.SHAREDPREF.KEY_ISBOOLEAN)
    }


}