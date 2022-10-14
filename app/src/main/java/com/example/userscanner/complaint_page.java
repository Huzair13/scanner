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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class complaint_page extends AppCompatActivity {

    private TextView sn, make, model, procurement, powerRating, wexpiry, wperiod, ins_by, ins_date, mob,dep_of_pro,cost,config;
    private TextView location;
    private TextView cost1,cost2,config1,config2;
    private String location_str;

    private TextView verified_textview;
    private Button verify;
    public boolean VerifyBool=false;

    private EditText  complainted_by_mob ;
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
        setContentView(R.layout.activity_complaint_page);

        uref= FirebaseAuth.getInstance().getUid();

        final ProgressBar progressBar =  findViewById(R.id.progressbarforotp);

        sn = (TextView) findViewById(R.id.sn_unit);
        make = (TextView) findViewById(R.id.make_unit);
        model = (TextView) findViewById(R.id.model_unit);
        powerRating = (TextView) findViewById(R.id.powerRating_unit);
        procurement = (TextView) findViewById(R.id.procurement_unit);
        wperiod = (TextView) findViewById(R.id.warranty_unit);
        wexpiry = (TextView) findViewById(R.id.warranty_exp_unit);
        ins_by = (TextView) findViewById(R.id.ins_by_unit);
        ins_date = (TextView) findViewById(R.id.ins_date_unit);
        mob = (TextView) findViewById(R.id.mob_unit);
        dep_of_pro=(TextView)findViewById(R.id.dep_of_pro_unit);
        location=(TextView)findViewById(R.id.scanned_location);
        cost=(TextView)findViewById(R.id.cost);
        config=(TextView)findViewById(R.id.Config);

        verify=(Button)findViewById(R.id.verifyMobNum);
        verified_textview=(TextView)findViewById(R.id.verifiedMobSign);

        cost1=(TextView)findViewById(R.id.cost1);
        cost2=(TextView)findViewById(R.id.cost2);
        config1=(TextView)findViewById(R.id.config1);
        config2=(TextView)findViewById(R.id.config2);

        complaint_subBtn = (Button) findViewById(R.id.button_complaint_submit);

        complaint_qrcode=(Spinner) findViewById(R.id.complaint_Qrcode);
        //complainted_by_name = (EditText) findViewById(R.id.scan_qr_com_name);
        complainted_by_mob = (EditText) findViewById(R.id.scan_qr_com_mob);

        other_com=(EditText)findViewById(R.id.others_complaint_qr);

        //complainted_by_dep = (Spinner) findViewById(R.id.scan_qr_com_dep);

//        vhigh =(CheckBox)findViewById(R.id.veryhighpriority);
//        high=(CheckBox)findViewById(R.id.highpriority);
//        low =(CheckBox)findViewById(R.id.lowpriority);

        LinearLayout priority = (LinearLayout) findViewById(R.id.prioritylayout);

        s = getIntent().getStringExtra("SCAN_RESULT");
        VerifyBool= Boolean.parseBoolean(getIntent().getStringExtra("boolean123"));
        complainted_by_mob_str=getIntent().getStringExtra("mobile");

        String[] com_scan={"Complaint","Not Working","Broken","Leakage","Others"};
        complaint_qrcode.setAdapter(new ArrayAdapter<String>(this, simple_spinner_dropdown_item,com_scan));

        databaseReference = FirebaseDatabase.getInstance().getReference("Datas");



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(VerifyBool){
                    Toast.makeText(complaint_page.this, "Already verified", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (!complainted_by_mob.getText().toString().trim().isEmpty()){
                        if ((complainted_by_mob.getText().toString().trim()).length() == 10){
                            progressBar.setVisibility(View.VISIBLE);
                            verify.setVisibility(View.INVISIBLE);

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + complainted_by_mob.getText().toString(),
                                    60,
                                    TimeUnit.SECONDS,
                                    complaint_page.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            progressBar.setVisibility(View.GONE);
                                            verify.setVisibility(View.VISIBLE);
                                            Toast.makeText(complaint_page.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verficationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            progressBar.setVisibility(View.GONE);
                                            verify.setVisibility(View.VISIBLE);
                                            Intent intent = new Intent(getApplicationContext(),verifyPage.class);
                                            intent.putExtra("mobile",complainted_by_mob.getText().toString());
                                            intent.putExtra("verfication",verficationid);
                                            intent.putExtra("scanresult",s);
                                            intent.putExtra("boolean123",VerifyBool);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            progressBar.setVisibility(View.GONE);
                                            verify.setVisibility(View.VISIBLE);
                                        }
                                    }

                            );



                        }else {
                            Toast.makeText(complaint_page.this,"Please enter correct number",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(complaint_page.this,"Enter Mobile number",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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
                    Toast.makeText(complaint_page.this,"Please specify your complaint",Toast.LENGTH_SHORT).show();
                    other_com.requestFocus();
                }
                else if (complaint_txt.equals("Complaint")) {
                    Toast.makeText(complaint_page.this, "Please select the Complaint", Toast.LENGTH_SHORT).show();
                    complaint_qrcode.requestFocus();
                } else if (complainted_by_mob.getText().toString().isEmpty()) {
                    complainted_by_mob.setError("Empty");
                    complainted_by_mob.requestFocus();
                }
                else{
                    Toast.makeText(complaint_page.this, "Your mobile number is not verified", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

//    private void submitComplaint() {
//
//        dbRef = FirebaseDatabase.getInstance().getReference().child("complaints").child(dep_of_pro_str);
//        final String uniqueKey = dbRef.push().getKey();
//
//        //complainted_by_name_str = complainted_by_name.getText().toString();
//        complainted_by_mob_str = complainted_by_mob.getText().toString();
//        if(complaint_txt.equals("Others")){
//            complaint_txt=other_com.getText().toString();
//        }
//
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
//        String date = currentDate.format(calForDate.getTime());
//
//        Calendar calForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
//        String time = currentTime.format(calForTime.getTime());
//
//
//        Complaint_details complaint_details = new Complaint_details("null", complainted_by_mob_str,
//                "null", complaint_txt, sn_str,
//                make_str, model_str, procurement_str,
//                powerRating_str, wperiod_str, wexpiry_str, ins_by_str, ins_date_str, mob_str, date, time, uniqueKey, s,
//                status,dep_of_pro_str,uref,location_str,rating_str,FeedBack_str);
//
//        //refDash= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
//
//
//        dbRef.child(uniqueKey).setValue(complaint_details).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                //sendemail();
//                Toast.makeText(complaint_page.this, "Complaint Registered Successfully", Toast.LENGTH_SHORT).show();
//                refDash.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Intent intent=new Intent(complaint_page.this, ScannerPage.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(complaint_page.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    //private void getReciverEmail() {
//        refemail=FirebaseDatabase.getInstance().getReference("Emails");
//        if(dep_of_pro_str.equals("Electricity")){
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }else if(dep_of_pro_str.equals("Plumber")) {
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail = snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }else if(dep_of_pro_str.equals("Network")){
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }else if(dep_of_pro_str.equals("Carpenter")){
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }else if(dep_of_pro_str.equals("Painting")){
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail=snapshot.child(dep_of_pro_str).child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        else{
//            refemail.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ReceiverEmail=snapshot.child("other").child("email").getValue(String.class);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//
//        }
//    }

//    private void sendemail() {
//        SendMail mail=new SendMail(Senderemail,
//                "ywfcjyswheezxmde",
//                ReceiverEmail,//panneerselvamm@sonatech.ac.in
//                "Complaint in "+dep_of_pro_str+" Department",
//                "Complained by "+ complainted_by_name.getText().toString() +"\n" +
//                        "COMPLAINT: "+ complaint_txt
//        );
//        mail.execute();
//    }
    }