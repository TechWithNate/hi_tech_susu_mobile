package com.nate.hitechsusu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private MaterialButton loginBtn;
    private MaterialButton createAccountBtn;

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

    }
}