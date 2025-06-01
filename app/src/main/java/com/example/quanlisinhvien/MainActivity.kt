package com.example.quanlisinhvien

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private lateinit var dbHelper: StudentDatabaseHelper
    private val studentList = mutableListOf<Student>()

    companion object {
        const val ADD_REQUEST_CODE = 100
        const val UPDATE_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Toolbar làm ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Khởi tạo database và tải danh sách sinh viên
        dbHelper = StudentDatabaseHelper(this)
        studentList.addAll(dbHelper.getAllStudents())

        recyclerView = findViewById(R.id.recyclerView)
        adapter = StudentAdapter(studentList) { position, action ->
            when (action) {
                "delete" -> showDeleteDialog(position)
                "update" -> openUpdateActivity(position)
                "call" -> makeCall(studentList[position].phone)
                "email" -> sendEmail(studentList[position].email)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Hiển thị menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Xử lý click menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                // Mở màn hình thêm mới
                startActivityForResult(
                    Intent(this, AddStudentActivity::class.java),
                    ADD_REQUEST_CODE
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ADD_REQUEST_CODE -> {
                    val student = data?.getParcelableExtra<Student>("student")
                    student?.let {
                        if(dbHelper.insertStudent(it)) {
                            studentList.add(0, it)
                            adapter.notifyItemInserted(0)
                            recyclerView.scrollToPosition(0)
                        }
                    }
                }
                UPDATE_REQUEST_CODE -> {
                    val position = data?.getIntExtra("position", -1)
                    val student = data?.getParcelableExtra<Student>("student")
                    if (position != null && position != -1 && student != null) {
                        if(dbHelper.updateStudent(student)) {
                            studentList[position] = student
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn chắc chắn muốn xóa?")
            .setPositiveButton("Xóa") { _, _ ->
                val mssv = studentList[position].mssv
                if(dbHelper.deleteStudent(mssv)) {
                    studentList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun openUpdateActivity(position: Int) {
        Intent(this, AddStudentActivity::class.java).apply {
            putExtra("student", studentList[position])
            putExtra("position", position)
            startActivityForResult(this, UPDATE_REQUEST_CODE)
        }
    }

    private fun makeCall(phone: String) {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
            startActivity(this)
        }
    }

    private fun sendEmail(email: String) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            startActivity(this)
        }
    }
}