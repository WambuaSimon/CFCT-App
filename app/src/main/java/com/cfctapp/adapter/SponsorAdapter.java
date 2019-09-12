package com.cfctapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.cfctapp.R;
import com.cfctapp.models.SponsorModel;

import java.util.List;

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.MyViewHolder> {

    private List<SponsorModel> sponsorModel;
    Context context;
    public SponsorAdapterListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView contribution;
        TextView country;

        CardView card;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.contribution = itemView.findViewById(R.id.contribution);
            this.country = itemView.findViewById(R.id.country);

            this.card = itemView.findViewById(R.id.card);


        }
    }

    public SponsorAdapter(List<SponsorModel> data, Context context, SponsorAdapterListener listener) {
        this.sponsorModel = data;
        this.context = context;
        this.onClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sponsor_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TextView name = holder.name;
        TextView contribution = holder.contribution;
        TextView country = holder.country;

        CardView card = holder.card;


//        age.setText("Age: "+sponsorModel.get(listPosition).getAge());
        name.setText(sponsorModel.get(listPosition).getName());
        country.setText(sponsorModel.get(listPosition).getCountry());
        contribution.setText(sponsorModel.get(listPosition).getMonthlyAmount());


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.sponsorOnClick(v, listPosition);

            }
        });


    }

    @Override
    public int getItemCount() {
        return sponsorModel.size();
    }

    public interface SponsorAdapterListener {

        void sponsorOnClick(View v, int position);

    }

}
