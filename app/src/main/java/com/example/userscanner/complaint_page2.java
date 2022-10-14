package com.example.userscanner;

import static android.R.layout.simple_spinner_dropdown_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class complaint_page2 extends AppCompatActivity {
    private TextView sn, make, model, procurement, powerRating, wexpiry, wperiod, ins_by, ins_date, mob,dep_of_pro,cost,config;
    private TextView location;
    private TextView cost1,cost2,config1,config2;
    private String location_str;

    private TextView verified_textview;
    private ImageView verify;
    public boolean VerifyBool=false;

    private TextView complainted_by_mob ;

    private EditText other_com;
    //private Spinner complainted_by_dep;
    private Spinner complaint_qrcode;
    private Button complaint_subBtn;
    private DatabaseReference refDash;

    Float rating;
    String rating_str;
    CheckBox vhigh ,high ,low;

    private String Senderemail, ReceiverEmail,Sendpass , StringHost;

    String uref;

    private String  complainted_by_mob_str, sn_str, make_str, model_str,
            procurement_str, powerRating_str, wexpiry_str, wperiod_str, ins_by_str, ins_date_str, mob_str,
            config_str,dep_of_pro_str,cost_str;
    private String complaint_txt,others_com_str;
    String status = "Pending";
    DatabaseReference databaseReference;
    String s,manual_name,manual_mob;
    String FeedBack_str;
    DatabaseReference dbRef;

    DatabaseReference refemail;

    TextInputLayout complaint_content;
    AutoCompleteTextView complaint_content_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_page2);

        uref= FirebaseAuth.getInstance().getUid();

        //final ProgressBar progressBar =  findViewById(R.id.progressbarforotp2);

        sn = (TextView) findViewById(R.id.sn_unit2);
        make = (TextView) findViewById(R.id.make_unit2);
        model = (TextView) findViewById(R.id.model_unit2);
        powerRating = (TextView) findViewById(R.id.powerRating_unit2);
        procurement = (TextView) findViewById(R.id.procurement_unit2);
        wperiod = (TextView) findViewById(R.id.warranty_unit2);
        wexpiry = (TextView) findViewById(R.id.warranty_exp_unit2);
        ins_by = (TextView) findViewById(R.id.ins_by_unit2);
        ins_date = (TextView) findViewById(R.id.ins_date_unit2);
        mob = (TextView) findViewById(R.id.mob_unit2);
        dep_of_pro=(TextView)findViewById(R.id.dep_of_pro_unit2);
        location=(TextView)findViewById(R.id.scanned_location2);
        cost=(TextView)findViewById(R.id.costA);
        config=(TextView)findViewById(R.id.ConfigA);
        verify=(ImageView) findViewById(R.id.verifiedMobSign2);

        cost1=(TextView)findViewById(R.id.cost1A);
        cost2=(TextView)findViewById(R.id.cost2A);
        config1=(TextView)findViewById(R.id.config1A);
        config2=(TextView)findViewById(R.id.config2A);

        complaint_subBtn = (Button) findViewById(R.id.button_complaint_submit2);

        complaint_qrcode=(Spinner) findViewById(R.id.complaint_Qrcode2);
        //complainted_by_name = (EditText) findViewById(R.id.scan_qr_com_name);
        complainted_by_mob = (TextView) findViewById(R.id.scan_qr_com_mob2);

        other_com=(EditText)findViewById(R.id.others_complaint_qr2);

        //complainted_by_dep = (Spinner) findViewById(R.id.scan_qr_com_dep);

//        vhigh =(CheckBox)findViewById(R.id.veryhighpriority);
//        high=(CheckBox)findViewById(R.id.highpriority);
//        low =(CheckBox)findViewById(R.id.lowpriority);

        LinearLayout priority = (LinearLayout) findViewById(R.id.prioritylayout2);

        s = getIntent().getStringExtra("SCAN_RESULT");
        complainted_by_mob_str=getIntent().getStringExtra("mobile");

        String[] com_scan={"Complaint","Not Working","Broken","Leakage","Others"};
        complaint_qrcode.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,com_scan));

        databaseReference = FirebaseDatabase.getInstance().getReference("Datas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mob_str = String.valueOf(snapshot.child(s).child("0").child("mob").getValue(Long.class));
                sn_str = String.valueOf(snapshot.child(s).child("0").child("sn_no").getValue(Long.class));
                make_str = snapshot.child(s).child("0").child("make").getValue(String.class);
                model_str = snapshot.child(s).child("0").child("model").getValue(String.class);
                procurement_str = snapshot.child(s).child("0").child("procurement").getValue(String.class);
                powerRating_str = snapshot.child(s).child("0").child("power_rating").getValue(String.class);
                wexpiry_str = snapshot.child(s).child("0").child("wexpiry").getValue(String.class);
                wperiod_str = snapshot.child(s).child("0").child("wperiod").getValue(String.class);
                ins_by_str = snapshot.child(s).child("0").child("ins_by").getValue(String.class);
                ins_date_str = snapshot.child(s).child("0").child("ins_date").getValue(String.class);
                dep_of_pro_str = snapshot.child(s).child("0").child("dep_of_pro").getValue(String.class);
                location_str = snapshot.child(s).child("0").child("location").getValue(String.class);
                cost_str=String.valueOf(snapshot.child(s).child("0").child("cost").getValue(Long.class));
                config_str=String.valueOf(snapshot.child(s).child("0").child("config").getValue(Long.class));
                rating = 0.0f;
                rating_str = rating.toString();
                FeedBack_str="None";

                if(dep_of_pro_str.equals("Assets")){
                    cost1.setVisibility(View.VISIBLE);
                    cost2.setVisibility(View.VISIBLE);
                    cost.setVisibility(View.VISIBLE);
                    config1.setVisibility(View.VISIBLE);
                    config2.setVisibility(View.VISIBLE);
                    config.setVisibility(View.VISIBLE);
                }

                sn.setText(sn_str);
                make.setText(make_str);
                model.setText(model_str);
                procurement.setText(procurement_str);
                powerRating.setText(powerRating_str);
                wexpiry.setText(wexpiry_str);
                wperiod.setText(wperiod_str);
                ins_by.setText(ins_by_str);
                ins_date.setText(ins_date_str);
                mob.setText(mob_str);
                dep_of_pro.setText(dep_of_pro_str);
                location.setText(location_str);
                cost.setText(cost_str);
                config.setText(config_str);
                complainted_by_mob.setText(complainted_by_mob_str);



//                if(!manual_name.equals("null")){
//                    complainted_by_name.setText(manual_name);
//                }
//                if(!manual_mob.equals("null")){
//                    complainted_by_mob.setText(manual_mob);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        complaint_qrcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                complaint_txt=complaint_qrcode.getSelectedItem().toString();
                if(complaint_txt.equals("Others")){
                    other_com.setVisibility(View.VISIBLE);
                }
                else{
                    other_com.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        complaint_subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(complaint_txt.equals("Others") && other_com.getText().toString().isEmpty()){
                    Toast.makeText(complaint_page2.this,"Please specify your complaint",Toast.LENGTH_SHORT).show();
                    other_com.requestFocus();
                }
                else if (complaint_txt.equals("Complaint")) {
                    Toast.makeText(complaint_page2.this, "Please select the Complaint", Toast.LENGTH_SHORT).show();
                    complaint_qrcode.requestFocus();
                }
                else{
                    getReciverEmail();
                    submitComplaint();
                }
//                else if (vhigh.getText().toString().isEmpty()) {
//                    vhigh.setError("Empty");
//                    vhigh.requestFocus();
//                }
            }

        });

        VerifyBool= Boolean.parseBoolean(getIntent().getStringExtra("verifyboolean"));
        if(VerifyBool){
            verified_textview.setVisibility(View.VISIBLE);
        }

    }

    private void submitComplaint() {
        dbRef = FirebaseDatabase.getInstance().getReference().child("complaints").child(dep_of_pro_str);
        final String uniqueKey = dbRef.push().getKey();

        //complainted_by_name_str = complainted_by_name.getText().toString();
        //complainted_by_mob_str = complainted_by_mob.getText().toString();
        if(complaint_txt.equals("Others")){
            complaint_txt=other_com.getText().toString();
        }

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());


        Complaint_details complaint_details = new Complaint_details("null", complainted_by_mob_str,
                "null", complaint_txt, sn_str,
                make_str, model_str, procurement_str,
                powerRating_str, wperiod_str, wexpiry_str, ins_by_str, ins_date_str, mob_str, date, time, uniqueKey, s,
                status,dep_of_pro_str,uref,location_str,rating_str,FeedBack_str);

        //refDash= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());


        dbRef.child(uniqueKey).setValue(complaint_details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //sendemail();
                Toast.makeText(complaint_page2.this, "Complaint Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(complaint_page2.this, ScannerPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(complaint_page2.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReciverEmail() {
        refemail=FirebaseDatabase.getInstance().getReference("Emails");
        if(dep_of_pro_str.equals("Electricity")){
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(dep_of_pro_str.equals("Plumber")) {
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail = snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(dep_of_pro_str.equals("Network")){
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(dep_of_pro_str.equals("Carpenter")){
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(dep_of_pro_str.equals("Painting")){
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            refemail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReceiverEmail=snapshot.child("other").child("email").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
}