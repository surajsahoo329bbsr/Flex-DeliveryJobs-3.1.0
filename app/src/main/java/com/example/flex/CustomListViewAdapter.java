package com.example.flex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Company> {

    private Context context;

    CustomListViewAdapter(Context context, int resourceId,
                          List<Company> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Company company =  getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.list_slot, null);
            holder = new ViewHolder();
            holder.txtCompany = convertView.findViewById(R.id.tvCompany);
            holder.txtTimings = convertView.findViewById(R.id.tvTimings);
            holder.txtAddress = convertView.findViewById(R.id.tvAddress);
            holder.imageView = convertView.findViewById(R.id.ivImage);
            holder.txtPaymentStatus = convertView.findViewById(R.id.tvPaymentStatus);
            holder.ivPaymentStatus = convertView.findViewById(R.id.imageViewPayment);
            holder.tvTransactionDateTime = convertView.findViewById(R.id.tvTransactionDate);
            holder.tvTransactionMoney = convertView.findViewById(R.id.tvTransactionMoney);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        assert company != null;
        holder.txtCompany.setText(company.getCompany());
        holder.txtTimings.setText(company.getTimings());
        holder.txtAddress.setText(company.getAddress());
        holder.imageView.setImageResource(company.getImageId());
        holder.txtPaymentStatus.setText(company.getPaymentStatus());
        if (company.getPaymentStatus().equals("Payment Received"))
            holder.ivPaymentStatus.setImageResource(R.drawable.ic_confirmed_tick);
        holder.tvTransactionDateTime.setText(company.getTransactionDate());
        holder.tvTransactionMoney.setText("\u20B9 "+company.getTransactionMoney());

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtCompany;
        TextView txtTimings;
        TextView txtAddress;
        TextView txtPaymentStatus;
        ImageView ivPaymentStatus;
        TextView tvTransactionDateTime;
        TextView tvTransactionMoney;
    }
}