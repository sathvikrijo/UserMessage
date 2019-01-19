package com.example.sathvikrijo.firebasetutor;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateValues extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_updated_name;
    private EditText editText_updated_msg;

    private Button button_update;
    private Button button_cancel;
    private Button button_delete;

    private String name;
    private String msg;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_values);

        editText_updated_name = findViewById(R.id.edt_updated_name);
        editText_updated_msg = findViewById(R.id.edt_updated_msg);
        button_update = findViewById(R.id.btn_update);
        button_cancel = findViewById(R.id.btn_cancel);
        button_delete = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        name = intent.getStringExtra("userInfo_Name");
        msg = intent.getStringExtra("userInfo_Msg");
        id = intent.getStringExtra("userInfo_ID");

        editText_updated_name.setText(name);
        editText_updated_msg.setText(msg);

        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);

        //int width = dm.widthPixels;
        //int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*.8),  (int)(height*.8));

        button_update.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        button_delete.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v == button_update) {
            name = editText_updated_name.getText().toString();
            msg = editText_updated_msg.getText().toString();
            new ProfileActivity().update(getApplicationContext(), new Information(), name, msg, id);
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(v == button_cancel) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(v == button_delete) {
            new ProfileActivity().delete(getApplicationContext(), id);
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
