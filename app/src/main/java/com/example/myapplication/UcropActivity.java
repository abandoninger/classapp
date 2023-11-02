package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class UcropActivity extends AppCompatActivity {

    ImageView iv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucrop);
        //iv1=findViewById(R.id.iv1);
        //Intent intent=getIntent();
        //Uri picuri = intent.getData();
        //iv1.setImageURI(picuri);

    }
}