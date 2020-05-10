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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {


    static int updateFlag=0;
    private String id;
    private EditText etEditName, etEditPhone, etEditUpiId;
    private DatabaseReference usrRef, updateRef;
    private Button btnConfirm;
    private boolean onceChecked=false;
    private String checkEmail, uEmail;

    public EditFragment() {
        // Required empty public constructor
    }

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            String updateName=etEditName.getText().toString();
            String updatePhone=etEditPhone.getText().toString();
            String updateUpiId=etEditUpiId.getText().toString();

            if (updateName.length() == 0) {

                etEditName.setError("Please enter your name");

            } else if (updatePhone.length() != 10) {

                etEditPhone.setError("Please enter a valid phone number");

            }
            else if(!updateUpiId.matches("[a-zA-Z0-9.\\-_]{3,256}@[a-zA-Z]{3,64}")){
                etEditUpiId.setError("Please enter a valid UPI ID");
            }
            else {

                etEditPhone.setError(null);
                etEditName.setError(null);
                etEditUpiId.setError(null);

            }

            btnConfirm.setEnabled(!updateName.isEmpty() && updatePhone.length() == 10);

            if (!updateName.isEmpty() && updatePhone.length() == 10)
                btnConfirm.setTextColor(Color.parseColor("#FFFFFF"));
            else
                btnConfirm.setTextColor(Color.parseColor("#E8F8EC"));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View parentHolder=inflater.inflate(R.layout.fragment_edit, container, false);

        etEditName=parentHolder.findViewById(R.id.etEditName);
        etEditPhone=parentHolder.findViewById(R.id.etEditPhone);
        etEditUpiId=parentHolder.findViewById(R.id.etEditUpiId);
        btnConfirm=parentHolder.findViewById(R.id.btnEditProfile);

        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();

        final DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        usrRef=dbRef.child("User");
        updateRef=dbRef.child("User");

        if (!onceChecked) {

            ValueEventListener userListener=new ValueEventListener() {

                final ProgressDialog pd=ProgressDialog.show(getActivity(), "Fetching Data", "Hang on...", true);

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        uEmail=ds.child("userMail").getValue(String.class);
                        assert uEmail != null;
                        if (uEmail.equals(checkEmail)) {

                            pd.dismiss();
                            String getName=ds.child("userName").getValue(String.class);
                            String getUpiId=ds.child("userUpiId").getValue(String.class);
                            String getPhone=ds.child("userPhone").getValue(String.class);
                            etEditName.setText(getName);
                            etEditUpiId.setText(getUpiId);
                            etEditPhone.setText(getPhone);
                            etEditName.addTextChangedListener(textWatcher);
                            etEditUpiId.addTextChangedListener(textWatcher);
                            etEditPhone.addTextChangedListener(textWatcher);
                            break;
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    pd.dismiss();
                    Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                }
            };

            usrRef.addListenerForSingleValueEvent(userListener);
            onceChecked=true;
        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String updateName=etEditName.getText().toString();
                final String updatePhone=etEditPhone.getText().toString();
                final String updateUpiId=etEditUpiId.getText().toString();

                final ProgressDialog pd=ProgressDialog.show(getActivity(), "Updating", "Hang on...", true);
                updateRef=dbRef.child("User");

                ValueEventListener userListener=new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            uEmail=ds.child("userMail").getValue(String.class);

                            assert uEmail != null;
                            if (uEmail.equals(checkEmail)) {

                                pd.dismiss();
                                id=ds.child("userId").getValue(String.class);
                                assert id != null;
                                usrRef.child(id).child("userName").setValue(updateName);
                                usrRef.child(id).child("userPhone").setValue(updatePhone);
                                usrRef.child(id).child("userUpiId").setValue(updateUpiId);
                                updateFlag=1;
                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("openProfile", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                break;
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        pd.dismiss();
                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_LONG).show();
                    }
                };

                updateRef.addListenerForSingleValueEvent(userListener);



            }
        });


        return parentHolder;
    }

}