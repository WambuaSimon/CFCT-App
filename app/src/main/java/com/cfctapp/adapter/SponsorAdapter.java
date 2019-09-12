package com.cfctapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        TextView assign_child;
        ImageButton remove;
        CardView card;
        ImageButton edit;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.contribution = itemView.findViewById(R.id.contribution);
            this.country = itemView.findViewById(R.id.country);
            this.remove = itemView.findViewById(R.id.remove);
            this.edit = itemView.findViewById(R.id.edit);
            this.card = itemView.findViewById(R.id.card);
            this.assign_child = itemView.findViewById(R.id.assign_child);


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
        TextView assign_child = holder.assign_child;
        ImageButton remove = holder.remove;
        ImageButton edit = holder.edit;
        CardView card = holder.card;


//        age.setText("Age: "+sponsorModel.get(listPosition).getAge());
        name.setText("Name: "+sponsorModel.get(listPosition).getName());
        country.setText("Country: "+sponsorModel.get(listPosition).getCountry());
        contribution.setText(sponsorModel.get(listPosition).getMonthlyAmount());


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.sponsorOnEdit(v, listPosition);

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onRemoveSponsor(view, listPosition);
            }
        });
        assign_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.assignChild(view,listPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sponsorModel.size();
    }

    public interface SponsorAdapterListener {

        void sponsorOnEdit(View v, int position);
        void assignChild(View v, int position);


        void onRemoveSponsor(View v, int position);
    }

}
