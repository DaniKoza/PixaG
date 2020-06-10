package com.example.pixag.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pixag.Activities.LoginActivity;
import com.example.pixag.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyPhoneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar verify_PSB_bar;
    private EditText verify_EDT_pass;
    private Button verify_BTN_login;
    private TextView verify_LBL_login;

    public VerifyPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyPhoneFragment newInstance(String param1, String param2) {
        VerifyPhoneFragment fragment = new VerifyPhoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        verify_BTN_login = view.findViewById(R.id.verify_BTN_login);
        verify_PSB_bar = view.findViewById(R.id.verify_PSB_bar);
        verify_EDT_pass = view.findViewById(R.id.verify_EDT_pass);
        verify_LBL_login = view.findViewById(R.id.verify_LBL_login);

        mAuth = FirebaseAuth.getInstance();

        Intent i = getActivity().getIntent();
        String phoneNumber = (String) i.getSerializableExtra("phoneNumber");
        sendVerificationCode(phoneNumber);

        verify_BTN_login.setOnClickListener(v -> {
            String code = verify_EDT_pass.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                verify_EDT_pass.setError("Enter the correct code please");
                verify_EDT_pass.requestFocus();
                return;
            }
            verifyCode(code);
        });

        verify_EDT_pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        verify_BTN_login.performClick();
                        return true;
                    }
                }
                return false;
            }
        });
        verify_LBL_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_BTN_login.performClick();
            }
        });

        return view;
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            LoginActivity la = (LoginActivity) getActivity();
                            la.continueToMainActivity();
                        } else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String phoneNumber) {
        verify_PSB_bar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+972" + phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verify_EDT_pass.setText(code);
                verifyCode(code);
            } else signInWithCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public interface OnVerifyPhoneFragmentInteractionListener {
        // TODO: Update argument type and name
        void onVerifyPhoneFragmentInteraction(Uri uri);
    }
}