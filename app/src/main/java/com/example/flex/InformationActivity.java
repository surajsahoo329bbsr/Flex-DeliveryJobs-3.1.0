package com.example.flex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class InformationActivity extends AppCompatActivity {


    @SuppressLint("SetTextI18n")
    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(InformationActivity.this, android.R.color.background_light));// set status background white
        }


        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable()); // Add Color.Parse("#000") inside ColorDrawable() for color change
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("PrivateResource") final Drawable upArrow=getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(android.R.color.background_dark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Spannable text=new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        Intent it = getIntent();

        final String msg1 = it.getStringExtra("flipkart");
        final String msg2 = it.getStringExtra("fedex");
        final String msg3 = it.getStringExtra("aramex");
        final String msg4 = it.getStringExtra("delhivery");
        final String msg5 = it.getStringExtra("bluedart");
        final String msg6 = it.getStringExtra("dtdc");
        final String msg7 = it.getStringExtra("ipost");

        TextView tv=findViewById(R.id.tvCourier);
        ImageView iv=findViewById(R.id.ivImage);
        TextView address=findViewById(R.id.tvAddress);
        TextView phone=findViewById(R.id.tvPhoneCourier);
        TextView officeHours=findViewById(R.id.tvOfficeHours);

        ImageView ivCall=findViewById(R.id.ivCall);
        ImageView ivDirections=findViewById(R.id.ivDirections);
        ImageView ivWeb=findViewById(R.id.ivWeb);


        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert msg1 != null;
                if(msg1.equals("1")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+919090011700"));
                    startActivity(callIntent);
                }

                assert msg2 != null;
                if(msg2.equals("2")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+917752017762"));
                    startActivity(callIntent);
                }

                assert msg3 != null;
                if(msg3.equals("3")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+919337851816"));
                    startActivity(callIntent);
                }

                assert msg4 != null;
                if(msg4.equals("4")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+917790018067"));
                    startActivity(callIntent);
                }

                assert msg5 != null;
                if(msg5.equals("5")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+917759608100"));
                    startActivity(callIntent);
                }

                assert msg6 != null;
                if(msg6.equals("6")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:06743043518"));
                    startActivity(callIntent);
                }

                assert msg7 != null;
                if(msg7.equals("7")) {

                    Intent callIntent=new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:+06742530137"));
                    startActivity(callIntent);
                }

            }
        });

        ivDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert msg1 != null;
                if(msg1.equals("1")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/ekart+bhubaneswar/@20.3086795,85.7579884,12z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg2 != null;
                if(msg2.equals("2")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/place/Fedex+Express/@20.2660291,85.8294654,15z/data=!4m19!1m13!4m12!1m4!2m2!1d85.8467041!2d20.2573183!4e1!1m6!1m2!1s0x3a19a750ffffffb9:0x1eb57fa169c70acd!2sfedex+website+bhubaneswar!2m2!1d85.8426803!2d20.2704502!3m4!1s0x3a19a750ffffffb9:0x1eb57fa169c70acd!8m2!3d20.2704502!4d85.8426803";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg3 != null;
                if(msg3.equals("3")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/aramex+bhubaneswar/@20.3370506,85.7931465,12z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg4 != null;
                if(msg4.equals("4")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/delhivery+bhubaneswar/@20.3067283,85.8105208,14z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg5 != null;
                if(msg5.equals("5")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/bluedart+bhubaneswar/@20.3069147,85.7579895,12z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg6 != null;
                if(msg6.equals("6")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/dtdc+bhubaneswar/@20.3083854,85.7579886,12z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }

                assert msg7 != null;
                if(msg7.equals("7")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            String uri="https://www.google.com/maps/search/indain+post+bhubaneswar/@20.3072089,85.7579893,12z/data=!3m1!4b1";
                            Intent it=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(it);

                        }
                    }, 1000);
                }
            }
        });

        ivWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert msg1 != null;
                if(msg1.equals("1")) {

                    Uri uri=Uri.parse("https://www.ekartlogistics.com/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                assert msg2 != null;
                if(msg2.equals("2")) {

                    Uri uri=Uri.parse("https://www.fedex.com/en-in/home.html");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);


                    startActivity(intent);
                }

                assert msg3 != null;
                if(msg3.equals("3")) {

                    Uri uri=Uri.parse("https://www.aramex.com/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                assert msg4 != null;
                if(msg4.equals("4")) {

                    Uri uri=Uri.parse("https://www.delhivery.com/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                assert msg5 != null;
                if(msg5.equals("5")) {

                    Uri uri=Uri.parse("https://www.bluedart.com/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                assert msg6 != null;
                if(msg6.equals("6")) {

                    Uri uri=Uri.parse("http://www.dtdc.in/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                assert msg7 != null;
                if(msg7.equals("7")) {

                    Uri uri=Uri.parse("https://www.indiapost.gov.in/");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });


        assert msg1 != null;
        if(msg1.equals("1")) {

            tv.setText("Ekart Logistics");
            iv.setImageResource(R.drawable.ic_flipkart);
            address.setText("Soubhagya Nagar, Baramunda, Bhubaneswar, Odisha 751003");
            phone.setText("09090011700");
            officeHours.setText("8am-10pm. Sundays closed.");

        }

        assert msg2 != null;
        if(msg2.equals("2")){

            tv.setText("Fedex");
            iv.setImageResource(R.drawable.ic_fedex);
            address.setText("11, Master Canteen Chowk, Master Canteen Area, Kharabela Nagar, Bhubaneswar, Odisha 751001");
            phone.setText("07752017762");
            officeHours.setText("10am-7pm. Sundays closed.");

        }

        assert msg3 != null;
        if(msg3.equals("3")){

            tv.setText("Aramex");
            iv.setImageResource(R.drawable.ic_aramex);
            address.setText("Jayadev Vihar, Bhubaneswar, Odisha 751002");
            phone.setText("09337851816");
            officeHours.setText("10am-6pm. Sundays closed.");

        }

        assert msg4 != null;
        if(msg4.equals("4")){

            tv.setText("Delhivery");
            iv.setImageResource(R.drawable.ic_delhivery);
            address.setText("N2-B2, CRP Ekamra Kanana Rd, N2, Block N2, IRC Village, Nayapalli, Bhubaneswar, Odisha 751015");
            phone.setText("07790018067");
            officeHours.setText("8am-6pm. Sundays closed.");

        }

        assert msg5 != null;
        if(msg5.equals("5")){

            tv.setText("Blue Dart");
            iv.setImageResource(R.drawable.ic_bluedart);
            address.setText("A/69, Railway Yard and Washing Line, Kharabela Nagar, Bhubaneswar, Odisha 751001");
            phone.setText("07759608100");
            officeHours.setText("10am-9pm. Sundays closed.");

        }

        assert msg6 != null;
        if(msg6.equals("6")){

            tv.setText("DTDC");
            iv.setImageResource(R.drawable.ic_dtdc);
            address.setText("Master Canteen Area, Kharabela Nagar, Bhubaneswar, Odisha 751001");
            phone.setText("0674-3043518");
            officeHours.setText("9am-7pm. Sundays closed.");

        }

        assert msg7 != null;
        if(msg7.equals("7")){

            tv.setText("Indian Post");
            iv.setImageResource(R.drawable.ic_indianpost);
            address.setText("107, Postal Road, Bapuji Nagar, Bhubaneswar, Odisha 751001");
            phone.setText("0674-2530137");
            officeHours.setText("10am-5pm. Sundays closed.");

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent=new Intent(InformationActivity.this, MainActivity.class);
        intent.putExtra("openBookingStackNull", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(InformationActivity.this, MainActivity.class);
        intent.putExtra("openBookingStackNull", true);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}