package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginDoc extends AppCompatActivity {

    Doctor doc = SignUpDoc.doc;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_doc);

        button = (Button) findViewById(R.id.button);
        TextInputEditText emailBox = findViewById(R.id.email);
        TextInputEditText passwordBox = findViewById(R.id.pass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(emailBox.getText());
                String password = String.valueOf(passwordBox.getText());
                if(doc.login(email, password)){
                    //login successful, send to docView page
                    openLogin();
                } else{
                    emailBox.getText().clear();
                    passwordBox.getText().clear();
                }

            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this, DocView.class);
        startActivity(intent);
    }
}