package com.example.quanlisinhvien

import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAll(): List<Student>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(student: Student): Long

    @Update
    fun update(student: Student): Int

    @Delete
    fun delete(student: Student): Int
}