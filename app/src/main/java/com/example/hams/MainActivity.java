package com.example.hams;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MainActivity extends AppCompatActivity {
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference dbReference = database.getReference();
    public static DatabaseReference usersRef = dbReference.child("Users");
    public static DatabaseReference appointmentsRef = dbReference.child("Appointments");

    private static int SPLASH_SCREEN = 5000;

    // animations
    // variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo;
    //admin object
    public static Admin admin = new Admin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);


        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        // hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        long SplashScreen;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this,Dashboard.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View,String>(image, "logo_image");
            pairs[1] = new Pair<View,String>(logo, "logo_text");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(intent, options.toBundle());
        }, SPLASH_SCREEN);
    }
}