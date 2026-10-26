package com.example.studentdatamanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDbHelper(context: Context) :
    SQLiteOpenHelper(context, "StudentDatabase.db", null, 1) {

    companion object {
        const val TABLE_NAME = "students"
        const val COL_ID = "student_id"
        const val COL_NAME = "student_name"
        const val COL_MAJOR = "major"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                $COL_ID TEXT PRIMARY KEY,
                $COL_NAME TEXT NOT NULL,
                $COL_MAJOR TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addStudent(
        id: String,
        name: String,
        major: String
    ): Boolean {
        val values = ContentValues().apply {
            put(COL_ID, id)
            put(COL_NAME, name)
            put(COL_MAJOR, major)
        }

        return writableDatabase.insert(
            TABLE_NAME, null, values
        ) != -1L
    }

    fun getStudent(id: String) =
        readableDatabase.query(
            TABLE_NAME,
            arrayOf(COL_ID, COL_NAME, COL_MAJOR),
            "$COL_ID = ?",
            arrayOf(id),
            null, null, null
        )

    fun updateStudent(
        id: String,
        name: String,
        major: String
    ): Int {
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_MAJOR, major)
        }

        return writableDatabase.update(
            TABLE_NAME, values, "$COL_ID = ?", arrayOf(id)
        )
    }

    fun deleteStudent(id: String): Int {
        return writableDatabase.delete(
            TABLE_NAME, "$COL_ID = ?", arrayOf(id)
        )
    }
}
