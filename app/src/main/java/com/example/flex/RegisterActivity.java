package com.example.flex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements ValueEventListener {

    private EditText etName, etEmail, etPhone, etUpiId, etPassword, etConfirmPassword;
    private FirebaseAuth auth;
    private DatabaseReference databaseUser, databaseFeedback, databaseSlot, databaseDL;
    private View parentLayout;
    static int registerFlag=0;
    Button btnSignUp;
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String name=etName.getText().toString();
            String phone_no=etPhone.getText().toString().trim();
            String email=etEmail.getText().toString().trim();
            String password=etPassword.getText().toString().trim();
            String upiId = etUpiId.getText().toString().trim();
            String confirmPassword=etConfirmPassword.getText().toString().trim();

            if (name.length() == 0)
                etName.setError("Please enter your name");
            else if (phone_no.length() != 10)
                etPhone.setError("Invalid phone number");
            else if (email.length() == 0)
                etEmail.setError("Please enter your email");
            else if (!upiId.matches("[a-zA-Z0-9.\\-_]{3,256}@[a-zA-Z]{3,64}"))
                etUpiId.setError("Please enter valid UPI ID");
            else if (password.length() < 8)
                etPassword.setError("Password must have at least 8 characters");
            else if (!password.equals(confirmPassword))
                etConfirmPassword.setError("Passwords do not match");
            else
                etEmail.setError(null);

            final boolean b=!name.isEmpty() && phone_no.length() == 10 && !email.isEmpty() && password.length() >= 8 && confirmPassword.equals(password);
            btnSignUp.setEnabled(b);

            if (b)
                btnSignUp.setTextColor(Color.parseColor("#FFFFFF"));
            else
                btnSignUp.setTextColor(Color.parseColor("#E8F8EC"));

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);


        etName=findViewById(R.id.etNameRegister);
        etEmail=findViewById(R.id.etEmailRegister);
        etPhone=findViewById(R.id.etPhoneRegister);
        etUpiId=findViewById(R.id.etUpiIdRegister);
        etPassword=findViewById(R.id.etPasswordRegister);
        etConfirmPassword=findViewById(R.id.etConfirmPasswordRegister);
        btnSignUp=findViewById(R.id.btnSignUp);
        TextView tvLogin=findViewById(R.id.tvLogin);
        auth=FirebaseAuth.getInstance();
        parentLayout=findViewById(android.R.id.content);

        etName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);

        databaseUser=FirebaseDatabase.getInstance().getReference("User");
        databaseFeedback=FirebaseDatabase.getInstance().getReference("Feedback");
        databaseSlot=FirebaseDatabase.getInstance().getReference("Slot");
        databaseDL=FirebaseDatabase.getInstance().getReference("DrivingLicense");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                addUser();
                addFeedback();
                addDrivingLicense();
                addSlot();
            }

        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private void addUser() {

        final String name=etName.getText().toString().trim();
        final String mail=etEmail.getText().toString().trim();
        final String phone=etPhone.getText().toString().trim();
        final String password=etPassword.getText().toString().trim();
        final  String upiId =etUpiId.getText().toString().trim();
        final int DLFlag=0, PhotoFlag=0;


        final ProgressDialog pd=ProgressDialog.show(RegisterActivity.this, "Creating account", "Please wait", true);


        auth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            pd.dismiss();

                            try {
                                throw Objects.requireNonNull(task.getException());
                            }
                            // if user enters wrong email.

                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Snackbar.make(parentLayout, "Please enter a valid email", Snackbar.LENGTH_LONG)
                                        .setDuration(5000)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                        .show();

                                // TODO: Take your action
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Snackbar.make(parentLayout, "You have already registered. Please Login.", Snackbar.LENGTH_LONG)
                                        .setDuration(5000)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                        .show();

                                // TODO: Take your action
                            } catch (Exception e) {
                                Snackbar.make(parentLayout, "Unable to create account. Please try again. ", Snackbar.LENGTH_LONG)
                                        .setDuration(5000)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                        .show();
                            }


                        } else {

                            pd.dismiss();
                            FirebaseUser userCheck=FirebaseAuth.getInstance().getCurrentUser();
                            String id=databaseUser.push().getKey();
                            User user=new User(id, name, mail, phone, upiId, DLFlag, PhotoFlag);
                            assert id != null;
                            databaseUser.child(id).setValue(user);
                            registerFlag=1;
                            assert userCheck != null;
                            userCheck.sendEmailVerification();
                            Intent it=new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(it);
                            finish();

                        }
                    }
                });


    }

    private void addDrivingLicense() {

        String usrMail=etEmail.getText().toString().trim();
        String userName="", LicenseNumber="", userDOB="", userAddress="", LicenseIssueDate="", LicenseExpiryDate="";
        String id=databaseDL.push().getKey();
        DrivingLicense drivingLicense=new DrivingLicense(id, usrMail, userName, LicenseNumber, userDOB, userAddress, LicenseIssueDate, LicenseExpiryDate);
        assert id != null;
        databaseDL.child(id).setValue(drivingLicense);

    }

    private void addSlot() {
        String usrMail=etEmail.getText().toString().trim();
        String showDate="", showStartDate="", showWorkHours="";
        String id=databaseSlot.push().getKey();
        int slotFlag=0;
        Slot slot=new Slot(usrMail, id, slotFlag, showDate, showStartDate, showWorkHours);
        assert id != null;
        databaseSlot.child(id).setValue(slot);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void addFeedback() {

        String checkEmail=etEmail.getText().toString().trim();
        String feedbackRating="0", feedbackText="", feedbackDate="";
        String id=databaseFeedback.push().getKey();
        Feedback feedback=new Feedback(id, checkEmail, feedbackRating, feedbackText, feedbackDate);
        assert id != null;
        databaseFeedback.child(id).setValue(feedback);

    }

    @Override
    public void onBackPressed() {
        Intent it=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(it);
        finish();
    }


}