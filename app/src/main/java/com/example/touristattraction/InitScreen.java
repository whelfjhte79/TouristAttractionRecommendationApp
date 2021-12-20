package com.example.touristattraction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_screen);

        Button loginPageButton = findViewById(R.id.init_page_login_button);
        loginPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
            }

        });
        Button signUpPageButton = findViewById(R.id.init_page_signup_button);
        signUpPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),SignUpPage.class);
                startActivity(intent);
            }

        });

    }
}