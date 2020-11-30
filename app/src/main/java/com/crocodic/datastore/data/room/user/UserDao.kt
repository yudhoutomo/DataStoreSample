package com.crocodic.datastore.data.room.user

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: User)

    @Query("SELECT * from user WHERE idUser = 1 LIMIT 1")
    fun getUser(): LiveData<User?>

    @Query("SELECT COUNT(*) from user WHERE idUser = 1")
    fun getUserLogin(): Single<Int>

    @Update
    suspend fun update(value: User)

    @Delete
    suspend fun delete(value: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}