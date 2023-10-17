package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginPat extends AppCompatActivity {

    private Button button;
    //Patient pat = SignUpPat.patient;
    private FirebaseAuth mAuth;
    FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference rootRef, newRef;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_pat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();

        button = (Button) findViewById(R.id.button);
        TextInputEditText userBox = findViewById(R.id.user);
        TextInputEditText passwordBox = findViewById(R.id.pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = String.valueOf(userBox.getText());
                String password = String.valueOf(passwordBox.getText());

                mAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginPat.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            openLogin();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Info", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPat.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, PatView.class);
        startActivity(intent);
    }
}