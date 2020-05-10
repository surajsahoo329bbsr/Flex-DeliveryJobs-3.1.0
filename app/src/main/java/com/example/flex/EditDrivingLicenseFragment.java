package com.example.flex;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import java.util.Calendar;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class EditDrivingLicenseFragment extends Fragment {

    private Activity refActivity;
    private StorageReference mStorageReference;
    private DatabaseReference dbRef, usrRef, dlRef;
    private ProgressDialog pd;
    private final static int PICK_DL_CODE=2342;
    private int isUploadedDLFlag=0;
    private double progress;
    private String uEmail, checkEmail="", monthStr="";
    private View parentLayout;
    private DatePickerDialog datePickerDialog;
    private EditText etDLName, etDLNumber, etDOB, etAddress, etIssueDate, etExpiryDate;
    private TextView tvIDOB, tvIDate, tvIEDate;
    private Button btnSubmitLicenseDetails;

    public EditDrivingLicenseFragment()
    {

    }


    private void downloadFiles(Context context, String destDirectory, String url)
    {
        Snackbar.make(parentLayout,"Downloading.Please see notification bar...", Snackbar.LENGTH_LONG)
                .setDuration(3000)
                .setAction("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                .show();


        DownloadManager downloadManager = (DownloadManager) context
                .getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destDirectory, "Flex DrivingLicense" + ".pdf");

        assert downloadManager != null;
        downloadManager.enqueue(request);

    }

    private void download()
    {

        final ProgressDialog pd = ProgressDialog.show(refActivity,"Downloading","Please wait...",true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference reference=storageReference.child(checkEmail + "/userDL.pdf");

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String url = uri.toString();
                downloadFiles(refActivity, DIRECTORY_DOWNLOADS,url);
                pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(parentLayout,"Download failed", Snackbar.LENGTH_LONG)
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

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String dlNumber=etDLNumber.getText().toString().trim();
            String name=etDLName.getText().toString().trim();
            String address=etAddress.getText().toString().trim();
            String dob=etDOB.getText().toString().trim();
            String issueDate=etIssueDate.getText().toString().trim();
            String expiryDate=etExpiryDate.getText().toString().trim();

            btnSubmitLicenseDetails.setTextColor(Color.parseColor("#E8F8EC"));

            if (dlNumber.length() != 15) {

                etDLNumber.setError("Invalid License Number");

            } else if (name.length() == 0) {

                etDLName.setError("Please enter your name");

            } else if (dob.length() == 0) {

                etDOB.setError("Please enter your Date of Birth");

            } else if (address.length() == 0) {

                etAddress.setError("Please enter your address");

            } else if (issueDate.length() == 0) {

                etIssueDate.setError("Please enter your license's issue date");

            } else if (expiryDate.length() == 0) {

                etExpiryDate.setError("Please enter your license's expiry date");
            } else {

                etDLNumber.setError(null);
                etDLName.setError(null);
                etDOB.setError(null);
                etAddress.setError(null);
                etIssueDate.setError(null);
                etExpiryDate.setError(null);
            }

            boolean enabled=dlNumber.length() == 15 && !name.isEmpty() && !dob.isEmpty() && !address.isEmpty() && !issueDate.isEmpty() && !expiryDate.isEmpty();
            btnSubmitLicenseDetails.setEnabled(enabled);

            if (enabled)
                btnSubmitLicenseDetails.setTextColor(Color.parseColor("#FFFFFF"));
            else
                btnSubmitLicenseDetails.setTextColor(Color.parseColor("#E8F8EC"));



        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        refActivity = getActivity();
        assert refActivity != null;
        parentLayout = refActivity.findViewById(android.R.id.content);
        View parentHolder=inflater.inflate(R.layout.fragment_edit_driving_license, container, false);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        etDLNumber=parentHolder.findViewById(R.id.etDLNumber);
        etDLName=parentHolder.findViewById(R.id.etDLName);
        etDOB=parentHolder.findViewById(R.id.etDOB);
        etAddress=parentHolder.findViewById(R.id.etAddress);
        etIssueDate=parentHolder.findViewById(R.id.etIssueDate);
        etExpiryDate=parentHolder.findViewById(R.id.etExpiryDate);

        tvIDOB=parentHolder.findViewById(R.id.tvIDOB);
        tvIDate=parentHolder.findViewById(R.id.tvInvisibleIssueDate);
        tvIEDate=parentHolder.findViewById(R.id.tvInvisibleExpiryDate);

        ImageView ivUploadDL=parentHolder.findViewById(R.id.ivUploadDL);
        ImageView ivDownloadDL=parentHolder.findViewById(R.id.ivDownloadDL);

        btnSubmitLicenseDetails=parentHolder.findViewById(R.id.btnSubmit);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkEmail = user.getEmail();
        dbRef = FirebaseDatabase.getInstance().getReference();
        usrRef = dbRef.child("User");
        dlRef=dbRef.child("DrivingLicense");

        final ProgressDialog pd=ProgressDialog.show(getActivity(), "Fetching data", "Hang on...", true);

        ValueEventListener dlListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    uEmail=ds.child("userMail").getValue(String.class);

                    assert uEmail != null;
                    if (uEmail.equals(checkEmail)) {

                        pd.dismiss();

                        String id=ds.child("licenseId").getValue(String.class);

                        assert id != null;
                        String dlNumber=ds.child("licenseNumber").getValue(String.class);
                        String name=ds.child("userName").getValue(String.class);
                        String dob=ds.child("userDOB").getValue(String.class);
                        String address=ds.child("userAddress").getValue(String.class);
                        String issueDate=ds.child("licenseIssueDate").getValue(String.class);
                        String expiryDate=ds.child("licenseExpiryDate").getValue(String.class);

                        etDLNumber.setText(dlNumber);
                        etDLName.setText(name);
                        etDOB.setText(dob);
                        etAddress.setText(address);
                        etIssueDate.setText(issueDate);
                        etExpiryDate.setText(expiryDate);

                        etDLNumber.addTextChangedListener(textWatcher);
                        etDLName.addTextChangedListener(textWatcher);
                        etDOB.addTextChangedListener(textWatcher);
                        etAddress.addTextChangedListener(textWatcher);
                        etIssueDate.addTextChangedListener(textWatcher);
                        etExpiryDate.addTextChangedListener(textWatcher);

                        break;


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
                Snackbar.make(parentLayout, "Try again", Snackbar.LENGTH_LONG)
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

        ivUploadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrivingLicense();
            }

        });

        ivDownloadDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbRef= FirebaseDatabase.getInstance().getReference();
                usrRef=dbRef.child("User");

                ValueEventListener userListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            uEmail=ds.child("userMail").getValue(String.class);

                            assert uEmail != null;
                            if (uEmail.equals(checkEmail)) {
                                isUploadedDLFlag = ds.child("userDLFlag").getValue(Integer.class);

                                if(isUploadedDLFlag == 1)
                                    download();
                                else {
                                    Snackbar.make(parentLayout,"Please upload your Driving License first ", Snackbar.LENGTH_LONG)
                                            .setDuration(3000)
                                            .setAction("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                            .show();
                                }

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

            }
        });

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(refActivity, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        if(month == 0)
                            monthStr = "Jan";
                        else if(month == 1)
                            monthStr = "Feb";
                        else if(month == 2)
                            monthStr="Mar";
                        else if(month == 3)
                            monthStr = "Apr";
                        else if(month == 4)
                            monthStr ="May";
                        else if(month == 5)
                            monthStr="June";
                        else if(month == 6)
                            monthStr="Jul";
                        else if(month == 7)
                            monthStr = "Aug";
                        else if(month == 8)
                            monthStr = "Sep";
                        else if(month == 9)
                            monthStr = "Oct";
                        else if(month == 10)
                            monthStr = "Nov";
                        else
                            monthStr = "Dec";

                        etDOB.setText(dayOfMonth+"-"+monthStr+"-"+year);
                        tvIDOB.setText(dayOfMonth+"/"+month+"/"+year);
                        etDOB.setError(null);

                    }
                }, year,month,date);

                //sets the date picker 18 years back
                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis()-6574* 24 * 60 * 60 * 1000L));

                datePickerDialog.show();

            }
        });

        etIssueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(refActivity, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        if(month == 0)
                            monthStr = "Jan";
                        else if(month == 1)
                            monthStr = "Feb";
                        else if(month == 2)
                            monthStr="Mar";
                        else if(month == 3)
                            monthStr = "Apr";
                        else if(month == 4)
                            monthStr ="May";
                        else if(month == 5)
                            monthStr="June";
                        else if(month == 6)
                            monthStr="Jul";
                        else if(month == 7)
                            monthStr = "Aug";
                        else if(month == 8)
                            monthStr = "Sep";
                        else if(month == 9)
                            monthStr = "Oct";
                        else if(month == 10)
                            monthStr = "Nov";
                        else
                            monthStr = "Dec";

                        etIssueDate.setText(dayOfMonth+"-"+monthStr+"-"+year);
                        tvIDate.setText(dayOfMonth+"/"+month+"/"+year);
                        etIssueDate.setError(null);

                    }
                }, year,month,date);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });

        etExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(refActivity, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        if(month == 0)
                            monthStr = "Jan";
                        else if(month == 1)
                            monthStr = "Feb";
                        else if(month == 2)
                            monthStr="Mar";
                        else if(month == 3)
                            monthStr = "Apr";
                        else if(month == 4)
                            monthStr ="May";
                        else if(month == 5)
                            monthStr="June";
                        else if(month == 6)
                            monthStr="Jul";
                        else if(month == 7)
                            monthStr = "Aug";
                        else if(month == 8)
                            monthStr = "Sep";
                        else if(month == 9)
                            monthStr = "Oct";
                        else if(month == 10)
                            monthStr = "Nov";
                        else
                            monthStr = "Dec";

                        etExpiryDate.setText(dayOfMonth+"-"+monthStr+"-"+year);
                        tvIEDate.setText(dayOfMonth+"/"+month+"/"+year);
                        etExpiryDate.setError(null);

                    }
                }, year,month,date);

                datePickerDialog.getDatePicker().setMinDate((System.currentTimeMillis() + 24 * 60 * 60 * 1000L) - 1000);

                datePickerDialog.show();

            }
        });


        btnSubmitLicenseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String dlNumber=etDLNumber.getText().toString().trim();
                final String name=etDLName.getText().toString().trim();
                final String address=etAddress.getText().toString().trim();
                final String dob=etDOB.getText().toString().trim();
                final String issueDate=etIssueDate.getText().toString().trim();
                final String expiryDate=etExpiryDate.getText().toString().trim();

                final ProgressDialog pd=ProgressDialog.show(refActivity, "", "Please wait...", true);

                dlRef=dbRef.child("DrivingLicense");

                ValueEventListener dlListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            uEmail=ds.child("userMail").getValue(String.class);

                            assert uEmail != null;
                            if (uEmail.equals(checkEmail)) {

                                pd.dismiss();

                                String id=ds.child("licenseId").getValue(String.class);

                                assert id != null;
                                dlRef.child(id).child("licenseNumber").setValue(dlNumber);
                                dlRef.child(id).child("userName").setValue(name);
                                dlRef.child(id).child("userDOB").setValue(dob);
                                dlRef.child(id).child("userAddress").setValue(address);
                                dlRef.child(id).child("licenseIssueDate").setValue(issueDate);
                                dlRef.child(id).child("licenseExpiryDate").setValue(expiryDate);

                                Intent intent=new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("openDLSubmit", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);

                                break;


                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        pd.dismiss();
                        Snackbar.make(parentLayout, "Try again", Snackbar.LENGTH_LONG)
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


            }

        });

        return parentHolder;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file

        if (requestCode == PICK_DL_CODE && resultCode == MainActivity.RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            } else {

                Toast.makeText(refActivity, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void getDrivingLicense() {
        //for greater than lollipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(refActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + refActivity.getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose your file  "), PICK_DL_CODE);
    }

    private void uploadFile(Uri data) {

        pd = ProgressDialog.show(refActivity,"Uploading","Please wait...",true);
        StorageReference sRef = mStorageReference.child(checkEmail+"/userDL.pdf");

        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        pd.dismiss();
                        Snackbar.make(parentLayout,"File uploaded successfully", Snackbar.LENGTH_LONG)
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

                                    uEmail=ds.child("userMail").getValue(String.class);
                                    assert uEmail != null;
                                    if (uEmail.equals(checkEmail)) {

                                        String id = ds.child("userId").getValue(String.class);
                                        assert id != null;
                                        usrRef.child(id).child("userDLFlag").setValue(1);
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

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        pd.dismiss();
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
                        progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded "+ (int)progress+"%");

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}