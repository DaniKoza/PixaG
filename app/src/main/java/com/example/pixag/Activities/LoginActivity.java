package com.example.pixag.Activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.pixag.Fragments.GetMobileNumberFragment;
import com.example.pixag.Fragments.VerifyPhoneFragment;
import com.example.pixag.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends AppCompatActivity
        implements GetMobileNumberFragment.onGetMobileNumberFragmentInteractionListener,
        VerifyPhoneFragment.OnVerifyPhoneFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container_login_phase);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            continueToMainActivity();
        }

        if (fragment == null) // if its the first time to call the GetMobileNumberFragment
        {
            fragment = new GetMobileNumberFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container_login_phase, fragment, "0").commit();
        }
    }

    public void LoadVerifyPhoneFragment(String phoneNum) {
        VerifyPhoneFragment mVerifyPhoneFragment = new VerifyPhoneFragment();
        getIntent().putExtra("phoneNumber", phoneNum);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.container_login_phase, mVerifyPhoneFragment, (getSupportFragmentManager().getBackStackEntryCount() - 1) + "").addToBackStack(null).commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit the best app ever!?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LoginActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void continueToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onGetMobileNumberFragmentInteraction(Uri uri) {
    }

    @Override
    public void onVerifyPhoneFragmentInteraction(Uri uri) {
    }
}
