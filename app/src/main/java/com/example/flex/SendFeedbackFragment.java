package com.example.flex;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendFeedbackFragment extends Fragment {

    private View parentHolder;
    private TextView tvRating;
    private TextView tvComment;
    private EditText etFeedback;
    private String feedbackText="", checkEmail, uEmail, feedbackRating="0";
    private DatabaseReference dbRef;
    private DatabaseReference fdbRef;
    private Button btnFeedback;

    public SendFeedbackFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        parentHolder=inflater.inflate(R.layout.fragment_send_feedback, container,
                false);

        btnFeedback=parentHolder.findViewById(R.id.btnSubmitFeedback);
        etFeedback=parentHolder.findViewById(R.id.etFeedback);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail=user.getEmail();
        dbRef =FirebaseDatabase.getInstance().getReference();

        addListenerOnRatingBar(parentHolder);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fdbRef = dbRef.child("Feedback");

                final ProgressDialog pd=ProgressDialog.show(getActivity(), "Sending Feedback", "Please wait...", true);

                    feedbackText = etFeedback.getText().toString();

                    ValueEventListener feedbackListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                uEmail=ds.child("userMail").getValue(String.class);

                                assert uEmail != null;
                                if (uEmail.equals(checkEmail)) {

                                    Date date=new Date();
                                    DateFormat dateFormat=android.text.format.DateFormat.getDateFormat(getActivity());

                                    String id=ds.child("feedbackId").getValue(String.class);
                                    assert id != null;
                                    fdbRef.child(id).child("feedback").setValue(feedbackText);
                                    fdbRef.child(id).child("rating").setValue(feedbackRating);
                                    fdbRef.child(id).child("date").setValue(dateFormat.format(date));

                                    Intent intent=new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("openFeedbackSubmit", true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);

                                    pd.dismiss();

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

            }
        });


        return parentHolder;

    }



     private void addListenerOnRatingBar(View view) {

         RatingBar rb=parentHolder.findViewById(R.id.ratingBar);
         tvRating=view.findViewById(R.id.tvRating);
         tvComment=view.findViewById(R.id.tvComment);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @SuppressLint("SetTextI18n")
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                if(rating < 1.0f)
                    ratingBar.setRating(1.0f);

                Double stars =(double) rating;

                NumberFormat nf = new DecimalFormat("#.####");
                String strStars = nf.format(stars);


                if(String.valueOf(rating).equals("1.0") || String.valueOf(rating).equals("1.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Ohh ! Please give feedback.");

                } else if(String.valueOf(rating).equals("2.0") || String.valueOf(rating).equals("2.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("That's poor. Please give feedback.");
                } else if(String.valueOf(rating).equals("3.0") || String.valueOf(rating).equals("3.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Not Good, Not Terrible. Please give feedback.");
                } else if(String.valueOf(rating).equals("4.0") || String.valueOf(rating).equals("4.5")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("Thank you ! Please give feedback.");
                } else if(String.valueOf(rating).equals("5.0")) {
                    tvRating.setText("Your Rating : " +strStars+" / 5");
                    tvComment.setText("We are pleased. Please give feedback and suggestions if any.");
                } else {

                    tvRating.setText("Your Rating : 1 / 5");
                    tvComment.setText("Ohh ! Please give feedback.");

                }

                tvRating.setVisibility(View.VISIBLE);
                tvComment.setVisibility(View.VISIBLE);
                btnFeedback.setVisibility(View.VISIBLE);
                etFeedback.setVisibility(View.VISIBLE);

                feedbackRating = String.valueOf(rating);

            }
        });
    }

}