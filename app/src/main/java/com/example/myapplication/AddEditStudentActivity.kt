package com.example.myapplication

// AddEditStudentActivity.kt
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddEditStudentActivity : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtId: EditText
    private lateinit var btnSave: Button
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        edtName = findViewById(R.id.edtName)
        edtId = findViewById(R.id.edtId)
        btnSave = findViewById(R.id.btnSave)

        // Check if editing existing student
        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            edtId.setText(intent.getStringExtra("student_id"))
            edtName.setText(intent.getStringExtra("student_name"))
        }

        btnSave.setOnClickListener {
            val id = edtId.text.toString()
            val name = edtName.text.toString()

            if (id.isNotEmpty() && name.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("student_id", id)
                intent.putExtra("student_name", name)
                intent.putExtra("position", position)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}