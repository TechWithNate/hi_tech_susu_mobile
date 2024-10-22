package com.nate.hitechsusu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private MaterialButton loginBtn;
    private MaterialButton createAccountBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        createAccountBtn.setOnClickListener(view -> openCreateAccount());
        loginBtn.setOnClickListener(view -> checkFields());
    }


    private void initViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        createAccountBtn = findViewById(R.id.create_btn);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void openCreateAccount(){
        startActivity(new Intent(Login.this, CreateAccount.class));
        finish();
    }

    private void checkFields(){
        if (TextUtils.isEmpty(email.getText())){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
        }else if (TextUtils.isEmpty(password.getText())){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        }else {
            loginUser(email.getText().toString(), password.getText().toString());
        }
    }

    private void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        openHomePage();
                    }else {
                        Toast.makeText(Login.this, "Error Login in: "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(Login.this, "Error: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void openHomePage(){
        startActivity(new Intent(Login.this, Home.class));
        finish();
    }
}