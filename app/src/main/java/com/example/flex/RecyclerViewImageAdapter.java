package com.example.flex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageUrls;
    private Context mContext;

    RecyclerViewImageAdapter(Context context, ArrayList<String> imageUrls)
    {
        mImageUrls = imageUrls;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG,"onCreateViewHolder: called.");

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_company, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Log.d(TAG,"onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(mContext,InformationActivity.class);

                it.putExtra("flipkart","0");
                it.putExtra("fedex","0");
                it.putExtra("aramex","0");
                it.putExtra("delhivery","0");
                it.putExtra("bluedart","0");
                it.putExtra("dtdc","0");
                it.putExtra("ipost","0");

                int position  = holder.getAdapterPosition();

                switch (position){

                    case 0:
                        it.putExtra("flipkart","1");
                        break;

                    case 1:
                        it.putExtra("fedex","2");
                        break;

                    case 2:
                        it.putExtra("aramex","3");
                        break;

                    case 3:
                        it.putExtra("delhivery","4");
                        break;

                    case 4:
                        it.putExtra("bluedart","5");
                        break;

                    case 5:
                        it.putExtra("dtdc","6");
                        break;

                    case 6:
                        it.putExtra("ipost","7");
                        break;

                }

                mContext.startActivity(it);

            }
        });

    }

    @Override
    public int getItemCount() {

        return mImageUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageRecycler);

        }


    }
}