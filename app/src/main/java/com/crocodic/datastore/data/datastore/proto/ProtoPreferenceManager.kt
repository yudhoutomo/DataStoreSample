package com.crocodic.datastore.data.datastore.proto

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.crocodic.datastore.UserProto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

class ProtoPreferenceManager(context: Context) {
    private val dataStore: DataStore<UserProto> =
        context.createDataStore(
            fileName = "userProto.pb",
            serializer = UserProtoSerializer
        )

    fun getValue() : Flow<UserProto> {
        return dataStore.data
    }

    fun setValue(nama: String,age : Int) {
        GlobalScope.launch {
            dataStore.updateData { preferences ->
                preferences.toBuilder()
                        .setName(Base64.encodeToString(nama.toByteArray(), Base64.NO_WRAP))
                        .setAge(age)
                        .setIsBoolean(true)
                        .build()
            }
        }
    }
}

object UserProtoSerializer : Serializer<UserProto> {
    override fun readFrom(input: InputStream): UserProto {
        try {
            return UserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: UserProto, output: OutputStream) = t.writeTo(output)
}