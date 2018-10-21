package com.example.krushna.endsem;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Login_Activity extends AppCompatActivity {

    private Button amb,pol,fire,pub;
    TextView txt;
    RelativeLayout layout;
    AnimationDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        amb = findViewById(R.id.btn_amb);
        pol = findViewById(R.id.btn_pol);
        fire = findViewById(R.id.btn_fire);
        pub = findViewById(R.id.btn_public);
        txt =findViewById(R.id.txt_login);
        layout = findViewById(R.id.rl1);
        drawable=(AnimationDrawable) layout.getBackground();
        drawable.setEnterFadeDuration(4500);
        drawable.setExitFadeDuration(4500);
        drawable.start();

        amb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                i.putExtra("type","amb");
                startActivity(i);

            }
        });

        pol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                i.putExtra("type","pol");
                startActivity(i);
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                i.putExtra("type","fire");
                startActivity(i);
            }
        });

        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                i.putExtra("type","pub");
                startActivity(i);
            }
        });
    }


}
