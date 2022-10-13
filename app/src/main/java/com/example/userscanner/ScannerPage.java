package com.example.userscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScannerPage extends AppCompatActivity {
    LinearLayout scanBtn;
    public static TextView scantxt;
    Button num_enter_man;
    TextView manualltext;
    MaterialToolbar toolbar;
    private DatabaseReference refDash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn=findViewById(R.id.Scanner);
//        scantxt=(TextView) findViewById(R.id.scantxt);
        manualltext=findViewById(R.id.entermanuallytext);

        //refDash= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());

        num_enter_man=findViewById(R.id.num_enter_man);

//        num_enter_man.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ScannerPage.this,Enter_num_manual.class));
//            }
//        });

//        manual =(TextView) findViewById(R.id.ManualEntry);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScannerView.class));
            }
        });

//        manualltext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ScannerPage.this, ManualComplaint_page.class));
//            }
//        });
    }
}