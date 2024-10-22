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

import com.google.android.material.button.MaterialButton;

import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private MaterialButton createAccountBtn;
    private MaterialButton loginBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        loginBtn.setOnClickListener(view -> openLogin());
        createAccountBtn.setOnClickListener(view -> checkFields());
    }

    private void initViews(){
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        createAccountBtn = findViewById(R.id.create_btn);
        loginBtn = findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();  // Initialize Firestore
    }

    private void openLogin(){
        startActivity(new Intent(CreateAccount.this, Login.class));
        finish();
    }

    private void checkFields(){
        if (TextUtils.isEmpty(firstname.getText())){
            Toast.makeText(this, "Enter firstname", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(lastname.getText())){
            Toast.makeText(this, "Enter lastname", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email.getText())) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password.getText())) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
        } else if (!password.getText().toString().equals(confirm_password.getText().toString())){
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        } else {
            createAccount(email.getText().toString(), password.getText().toString());
        }
    }

    private void createAccount(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid());
                        }
                        Toast.makeText(CreateAccount.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        openLogin();
                    } else {
                        Toast.makeText(CreateAccount.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(CreateAccount.this, "Account Creation Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveUserToFirestore(String userId) {
        // Create a map to store user details
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstname.getText().toString());
        userDetails.put("lastName", lastname.getText().toString());
        userDetails.put("email", email.getText().toString());
        userDetails.put("role", "cashier");  // Assign role as "cashier"

        // Reference to Firestore users collection
        DocumentReference userRef = firestore.collection("cashier").document(userId);

        userRef.set(userDetails)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateAccount.this, "User added to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateAccount.this, "Error saving user: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
