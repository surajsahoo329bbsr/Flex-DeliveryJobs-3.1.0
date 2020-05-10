package com.example.flex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Activity refActivity;
    private View parentHolder;
    private ImageView viewPhoto;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog pd;
    private static final int PICK_FROM_GALLERY=1;
    private final int PICK_IMAGE_REQUEST=71;
    private Uri filePath;
    private int uploadImageFlag=0, isUploadedPhotoFlag=0;
    private double progress;
    private String uEmail, getImageUrl, checkEmail="", uPhone="", uName="";
    private TextView textViewName, textViewPh, textViewMail, tvTotalHours, tvTotalEarnings, tvTotalShifts;
    private View parentLayout;
    private int totalShifts, totalHours;
    private double totalEarnings;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private void chooseImage()
    {
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it,"Select Picture "),PICK_IMAGE_REQUEST);
    }

    private void setImage() {

        final ProgressDialog pd = ProgressDialog.show(refActivity,"Loading photo","Please wait...",true);

        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child(checkEmail+"/photo.jpg");
        final long TEN_MEGABYTES = 10024 * 10024;

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                getImageUrl = uri.toString();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(parentLayout,"Failed to load image URL", Snackbar.LENGTH_LONG)
                        .setDuration(3000)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                        .show();

                pd.dismiss();


            }
        });

        imgRef.getBytes(TEN_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Glide.with(ProfileFragment.this)
                        .load(getImageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(viewPhoto);

                pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(parentLayout,"Failed to load photo", Snackbar.LENGTH_LONG)
                        .setDuration(3000)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                        .show();

                pd.dismiss();

            }
        });

        pd.dismiss();
    }

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        refActivity = getActivity();
        assert refActivity != null;
        parentLayout = refActivity.findViewById(android.R.id.content);
        parentHolder = inflater.inflate(R.layout.fragment_profile, container ,false);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        viewPhoto=parentHolder.findViewById(R.id.imageView);

        ImageView ivEditProfile=parentHolder.findViewById(R.id.ivEditProfile);
        ImageView ivChoosePhoto=parentHolder.findViewById(R.id.ivChoosePhoto);
        ImageView ivDeleteAccount=parentHolder.findViewById(R.id.ivDeleteAccount);
        ImageView ivUploadPhoto=parentHolder.findViewById(R.id.ivUploadPhoto);

        final ProgressDialog pd = ProgressDialog.show(refActivity,"Loading account","Please wait...",true);

        if (EditFragment.updateFlag == 1) {
            pd.dismiss();

            Snackbar.make(parentLayout,"Profile updated", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

        }

        EditFragment.updateFlag=0;

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail = user.getEmail();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
        DatabaseReference usrRef=dbRef.child("User");
        DatabaseReference histRef=dbRef.child("History");

        ValueEventListener userListener = new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail = ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if(uEmail.equals(checkEmail)) {
                        uPhone = ds.child("userPhone").getValue(String.class);
                        uName = ds.child("userName").getValue(String.class);
                        isUploadedPhotoFlag=ds.child("userPhotoFlag").getValue(Integer.class);
                        if(isUploadedPhotoFlag == 1)
                            setImage();
                        pd.dismiss();
                        break;
                    }

                }


                textViewName=parentHolder.findViewById(R.id.tvName);
                textViewMail=parentHolder.findViewById(R.id.tvEmail);
                textViewPh=parentHolder.findViewById(R.id.tvPhone);

                textViewName.setText(uName);
                textViewPh.setText("+91-" + uPhone);
                textViewMail.setText(uEmail);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
                Toast.makeText(refActivity, databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        };

        usrRef.addListenerForSingleValueEvent(userListener);

        totalShifts = 0;
        totalEarnings = 0;
        totalHours = 0;

        ValueEventListener histListener = new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail = ds.child("userMail").getValue(String.class);
                    assert uEmail != null;
                    if(uEmail.equals(checkEmail)) {
                        totalHours += Integer.parseInt(Objects.requireNonNull(ds.child("showWorkHours").getValue(String.class)).substring(0,1));
                        if(!Objects.requireNonNull(ds.child("transactionMoney").getValue(String.class)).equals(""))
                            totalEarnings += Double.parseDouble(Objects.requireNonNull(ds.child("transactionMoney").getValue(String.class)));
                        totalShifts ++;
                    }

                }

                pd.dismiss();

                tvTotalHours = parentHolder.findViewById(R.id.tvTotalHours);
                tvTotalShifts = parentHolder.findViewById(R.id.tvTotalShifts);
                tvTotalEarnings = parentHolder.findViewById(R.id.tvTotalEarnings);

                tvTotalHours.setText("Total Hours Worked : "+ totalHours);
                tvTotalShifts.setText("Total Shifts Worked : " + totalShifts);
                tvTotalEarnings.setText("Total Earnings : \u20B9 "+ totalEarnings);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
                Toast.makeText(refActivity, databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        };

        histRef.addListenerForSingleValueEvent(histListener);


        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(refActivity,EditProfileActivity.class));

            }
        });

        viewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isUploadedPhotoFlag == 1) {

                    Intent anotherIntent=new Intent(refActivity, ViewPhotoActivity.class);
                    startActivity(anotherIntent);
                }

            }
        });

        ivChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });


        ivUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uploadImageFlag == 1)
                    uploadImage();
                else {
                    Snackbar.make(parentLayout,"Please choose a photo before uploading", Snackbar.LENGTH_LONG)
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
        });

        ivDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(refActivity,DeleteAccountActivity.class));

            }
        });


        return parentHolder;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == MainActivity.RESULT_OK && data !=null && data.getData() != null)
        {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(refActivity.getContentResolver(),filePath);
                viewPhoto.setImageBitmap(bitmap);
                uploadImageFlag = 1;
           }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
            uploadImageFlag = 0;


    }


    private void uploadImage() {


        if(filePath != null) {

            pd = ProgressDialog.show(refActivity, "Uploading", "Please wait...", true);

            StorageReference sRef = mStorageReference.child(checkEmail).child("photo.jpg");
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @RequiresApi(api=Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            pd.dismiss();
                            Upload upload = new Upload();
                            mDatabaseReference.child(Objects.requireNonNull(mDatabaseReference.push().getKey())).setValue(upload);
                            Snackbar.make(parentLayout,"Photo uploaded", Snackbar.LENGTH_LONG)
                                    .setDuration(3000)
                                    .setAction("Close", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                    .show();
                            final DatabaseReference usrRef= FirebaseDatabase.getInstance().getReference().child("User");

                            ValueEventListener userListener = new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        uEmail = ds.child("userMail").getValue(String.class);

                                        assert uEmail != null;
                                        if (uEmail.equals(checkEmail)) {

                                            String id = ds.child("userId").getValue(String.class);
                                            assert id != null;
                                            usrRef.child(id).child("userPhotoFlag").setValue(1);
                                            break;
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Toast.makeText(refActivity, databaseError.getCode(),Toast.LENGTH_LONG).show();

                                }
                            };

                            usrRef.addListenerForSingleValueEvent(userListener);

                            isUploadedPhotoFlag = 1;

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            pd.dismiss();
                            Toast.makeText(refActivity.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            Snackbar.make(parentLayout,"Upload failed", Snackbar.LENGTH_LONG)
                                    .setDuration(3000)
                                    .setAction("Close", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                    .show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progress=100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            pd.setMessage("Uploaded "+ (int)progress+"%");

                        }
                    });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PICK_FROM_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            } else {

                Snackbar.make(parentLayout,"This app requires permission to upload your photos", Snackbar.LENGTH_LONG)
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

    }

}