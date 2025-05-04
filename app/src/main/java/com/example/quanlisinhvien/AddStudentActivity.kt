package com.example.quanlisinhvien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddStudentActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editMSSV: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        editName = findViewById(R.id.editName)
        editMSSV = findViewById(R.id.editMSSV)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)

        intent.getParcelableExtra<Student>("student")?.let { student ->
            editName.setText(student.name)
            editMSSV.setText(student.mssv)
            editEmail.setText(student.email)
            editPhone.setText(student.phone)
            position = intent.getIntExtra("position", -1)
        }

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val student = Student(
                editName.text.toString(),
                editMSSV.text.toString(),
                editEmail.text.toString(),
                editPhone.text.toString()
            )
            Intent().apply {
                putExtra("student", student)
                if (position != -1) putExtra("position", position)
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }
}