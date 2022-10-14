package com.example.userscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyPage extends AppCompatActivity {

    private EditText inputcode1, inputcode2, inputcode3, inputcode4, inputcode5, inputcode6;
    private String verificationid,s,str;
    private boolean verifybool;
    PhoneAuthCredential phoneAuthCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_page);


        TextView textView = findViewById(R.id.textmobileshownumber);
        str=getIntent().getStringExtra("mobile");
        textView.setText(String.format(
                "+91-%s", str
        ));

        inputcode1 = findViewById(R.id.inputotp1);
        inputcode2 = findViewById(R.id.inputotp2);
        inputcode3 = findViewById(R.id.inputotp3);
        inputcode4 = findViewById(R.id.inputotp4);
        inputcode5 = findViewById( R.id.inputotp5);
        inputcode6 = findViewById(R.id.inputotp6);

        setupotpinput();

        final ProgressBar progressBar = findViewById(R.id.progressbar_verify_otp);

        final Button buttonverify = findViewById(R.id.buttongetotp);

        verificationid = getIntent().getStringExtra("verfication");
        verifybool= Boolean.parseBoolean(getIntent().getStringExtra("boolean123"));
        s=getIntent().getStringExtra("scanresult");

        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputcode1.getText().toString().trim().isEmpty()
                        && !inputcode2.getText().toString().trim().isEmpty()
                        && !inputcode3.getText().toString().trim().isEmpty()
                        && !inputcode4.getText().toString().trim().isEmpty()
                        && !inputcode5.getText().toString().trim().isEmpty()
                        && !inputcode6.getText().toString().trim().isEmpty()) {
                    String code = inputcode1.getText().toString() +
                            inputcode2.getText().toString() +
                            inputcode3.getText().toString() +
                            inputcode4.getText().toString() +
                            inputcode5.getText().toString() +
                            inputcode6.getText().toString();

                    if (verificationid != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        buttonverify.setVisibility(View.INVISIBLE);
                        phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationid, code
                        );
                    }

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonverify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), complaint_page2.class);
                                        verifybool=true;
                                        //intent.putExtra("boolean123",verifybool);
                                        intent.putExtra("mobile",str);
                                        intent.putExtra("SCAN_RESULT",s);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(verifyPage.this, "Enter the Correct otp", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(verifyPage.this, "Please enter the otp", Toast.LENGTH_SHORT).show();
                }
            }
            });
        findViewById(R.id.textresendotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        verifyPage.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(verifyPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationid = newVerification;
                                Toast.makeText(verifyPage.this, "OTP sent again", Toast.LENGTH_SHORT).show();
                            }
                        }

                );
            }
        });
        }

    private void setupotpinput() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}