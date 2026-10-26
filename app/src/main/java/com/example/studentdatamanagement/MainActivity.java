 package com.example.studentdatamanagement;

import android.os.Bundle;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private StudentDbHelper dbHelper;

    private EditText etStudentName;
    private EditText etStudentId;
    private Spinner spMajor;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new StudentDbHelper(this);

        etStudentName = findViewById(R.id.etStudentName);
        etStudentId = findViewById(R.id.etStudentId);
        spMajor = findViewById(R.id.spMajor);
        tvResult = findViewById(R.id.tvResult);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnView = findViewById(R.id.btnView);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnAdd.setOnClickListener(v -> addStudent());
        btnView.setOnClickListener(v -> viewStudent());
        btnUpdate.setOnClickListener(v -> updateStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
    }

    private boolean validateInput() {
        if (etStudentName.getText().toString().trim().isEmpty()) {
            etStudentName.setError("Enter Student Name");
            return false;
        }

        if (etStudentId.getText().toString().trim().isEmpty()) {
            etStudentId.setError("Enter Student ID");
            return false;
        }

        return true;
    }

    private void addStudent() {
        if (!validateInput()) return;

        String name = etStudentName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        String major = spMajor.getSelectedItem().toString();

        boolean success = dbHelper.addStudent(id, name, major);

        if (success) {
            tvResult.setText("Student added successfully!");
            Toast.makeText(this, "Student added!", Toast.LENGTH_SHORT).show();
        } else {
            tvResult.setText("Student ID already exists.");
            Toast.makeText(this, "Student ID already exists!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void viewStudent() {
        String id = etStudentId.getText().toString().trim();

        if (id.isEmpty()) {
            etStudentId.setError("Enter Student ID");
            return;
        }

        try (Cursor cursor = dbHelper.getStudent(id)) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                StudentDbHelper.COL_NAME));

                String major = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                StudentDbHelper.COL_MAJOR));

                etStudentName.setText(name);

                ArrayAdapter adapter =
                        (ArrayAdapter) spMajor.getAdapter();

                int position = adapter.getPosition(major);
                if (position >= 0) {
                    spMajor.setSelection(position);
                }

                tvResult.setText(
                        "STUDENT INFORMATION\n\n" +
                                "Student Name: " + name + "\n" +
                                "Student ID: " + id + "\n" +
                                "Major: " + major
                );
            } else {
                tvResult.setText("Student not found.");
            }
        }
    }

    private void updateStudent() {
        if (!validateInput()) return;

        String name = etStudentName.getText().toString().trim();
        String id = etStudentId.getText().toString().trim();
        String major = spMajor.getSelectedItem().toString();

        int rows = dbHelper.updateStudent(id, name, major);

        if (rows > 0) {
            tvResult.setText("Student updated successfully!");
        } else {
            tvResult.setText("Student not found. Update failed.");
        }
    }

    private void deleteStudent() {
        String id = etStudentId.getText().toString().trim();

        if (id.isEmpty()) {
            etStudentId.setError("Enter Student ID");
            return;
        }

        int rows = dbHelper.deleteStudent(id);

        if (rows > 0) {
            etStudentName.setText("");
            tvResult.setText("Student deleted successfully!");
        } else {
            tvResult.setText("Student not found. Delete failed.");
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }

        super.onDestroy();
    }
}