package com.example.flex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class BookingConfirmedActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(BookingConfirmedActivity.this, android.R.color.background_light));// set status background white
        }


        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        TextView tvCompany=findViewById(R.id.tvCompany);
        TextView tvTimings=findViewById(R.id.tvTimings);
        TextView tvAddress=findViewById(R.id.tvAddress);
        ImageView ivCompany=findViewById(R.id.ivImage);

        Intent it = getIntent();

        final int company=it.getIntExtra("company", 1);
        final String date = it.getStringExtra("date");
        final String time = it.getStringExtra("time");
        final String hours = it.getStringExtra("hours");

        assert date != null;
        char[] dateArr=date.toCharArray();
        char[] modDateArr=new char[date.length()];
        int count = 0;

        for(int i =0; i< dateArr.length; i++) {
            if(dateArr[i] == '-')
                count ++;
            if(count == 2)
                break;

            modDateArr[i] = dateArr[i];
        }

        final String modTimeStr = String.valueOf(modDateArr)+", "+time+" | "+hours;
        tvTimings.setText(modTimeStr);

        if (company == 1) {
            tvCompany.setText("Ekart Logistics");
            tvAddress.setText("Baramunda, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_flipkart);
        } else if (company == 2) {
            tvCompany.setText("Fedex");
            tvAddress.setText("Master Canteen, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_fedex);
        } else if(company == 3) {
            tvCompany.setText("Aramex");
            tvAddress.setText("Jayadev Vihar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_aramex);
        } else if(company == 4) {
            tvCompany.setText("Delhivery");
            tvAddress.setText("Nayapalli, Bhubaneswar");
            Glide.with(BookingConfirmedActivity.this)
                    .load("https://nexusvp.com/wp-content/uploads/2014/04/oie_JbUn8ia6Q3Zq.png")
                    .into(ivCompany);
        } else if(company == 5) {
            tvCompany.setText("Blue Dart");
            tvAddress.setText("Kharabela Nagar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_bluedart);
        } else if(company == 6) {
            tvCompany.setText("DTDC");
            tvAddress.setText("Master Canteen Area, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_dtdc);
        } else if (company == 7) {
            tvCompany.setText("Indian Post");
            tvAddress.setText("Bapuji Nagar, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_indianpost);
        } else {
            tvCompany.setText("Ekart Logistics");
            tvAddress.setText("Baramunda, Bhubaneswar");
            ivCompany.setImageResource(R.drawable.ic_flipkart);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(BookingConfirmedActivity.this, MainActivity.class);
        intent.putExtra("openBookingStackNull", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(BookingConfirmedActivity.this, MainActivity.class);
        intent.putExtra("openBookingStackNull", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

}
