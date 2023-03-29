package com.example.internshiptask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class phoneFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        EditText etPhone = view.findViewById(R.id.etPhone);
        Button btnFetchOtp = view.findViewById(R.id.btnFetchOtp);
        btnFetchOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etPhone.getText().toString()) || etPhone.getText().length() < 10) {
                    Toast.makeText(getActivity(),"Please enter valid value",Toast.LENGTH_SHORT).show();
                } else {
                    String phone = "+91" + etPhone.getText().toString();
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    assert loginActivity != null;
                    loginActivity.sendVerificationCode(phone);
                    NavHostFragment.findNavController(phoneFragment.this).navigate(R.id.action_phoneFragment_to_otpFragment);
                }
            }
        });
        return view;
    }
}