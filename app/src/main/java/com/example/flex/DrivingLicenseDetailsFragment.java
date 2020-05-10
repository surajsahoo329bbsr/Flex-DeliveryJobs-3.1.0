package com.example.flex;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DrivingLicenseDetailsFragment extends Fragment {


    private View parentLayout;
    private TextView tvDLNumber, tvName, tvDOB, tvAddress, tvIssueDate, tvExpiryDate;
    private String checkMail="", uEmail="", dlNumber, dlName, DOB, address, issueDate, expiryDate;

    public DrivingLicenseDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentHolder=inflater.inflate(R.layout.fragment_driving_license_details, container, false);

        final ProgressDialog pd=ProgressDialog.show(getActivity(), "Loading License Details", "Please wait...", true);

        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkMail=user.getEmail();
        parentLayout=parentHolder.findViewById(android.R.id.content);

        tvDLNumber=parentHolder.findViewById(R.id.tvDLNumber);
        tvName=parentHolder.findViewById(R.id.tvDLName);
        tvDOB=parentHolder.findViewById(R.id.tvDOB);
        tvAddress=parentHolder.findViewById(R.id.tvAddress);
        tvIssueDate=parentHolder.findViewById(R.id.tvIssueDate);
        tvExpiryDate=parentHolder.findViewById(R.id.tvExpiryDate);


        DatabaseReference dlRef=dbRef.child("DrivingLicense");

        ValueEventListener dlListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uEmail=ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkMail)) {
                        dlNumber=ds.child("licenseNumber").getValue(String.class);
                        dlName=ds.child("userName").getValue(String.class);
                        DOB=ds.child("userDOB").getValue(String.class);
                        address=ds.child("userAddress").getValue(String.class);
                        issueDate=ds.child("licenseIssueDate").getValue(String.class);
                        expiryDate=ds.child("licenseExpiryDate").getValue(String.class);

                        break;
                    }
                }

                if (dlNumber != null && dlName != null && DOB != null && address != null && tvIssueDate != null && tvExpiryDate != null) {
                    tvDLNumber.setText(dlNumber);
                    tvName.setText(dlName);
                    tvDOB.setText(DOB);
                    tvAddress.setText(address);
                    tvIssueDate.setText(issueDate);
                    tvExpiryDate.setText(expiryDate);

                }

                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

                Snackbar.make(parentLayout, "Failed to load license details", Snackbar.LENGTH_LONG)
                        .setDuration(3000)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                        .show();


            }
        };

        dlRef.addListenerForSingleValueEvent(dlListener);


        return parentHolder;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}