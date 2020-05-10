package com.example.flex;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFeedbackFragment extends Fragment {


    public ViewFeedbackFragment() {
        // Required empty public constructor
    }

    LinearLayout linearLayout;
    private TextView tvSetRating, tvSetComment, tvSetDate;
    private String checkEmail, uEmail;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentHolder=inflater.inflate(R.layout.fragment_view_feedback, container,
                false);

        tvSetComment=parentHolder.findViewById(R.id.tvSetComment);
        tvSetRating=parentHolder.findViewById(R.id.tvSetRating);
        tvSetDate=parentHolder.findViewById(R.id.tvSetDate);


        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();

        final ProgressDialog pd=ProgressDialog.show(getActivity(), "Getting Feedback", "Please wait...", true);

        DatabaseReference fdbRef=dbRef.child("Feedback");

        ValueEventListener feedbackListener=new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    uEmail=ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkEmail)) {

                        String getRating=ds.child("rating").getValue(String.class);
                        String getFeedback=ds.child("feedback").getValue(String.class);
                        String getDate=ds.child("date").getValue(String.class);

                        assert getRating != null;
                        if (!getRating.equals("0")) {

                            double stars=Double.parseDouble(getRating);

                            if (stars != 0.0) {

                                NumberFormat nf=new DecimalFormat("#.####");
                                String strStars=nf.format(stars);
                                tvSetRating.setText("Rating : " + strStars + " / 5");
                                tvSetRating.setVisibility(View.VISIBLE);

                            } else {

                                tvSetRating.setText("Rating : 1 / 5");
                                tvSetRating.setVisibility(View.VISIBLE);
                            }

                            tvSetDate.setText(getDate);

                            pd.dismiss();


                        }

                        assert getFeedback != null;
                        if (!getFeedback.equals("")) {

                            tvSetComment.setText(getFeedback);
                            tvSetComment.setVisibility(View.VISIBLE);
                        }


                        break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

            }
        };

        fdbRef.addListenerForSingleValueEvent(feedbackListener);

        pd.dismiss();

        return parentHolder;
    }

}
