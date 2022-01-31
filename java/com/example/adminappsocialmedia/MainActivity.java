package com.example.adminappsocialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mauth;
    TextView tvsingout,showreport;
    EditText titleEt,messageEt;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = FirebaseAuth.getInstance();

        titleEt = findViewById(R.id.title_fcm);
        messageEt = findViewById(R.id.message_fcm);
        button = findViewById(R.id.btn_fcm);



        tvsingout = findViewById(R.id.signouttv);
        showreport = findViewById(R.id.showreporttv);

        tvsingout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEt.getText().toString().trim();
                String title = titleEt.getText().toString().trim();

                if (message.isEmpty() || title.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill empty fields", Toast.LENGTH_SHORT).show();
                }else {
                    sendfcm(title,message);
                }
            }
        });


            showreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Reportposts.class);
                startActivity(intent);
            }
        });


    }

    private void sendfcm(String title, String message) {

        FcmNotificationsSender notificationsSender =
                new FcmNotificationsSender("/topics/all", title,message,
                        getApplicationContext(), MainActivity.this);

        notificationsSender.SendNotifications();
    }
}