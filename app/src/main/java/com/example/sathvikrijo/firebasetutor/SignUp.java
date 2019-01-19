package com.example.sathvikrijo.firebasetutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.ProviderQueryResult;

//fir-authapp-8ffae
public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText edtMail;
    private EditText edtPass;
    private TextView txtvLogin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private boolean present = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        btnRegister = (Button) findViewById(R.id.btn_signup);
        edtMail = (EditText) findViewById(R.id.edt_mail);
        edtPass = (EditText) findViewById(R.id.edt_password);
        txtvLogin = (TextView) findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(this);
        txtvLogin.setOnClickListener(this);
    }

    private void registerUser() {
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

        progressDialog.setMessage("Registering user.....");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(emailId,passWord).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this,"Successfully Registered", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(SignUp.this, "Email id already exists", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUp.this,"Unable to register, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        if(v == btnRegister) {
            registerUser();
        }

        if(v == txtvLogin) {
            finish();
            startActivity(new Intent(this, Login.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
