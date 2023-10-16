package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginPat extends AppCompatActivity {

    private Button button;
    Patient pat = SignUpPat.pat;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_pat);

        button = (Button) findViewById(R.id.button);
        TextInputEditText userBox = findViewById(R.id.user);
        TextInputEditText passwordBox = findViewById(R.id.pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = String.valueOf(userBox.getText());
                String password = String.valueOf(passwordBox.getText());
                if(pat.login(user, password)){
                    openLogin();
                }else{
                    userBox.getText().clear();
                    passwordBox.getText().clear();
                }
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, PatView.class);
        startActivity(intent);
    }
}