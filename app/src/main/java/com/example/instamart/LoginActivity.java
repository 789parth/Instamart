package com.example.instamart;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TextInputLayout tilEmail = findViewById(R.id.tilEmail);
        TextInputLayout tilPassword = findViewById(R.id.tilPassword);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnGoogle = findViewById(R.id.btnGoogle);
        MaterialButton btnFacebook = findViewById(R.id.btnFacebook);
        TextView tvSignupAction = findViewById(R.id.tvSignupAction);

        btnLogin.setOnClickListener(v -> {
            String email = Objects.requireNonNull(tilEmail.getEditText()).getText().toString();
            String password = Objects.requireNonNull(tilPassword.getEditText()).getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
            }
        });

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        );

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Login clicked", Toast.LENGTH_SHORT).show()
        );

        btnFacebook.setOnClickListener(v ->
                Toast.makeText(this, "Facebook Login clicked", Toast.LENGTH_SHORT).show()
        );

        tvSignupAction.setOnClickListener(v ->
                Toast.makeText(this, "Navigate to Signup", Toast.LENGTH_SHORT).show()
        );
    }
}