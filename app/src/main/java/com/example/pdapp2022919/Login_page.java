package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button signupButton=(Button) findViewById(R.id.sign_up_button);
        Button signinButton=(Button) findViewById(R.id.sign_in_button);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openprofile();}
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openuser_Page();}
        });
    }
    private void  openprofile(){
        Intent intent=new Intent(this, profile_page.class);
        startActivity(intent);
    }
    private void  openuser_Page(){
        Intent intent=new Intent(this,user_Page.class);
        startActivity(intent);
    }
}