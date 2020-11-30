package com.crocodic.datastore.data.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    @Expose
    @SerializedName("id_user")
    val idUser: Int = 1,

    @Expose
    @SerializedName("id")
    val id: Int?,

    @Expose
    @SerializedName("name")
    val name: String?

)