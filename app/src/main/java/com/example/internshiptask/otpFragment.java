package com.example.internshiptask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chaos.view.PinView;

public class otpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        PinView pinView = view.findViewById(R.id.pinView);
        Button btnGetOtp = view.findViewById(R.id.btnEnterOtp);

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(pinView.getText().toString()) || pinView.getText().length() < 6) {
                    Toast.makeText(getActivity(), "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    assert loginActivity != null;
                    loginActivity.verifyCode(pinView.getText().toString());
                }
            }
        });

        return view;
    }
}