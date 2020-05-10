package com.example.flex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    LinearLayout loginLayout;
    private EditText etEmail, etPassword;
    private FirebaseAuth auth;
    private View parentLayout;
    private String email, password;
    private Button btnLogin;
    private boolean notVerified;

    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String usernameInput=etEmail.getText().toString().trim();
            String passwordInput=etPassword.getText().toString().trim();

            if (usernameInput.length() == 0)
                etEmail.setError("Please enter you email");
            else if (passwordInput.length() < 8)
                etPassword.setError("Password must have at least 8 characters");

            btnLogin.setEnabled(!usernameInput.isEmpty() && passwordInput.length() >= 8);

            if (!usernameInput.isEmpty() && passwordInput.length() >= 8)
                btnLogin.setTextColor(Color.parseColor("#FFFFFF"));
            else
                btnLogin.setTextColor(Color.parseColor("#E8F8EC"));

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_round)
                .setTitle("Closing App")
                .setMessage("Do you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent it=new Intent(Intent.ACTION_MAIN);
                        it.addCategory(Intent.CATEGORY_HOME);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);

                    }
                })
                .setNegativeButton("No", null).show();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, android.R.color.background_light));// set status background white
        }

        setContentView(R.layout.activity_login);

        etEmail=findViewById(R.id.etEmailLogin);
        etPassword=findViewById(R.id.etPasswordLogin);
        btnLogin=findViewById(R.id.btnProfile);
        TextView tvForgotPassword=findViewById(R.id.tvForgotPassword);
        TextView tvRegister=findViewById(R.id.tvRegister);
        parentLayout = findViewById(android.R.id.content);
        loginLayout=findViewById(R.id.login_layout);

        auth = FirebaseAuth.getInstance();

        if(DeleteAccountActivity.deleteAccountFlag == 1)
        {
            Snackbar.make(parentLayout,"Account deleted", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            DeleteAccountActivity.deleteAccountFlag=0;

        }

        if(RegisterActivity.registerFlag == 1)
        {
            Snackbar.make(parentLayout, "Account created. Please verify your email.", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            RegisterActivity.registerFlag = 0;

        }

        if (ForgotPasswordActivity.sentMailFlag == 1)
        {
            Snackbar.make(parentLayout,"Please check your email", Snackbar.LENGTH_LONG)
                    .setDuration(3000)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.background_light))
                    .show();

            ForgotPasswordActivity.sentMailFlag=0;

        }


        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=etEmail.getText().toString().trim().replace(" +", "");
                password=etPassword.getText().toString().trim();

                final ProgressDialog pd=ProgressDialog.show(LoginActivity.this, "Verifying Credentials", "Please wait...", true);

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api=Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {

                                    pd.dismiss();
                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail) {
                                        Snackbar.make(parentLayout, "Not a registered user", Snackbar.LENGTH_LONG)
                                                .setDuration(3000)
                                                .setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                .show();

                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                        Snackbar.make(parentLayout, "Invalid Credentials", Snackbar.LENGTH_LONG)
                                                .setDuration(3000)
                                                .setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                .show();

                                        // TODO: Take your action
                                    } catch (Exception e) {

                                        Snackbar.make(parentLayout, "Try Again ", Snackbar.LENGTH_LONG)
                                                .setDuration(3000)
                                                .setAction("Close", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                                .show();
                                    }

                                } else {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    assert user != null;
                                    if(user.isEmailVerified()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("openBookingStackNull", true);
                                        overridePendingTransition(0, 0);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                    }
                                    else{
                                        pd.dismiss();
                                        Snackbar.make(parentLayout, "Email not verified. Please check your email.", Snackbar.LENGTH_LONG)
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
                        });



                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(loginLayout.getWindowToken(), 0);

            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
                finish();

            }
        });

    }


}