package com.example.touristattraction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyPage extends Activity {
    Button btn_attraction;
    Button btn_reservation;
    Button btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);
        btn_attraction = findViewById(R.id.my_page_my_attraction_button);
        btn_attraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyAttractionPage.class);
                startActivity(intent);
            }
        });
        btn_reservation = findViewById(R.id.my_page_my_reservation_button);
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ReservationList.class);
                startActivity(intent);
            }
        });
        btn_home = findViewById(R.id.my_page_home_button);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}