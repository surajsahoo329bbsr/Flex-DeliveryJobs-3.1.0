package com.example.flex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChangePasswordFragment extends Fragment {

    static int resetPass=0;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private DatabaseReference dbRef, usrRef;
    private String checkEmail, uEmail;
    View parentLayout;
    private Button btnChangePassword;

    public ChangePasswordFragment() {

    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String currentPass=etCurrentPassword.getText().toString();
            String newPass=etNewPassword.getText().toString();
            String confirmPass=etConfirmNewPassword.getText().toString();

            if (currentPass.length() < 8)
                etCurrentPassword.setError("Current password must have at least 8 characters");
            else if (newPass.length() < 8)
                etNewPassword.setError("New password must have at least 8 characters");
            else if (currentPass.equals(newPass))
                etNewPassword.setError("Current password and new password are same");
            else if (!newPass.equals(confirmPass))
                etConfirmNewPassword.setError("Passwords do not match");
            else {
                etCurrentPassword.setError(null);
                etNewPassword.setError(null);
                etConfirmNewPassword.setError(null);
            }

            boolean b=currentPass.length() >= 8 && newPass.length() >= 8 && !currentPass.equals(newPass) && newPass.equals(confirmPass);
            btnChangePassword.setEnabled(b);

            if (b)
                btnChangePassword.setTextColor(Color.parseColor("#FFFFFF"));
            else
                btnChangePassword.setTextColor(Color.parseColor("#E8F8EC"));

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View parentHolder=inflater.inflate(R.layout.fragment_change_password, container, false);

        parentLayout=Objects.requireNonNull(getActivity()).findViewById(android.R.id.content);

        etCurrentPassword=parentHolder.findViewById(R.id.etCurrentPassword);
        etNewPassword=parentHolder.findViewById(R.id.etNewPassword);
        etConfirmNewPassword=parentHolder.findViewById(R.id.etConfirmNewPassword);
        btnChangePassword=parentHolder.findViewById(R.id.btnChangePassword);

        etConfirmNewPassword.addTextChangedListener(textWatcher);
        etNewPassword.addTextChangedListener(textWatcher);
        etCurrentPassword.addTextChangedListener(textWatcher);

        dbRef=FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usrRef=dbRef.child("User");
                final String currentPass=etCurrentPassword.getText().toString();
                final String confirmPass=etConfirmNewPassword.getText().toString();


                final ProgressDialog pd=ProgressDialog.show(getActivity(), "Changing password", "Please wait...", true);
                ValueEventListener userListener=new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            uEmail=ds.child("userMail").getValue(String.class);

                            assert uEmail != null;
                            if (uEmail.equals(checkEmail)) {

                                AuthCredential credential=EmailAuthProvider.getCredential(checkEmail, currentPass);

                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            user.updatePassword(confirmPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        pd.dismiss();
                                                        resetPass=1;
                                                        Intent intent=new Intent(getActivity(), MainActivity.class);
                                                        intent.putExtra("openProfile", true);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        startActivity(intent);

                                                    } else {

                                                        pd.dismiss();

                                                    }
                                                }


                                            });
                                        } else {
                                            pd.dismiss();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        pd.dismiss();
                                        Snackbar.make(parentLayout, "Old password and current password do not match", Snackbar.LENGTH_LONG)
                                                .setDuration(3000)
                                                .setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                .show();

                                    }
                                });

                                break;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                };

                usrRef.addListenerForSingleValueEvent(userListener);


            }
        });

        return parentHolder;
    }
}