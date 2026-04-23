package com.example.academichub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UserEntity::class,
        TutorEntity::class,
        SessionRequestEntity::class,
        RatingEntity::class,
        FavoriteEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tutorDao(): TutorDao
    abstract fun sessionRequestDao(): SessionRequestDao
    abstract fun ratingDao(): RatingDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `ratings` (" +
                        "`id` TEXT NOT NULL, " +
                        "`tutorId` TEXT NOT NULL, " +
                        "`studentId` TEXT NOT NULL, " +
                        "`rating` REAL NOT NULL, " +
                        "`comment` TEXT NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `favorites` (" +
                        "`id` TEXT NOT NULL, " +
                        "`tutorId` TEXT NOT NULL, " +
                        "`studentId` TEXT NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "academic_hub_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
