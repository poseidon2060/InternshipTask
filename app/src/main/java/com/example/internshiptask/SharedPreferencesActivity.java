package com.example.internshiptask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.paperdb.Paper;

public class SharedPreferencesActivity extends AppCompatActivity {

    private String uriData = "";
    final private Integer REQUEST_CODE = 100;
    private ArrayList<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences);

        Paper.init(this);

        //Loading data on activity opening
        loadUser();

        //Date Picker
        TextView tvDatePicker = findViewById(R.id.tvDatePicker);
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        //Image Select
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryForImage();
            }
        });

        //Save button
        EditText etName = findViewById(R.id.etName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        EditText etEmail = findViewById(R.id.etEmail);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String email = etEmail.getText().toString();
                String date = ((TextView) findViewById(R.id.tvDatePicker)).getText().toString();
                String uri = uriData;
                User user = new User(name, phone, email, date, uri);

                saveUser(user);
                loadUser();

                etName.setText("");
                etPhoneNumber.setText("");
                etEmail.setText("");
                tvDatePicker.setText("");
            }
        });

    }

    private void saveUser(User user) {
        usersList = Paper.book().read("users", new ArrayList<User>());
        usersList.add(user);
        for (int i = 0; i < usersList.size(); i++) {
            Log.d("HELLO", usersList.get(i).getPhoneNumber());
        }
        Paper.book().write("users", usersList);
    }

    private void loadUser() {
        usersList = Paper.book().read("users", new ArrayList<User>());
        if (usersList.isEmpty()) {

        } else {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new UserAdapter(usersList, this::deleteUser, this::editUser));
        }
    }

    private void deleteUser(int index) {
        usersList.remove(index);
        Paper.book().delete("users");
        Paper.book().write("users", usersList);
        loadUser();
    }

    private void editUser(Integer index) {
        User user = usersList.get(index);
        deleteUser(index);
        EditText etName = findViewById(R.id.etName);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        EditText etEmail = findViewById(R.id.etEmail);
        TextView tvDatePicker = findViewById(R.id.tvDatePicker);
        etName.setText(user.getName());
        etPhoneNumber.setText(user.getPhoneNumber());
        etEmail.setText(user.getEmailAdd());
        tvDatePicker.setText(user.getBirthDate());
        uriData = user.getImageUri().toString();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day + "/" + (month + 1) + "/" + year;
                        ((TextView) findViewById(R.id.tvDatePicker)).setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            uriData = data.getData().toString();
        }
    }
}