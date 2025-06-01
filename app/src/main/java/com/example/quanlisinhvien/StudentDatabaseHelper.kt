package com.example.quanlisinhvien

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "students.db", null, 1) {

    companion object {
        private const val TABLE_NAME = "students"
        private const val COL_NAME = "name"
        private const val COL_MSSV = "mssv"
        private const val COL_EMAIL = "email"
        private const val COL_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_MSSV TEXT PRIMARY KEY,
                $COL_NAME TEXT,
                $COL_EMAIL TEXT,
                $COL_PHONE TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_MSSV, student.mssv)
            put(COL_NAME, student.name)
            put(COL_EMAIL, student.email)
            put(COL_PHONE, student.phone)
        }
        return db.insert(TABLE_NAME, null, values) != -1L
    }

    fun updateStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, student.name)
            put(COL_EMAIL, student.email)
            put(COL_PHONE, student.phone)
        }
        return db.update(TABLE_NAME, values, "$COL_MSSV = ?", arrayOf(student.mssv)) > 0
    }

    fun deleteStudent(mssv: String): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COL_MSSV = ?", arrayOf(mssv)) > 0
    }

    fun getAllStudents(): MutableList<Student> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val list = mutableListOf<Student>()
        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    mssv = cursor.getString(cursor.getColumnIndexOrThrow(COL_MSSV)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE))
                )
                list.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}