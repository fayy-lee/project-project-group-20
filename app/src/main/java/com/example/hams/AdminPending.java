package com.example.hams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminPending extends AppCompatActivity {
    private TextView textView;
    private ImageButton reject, accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pending);

        //initiate views

        textView = findViewById(R.id.name);
        reject = (ImageButton) findViewById(R.id.reject);
        accept = (ImageButton) findViewById(R.id.accept);

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display text
                Toast.makeText(AdminPending.this, "User Rejected", Toast.LENGTH_SHORT).show();// display the toast on button click
                // get text from TextView
                String text = textView.getText().toString();
                // start rejected activity and pass the text
                Intent intent = new Intent(AdminPending.this, AdminRejected.class);
                intent.putExtra("text", text);
                startActivity(intent);
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display text
                Toast.makeText(AdminPending.this, "User Accepted", Toast.LENGTH_SHORT).show();// display the toast on button click
                // get text from TextView
                String text = textView.getText().toString();
                // start rejected activity and pass the text
                Intent intent = new Intent(AdminPending.this, AdminApproved.class);
                intent.putExtra("text", text);
                startActivity(intent);
                finish();}
        });

    }
}