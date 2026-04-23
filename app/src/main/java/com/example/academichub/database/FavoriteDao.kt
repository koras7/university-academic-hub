package com.example.academichub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE tutorId = :tutorId AND studentId = :studentId")
    fun deleteFavorite(tutorId: String, studentId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE tutorId = :tutorId AND studentId = :studentId)")
    fun isFavorite(tutorId: String, studentId: String): Boolean

    @Query("SELECT * FROM favorites WHERE studentId = :studentId")
    fun getFavoritesForStudent(studentId: String): List<FavoriteEntity>
}
