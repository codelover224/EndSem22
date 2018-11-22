package com.example.krushna.endsem;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
        mob_no=findViewById(R.id.et_mobno);
        veh_no=findViewById(R.id.et_vehno);
        done=findViewById(R.id.btn_done);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        layout = findViewById(R.id.rl1);
        drawable=(AnimationDrawable) layout.getBackground();
        drawable.setEnterFadeDuration(4500);
        drawable.setExitFadeDuration(4500);
        drawable.start();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,0x1);

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
    public void onBackPressed() {
       // super.onBackPressed();
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Login_Activity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Login_Activity.this);
        }
        builder.setTitle("Quit?")
                .setMessage("Are you sure you want to quit this application?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        System.exit(0);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Login_Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Login_Activity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Login_Activity.this, new String[]{permission}, requestCode);
            }
        }
    }
}
