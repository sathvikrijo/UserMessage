package com.example.sathvikrijo.firebasetutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText edtMail;
    private EditText edtPass;
    private TextView txtvSignup;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        btnLogin =  findViewById(R.id.btn_login);
        edtMail = findViewById(R.id.edt_mail);
        edtPass = findViewById(R.id.edt_password);
        txtvSignup = findViewById(R.id.tv_signup);

        btnLogin.setOnClickListener(this);
        txtvSignup.setOnClickListener(this);

    }

    private void userLogin(){
        String emailId = edtMail.getText().toString().trim();
        String passWord = edtPass.getText().toString().trim();

        if(TextUtils.isEmpty(emailId) && TextUtils.isEmpty(passWord)) {
            Toast.makeText(this,"Please enter the mail id and password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(emailId)){
            Toast.makeText(this,"Please enter the mail id",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passWord)){
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in.....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(emailId,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }

                else if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(Login.this, "Username is not registered", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(Login.this, "Username/Password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin){
            userLogin();
        }

        if(v == txtvSignup){
            finish();
            startActivity(new Intent(this, SignUp.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }

    }
}
