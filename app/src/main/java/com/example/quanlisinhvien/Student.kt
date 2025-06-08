package com.example.quanlisinhvien

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "students")
data class Student(
    val name: String,
    @PrimaryKey val mssv: String,
    val email: String,
    val phone: String
) : Parcelable