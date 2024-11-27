package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private var studentList = ArrayList<Student>()
    private lateinit var adapter: ArrayAdapter<String>

    companion object {
        const val ADD_STUDENT_REQUEST = 1
        const val EDIT_STUDENT_REQUEST = 2
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        studentList.add(Student("SV001", "Nguyễn Văn A"))
        studentList.add(Student("SV002", "Trần Thị B"))
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList.map { "${it.name} - ${it.id}" })
        listView.adapter = adapter
        registerForContextMenu(listView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuAdd -> {
                val intent = Intent(this, AddEditStudentActivity::class.java)
                startActivityForResult(intent, ADD_STUDENT_REQUEST)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        when (item.itemId) {
            R.id.menuEdit -> {
                val student = studentList[position]
                val intent = Intent(this, AddEditStudentActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("student_id", student.id)
                intent.putExtra("student_name", student.name)
                startActivityForResult(intent, EDIT_STUDENT_REQUEST)
                return true
            }
            R.id.menuRemove -> {
                studentList.removeAt(position)
                updateListView()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val id = data?.getStringExtra("student_id") ?: return
            val name = data.getStringExtra("student_name") ?: return

            when (requestCode) {
                ADD_STUDENT_REQUEST -> {
                    studentList.add(Student(id, name))
                    updateListView()
                }
                EDIT_STUDENT_REQUEST -> {
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) {
                        studentList[position] = Student(id, name)
                        updateListView()
                    }
                }
            }
        }
    }

    private fun updateListView() {
        adapter.clear()
        adapter.addAll(studentList.map { "${it.name} - ${it.id}" })
        adapter.notifyDataSetChanged()
    }
}