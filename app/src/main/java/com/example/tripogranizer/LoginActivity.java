package com.example.tripogranizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailET;
    private EditText passwordET;
    private Button submitBTN;
    private Button createAccountBTN;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.login_edit_email);
        passwordET = findViewById(R.id.login_edit_password);
        submitBTN = findViewById(R.id.login_btn_submit);
        createAccountBTN = findViewById(R.id.login_btn_createAccount);

        mAuth = FirebaseAuth.getInstance();

        createAccountBTN.setOnClickListener(v-> {
            startActivity(new Intent(LoginActivity.this , RegisterActivity.class));
        });
        submitBTN.setOnClickListener(v-> {
            String txt_email = emailET.getText().toString();
            String txt_password = passwordET.getText().toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(LoginActivity.this, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(txt_email , txt_password);
            }
        });
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Dane prawidłowe", Toast.LENGTH_SHORT).show();
                   // Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                  //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  //  startActivity(intent);
                    startActivity(new Intent(LoginActivity.this, TripActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}