package com.example.quanlisinhvien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val onActionClick: (Int, String) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textMSSV: TextView = itemView.findViewById(R.id.textMSSV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.textName.text = student.name
        holder.textMSSV.text = "MSSV: ${student.mssv}"

        holder.itemView.setOnLongClickListener { view ->
            showPopupMenu(view, position)
            true
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        PopupMenu(view.context, view).apply {
            menuInflater.inflate(R.menu.context_menu, menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_update -> onActionClick(position, "update")
                    R.id.menu_delete -> onActionClick(position, "delete")
                    R.id.menu_call -> onActionClick(position, "call")
                    R.id.menu_email -> onActionClick(position, "email")
                }
                true
            }
            show()
        }
    }

    override fun getItemCount(): Int = studentList.size

}