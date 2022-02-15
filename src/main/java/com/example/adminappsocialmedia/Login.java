package com.example.adminappsocialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


    private EditText loginEmailtext;
    private EditText loginPasswordtext;
    private Button loginbtn;
    private Button loginregisterbtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private CheckBox checkBox;
    TextView forgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progressBar);
        checkBox = findViewById(R.id.login_checkbox);
        loginEmailtext = findViewById(R.id.login_email);
        loginPasswordtext = findViewById(R.id.login_password);
        loginbtn = findViewById(R.id.login_button);
        loginregisterbtn = findViewById(R.id.login_register_button);
        forgotpass = findViewById(R.id.tv_forgotpass);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = loginEmailtext.getText().toString();
                String loginpass = loginPasswordtext.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) || !TextUtils.isEmpty(loginpass)){
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail, loginpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                sendtoMain();
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),"Error :" + error,Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });



                }else {
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    loginPasswordtext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    loginPasswordtext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmailtext.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Reset  Password")
                        .setMessage("Are you sure to reset?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(Login.this, "Reset Link sent ", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();
            }
        });


        loginregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

    }

    private void sendtoMain(){

//        FirebaseInstallations.getInstance().delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            //Log.d("Installations", "Installation deleted");
//                        } else {
//                            //  Log.e("Installations", "Unable to delete Installation");
//                           // Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);

        finish();

    }
    }
