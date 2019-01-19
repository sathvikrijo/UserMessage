package com.example.sathvikrijo.firebasetutor;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public FirebaseAuth firebaseAuth;

    private FirebaseUser user;

    private Button logoutButton;
    private TextView textViewUserText;

    public DatabaseReference databaseReference;

    private EditText editTextMessage;
    private EditText editTextUser;
    private Button buttonAddMsg;

    private ListView listView;
    private List<Information> userInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        user = firebaseAuth.getCurrentUser();

        logoutButton = findViewById(R.id.btn_logout);
        textViewUserText = findViewById(R.id.tv_username);
        editTextMessage = findViewById(R.id.edt_msg);
        editTextUser = findViewById(R.id.edt_username);
        buttonAddMsg = findViewById(R.id.btn_storemsg);
        listView = findViewById(R.id.list_view);

        textViewUserText.setText("Welcome " + user.getEmail());

        listView.setOnItemClickListener(this);
        logoutButton.setOnClickListener(this);
        buttonAddMsg.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userInfoList.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Information perUserInfo = userSnapshot.getValue(Information.class);
                    userInfoList.add(perUserInfo);
                }

                UserList userInfoAdapter = new UserList(ProfileActivity.this, userInfoList);
                listView.setAdapter(userInfoAdapter);
                userInfoAdapter.setNotifyOnChange(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void userInformation(){
        String msg = editTextMessage.getText().toString().trim();
        String username = editTextUser.getText().toString().trim();

        //FirebaseUser user = firebaseAuth.getCurrentUser();
        String id = databaseReference.push().getKey();

        Information userInfo = new Information(msg,username, id);

        databaseReference.child(id).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully msg added", Toast.LENGTH_SHORT).show();
                }
                else if(task.isCanceled()) {
                    Toast.makeText(getApplicationContext(), "Msg can't be added, try again", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void update(final Context mcontext, Information userInfo, String name, String message, String key) {
        userInfo.setName(name);
        userInfo.setMsg(message);
        userInfo.setId(key);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid());
        databaseReference.child(key).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(mcontext,"Updated successfully", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(task.isCanceled()) {
                    Toast.makeText(mcontext,"Can't able to update, please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void delete(final Context mcontext, String key) {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(firebaseAuth.getCurrentUser().getUid()).child(key);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mcontext,"Deleted Sucessfully",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == logoutButton) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,Login.class));
        }

        if(v == buttonAddMsg) {
            userInformation();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Information gotInfo = userInfoList.get(position);
         //Toast.makeText(this, gotInfo.getName() + "\n" + gotInfo.getMsg(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UpdateValues.class);
        intent.putExtra("userInfo_Name", gotInfo.getName());
        intent.putExtra("userInfo_Msg", gotInfo.getMsg());
        intent.putExtra("userInfo_ID", gotInfo.getId());
        //finish();
        startActivity(intent);
        //update(gotInfo,"HELLO","CHANGED",gotInfo.getId());
    }
}
