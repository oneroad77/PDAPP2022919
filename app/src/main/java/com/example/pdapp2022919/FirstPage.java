package com.example.pdapp2022919;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.Profile.LoginPage;
import com.example.pdapp2022919.Profile.SignUpPage;
import com.example.pdapp2022919.R;

public class FirstPage extends ScreenSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        checkPermission();
        FileManager.setFileDir(getFilesDir().getAbsolutePath());
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN |  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        Button signupButton=(Button) findViewById(R.id.sign_up_button);
        Button signinButton=(Button) findViewById(R.id.sign_in_button);
        ImageView DTxlablogo = (ImageView) findViewById(R.id.DTxLabLogo);
        signinButton.setOnClickListener(view -> openProfile());
        signupButton.setOnClickListener(view -> openUserPage());
        DTxlablogo.setOnClickListener(view -> startActivity(new Intent(this, MainPage.class)));
    }

    private void openProfile(){
        Intent intent=new Intent(this, SignUpPage.class);
        startActivity(intent);
    }

    private void openUserPage(){
        Intent intent=new Intent(this, LoginPage.class);
        startActivity(intent);
    }


    /**確認是否有麥克風使用權限*/
    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
        /**相機權限*/
        if  (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }
    }

    /**取得權限回傳*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            finish();
        }
        if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            finish();
        }
    }

}