package com.example.instamart;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private TextInputLayout tilPhone, tilOtp;
    private MaterialButton btnSendOtp, btnVerifyOtp;
    private TextView tvResendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        tilPhone = findViewById(R.id.tilPhone);
        tilOtp = findViewById(R.id.tilOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        tvResendOtp = findViewById(R.id.tvResendOtp);

        btnSendOtp.setOnClickListener(v -> sendOtp());
        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
        tvResendOtp.setOnClickListener(v -> resendOtp());

        // Initialize Phone Auth Callbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(LoginActivity.this, getString(R.string.error_verification_failed, e.getMessage()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(LoginActivity.this, R.string.msg_otp_sent, Toast.LENGTH_SHORT).show();

                // Update UI
                if (tilPhone.getEditText() != null) {
                    tilPhone.setEnabled(false);
                }
                btnSendOtp.setVisibility(View.GONE);
                tilOtp.setVisibility(View.VISIBLE);
                btnVerifyOtp.setVisibility(View.VISIBLE);
                tvResendOtp.setVisibility(View.VISIBLE);
            }
        };
    }

    private void sendOtp() {
        String phoneNumber = "";
        if (tilPhone.getEditText() != null) {
            phoneNumber = tilPhone.getEditText().getText().toString().trim();
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, R.string.error_enter_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        startPhoneNumberVerification(phoneNumber);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp() {
        String code = "";
        if (tilOtp.getEditText() != null) {
            code = tilOtp.getEditText().getText().toString().trim();
        }

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, R.string.error_enter_otp, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mVerificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void resendOtp() {
        String phoneNumber = "";
        if (tilPhone.getEditText() != null) {
            phoneNumber = tilPhone.getEditText().getText().toString().trim();
        }

        if (TextUtils.isEmpty(phoneNumber) || mResendToken == null) {
            Toast.makeText(this, R.string.error_resend_otp, Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, R.string.msg_login_success, Toast.LENGTH_SHORT).show();
                        // Proceed to next activity
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.msg_login_failed, Toast.LENGTH_SHORT).show();
                        if (task.getException() != null) {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}