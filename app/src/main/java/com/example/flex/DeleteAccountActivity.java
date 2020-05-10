package com.example.flex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DeleteAccountActivity extends AppCompatActivity {

    private DatabaseReference usrRef;
    private DatabaseReference fdbRef;
    private DatabaseReference slotRef;
    private DatabaseReference dlRef;
    private DatabaseReference hisRef;
    private String uEmail, password;
    private StorageReference mStorageReference;
    private FirebaseUser user;
    private EditText etDeletePassword;
    private Button btnDeleteAccount;
    private View parentLayout;
    static int deleteAccountFlag = 0;
    private ProgressDialog pd;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String password=etDeletePassword.getText().toString().trim();

            if (password.length() < 8)
                etDeletePassword.setError("Password must have atleast 8 characters");
            else
                etDeletePassword.setError(null);

            btnDeleteAccount.setEnabled(password.length() >= 8);

            if (password.length() < 8)
                btnDeleteAccount.setTextColor(Color.parseColor("#E8F8EC"));
            else
                btnDeleteAccount.setTextColor(Color.parseColor("#FFFFFF"));


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(DeleteAccountActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(DeleteAccountActivity.this, MainActivity.class);
        intent.putExtra("openProfile", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        finish();
    }

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(DeleteAccountActivity.this, android.R.color.background_light));// set status background white
        }

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        parentLayout = findViewById(android.R.id.content);
        btnDeleteAccount=findViewById(R.id.btnConfirmDeleteAccount);
        etDeletePassword=findViewById(R.id.etDeletePassword);
        etDeletePassword.addTextChangedListener(textWatcher);

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uEmail = user.getEmail();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        usrRef=dbRef.child("User");
        fdbRef=dbRef.child("Feedback");
        slotRef=dbRef.child("Slot");
        dlRef=dbRef.child("DrivingLicense");
        hisRef=dbRef.child("History");

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd=ProgressDialog.show(DeleteAccountActivity.this, "Deleting account", "Please wait...", true);

                password=etDeletePassword.getText().toString();

                if (password.equals(""))
                {
                    pd.dismiss();

                    Snackbar.make(parentLayout,"Please re-enter your password", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                }
                else {

                    AuthCredential credential=EmailAuthProvider.getCredential(uEmail, password);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                pd.dismiss();

                                new AlertDialog.Builder(DeleteAccountActivity.this)
                                        .setIcon(R.drawable.ic_launcher_round)
                                        .setTitle("Deleting account")
                                        .setMessage("Are you sure ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                pd=ProgressDialog.show(DeleteAccountActivity.this, "Deleting account", "Please wait...", true);

                                                Query emailQuery=usrRef.orderByChild("userMail").equalTo(uEmail);

                                                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//User Database deleted


                                                Query feedbackQuery=fdbRef.orderByChild("userMail").equalTo(uEmail);

                                                feedbackQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();

                                                    }
                                                });//Feedback Database deleted

                                                Query slotQuery=slotRef.orderByChild("userMail").equalTo(uEmail);

                                                slotQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//Slot Database deleted

                                                Query histQuery=hisRef.orderByChild("userMail").equalTo(uEmail);

                                                histQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//History Database deleted


                                                Query dlQuery=dlRef.orderByChild("userMail").equalTo(uEmail);

                                                dlQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        for (DataSnapshot ds : dataSnapshot.getChildren())
                                                            ds.getRef().removeValue();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        pd.dismiss();
                                                    }
                                                });//DrivingLicense Database deleted

                                                StorageReference photoRef=mStorageReference.child(uEmail).child("photo.jpg");
                                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        pd.dismiss();
                                                    }
                                                }); // Photo deleted

                                                StorageReference DLRef=mStorageReference.child(uEmail).child("userDL.pdf");
                                                DLRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        pd.dismiss();
                                                     }
                                                }); // DrivingLicense deleted

                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            pd.dismiss();
                                                            deleteAccountFlag = 1;
                                                            startActivity(new Intent(DeleteAccountActivity.this, LoginActivity.class));

                                                        } else {
                                                            pd.dismiss();
                                                        }
                                                    }
                                                });

                                            }
                                        }).setNegativeButton("No", null).show();
                            } else {

                                pd.dismiss();
                                Snackbar.make(parentLayout,"Invalid password", Snackbar.LENGTH_LONG)
                                        .setDuration(3000)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                        .show();

                            }
                        }

                    });//Auth deleted

                }
            }


        });

    }

}