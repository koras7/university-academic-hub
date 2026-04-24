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
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
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

        // v3 -> v4: add `password` column to users and recreate the table so the schema
        // (and indexes) match what Room expects for the new UserEntity.
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS `users`")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `users` (" +
                        "`id` TEXT NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`email` TEXT NOT NULL, " +
                        "`password` TEXT NOT NULL, " +
                        "`role` TEXT NOT NULL, " +
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
