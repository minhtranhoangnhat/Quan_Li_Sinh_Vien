package com.example.quanlisinhvien

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(val name: String, val mssv: String, val email: String, val phone: String) : Parcelable