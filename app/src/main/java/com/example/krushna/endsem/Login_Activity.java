package com.example.krushna.endsem;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Login_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button amb,pol,fire,pub;
    EditText name,mob_no,veh_no;
    Button done;
    TextView txt;
    RelativeLayout layout;
    AnimationDrawable drawable;
    Spinner spinner;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        /*amb = findViewById(R.id.btn_amb);
        pol = findViewById(R.id.btn_pol);
        fire = findViewById(R.id.btn_fire);
        pub = findViewById(R.id.btn_public);
        txt =findViewById(R.id.txt_login);*/
        name=findViewById(R.id.et_name);
        mob_no=findViewById(R.id.et_mobile_no);
        veh_no=findViewById(R.id.et_veh_no);
        done=findViewById(R.id.btn_done);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        layout = findViewById(R.id.rl1);
        drawable=(AnimationDrawable) layout.getBackground();
        drawable.setEnterFadeDuration(4500);
        drawable.setExitFadeDuration(4500);
        drawable.start();

        List<String> categories = new ArrayList<String>();
        categories.add("Ambulance");
        categories.add("Fire");
        categories.add("Police");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this,MainActivity.class);
                i.putExtra("type",type);
                i.putExtra("name",name.getText().toString());
                i.putExtra("mob",mob_no.getText().toString());
                i.putExtra("veh_no",veh_no.getText().toString());
                Log.d("values-->", type+ "\t"+name.getText().toString()+"\t" + mob_no.getText().toString() +"\t" + veh_no.getText().toString());
                startActivity(i);
            }
        });

        /*amb.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
