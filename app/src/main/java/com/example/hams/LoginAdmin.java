package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginAdmin extends AppCompatActivity {

    private Button button;
    Admin admin = MainActivity.admin;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_admin);

        button = (Button) findViewById(R.id.button);
        TextInputEditText emailBox = findViewById(R.id.username);
        TextInputEditText passwordBox = findViewById(R.id.password);

        //Toast invalidLoginToast = new Toast(this); //make toast pop up AFTER D1 IS OVER WITH
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(emailBox.getText());
                String password = String.valueOf(passwordBox.getText());
                if(admin.login(email, password)){
                    openLogin();
                }else{
                    emailBox.getText().clear();
                    passwordBox.getText().clear();
                }
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, AdminView.class);
        startActivity(intent);
    }

}