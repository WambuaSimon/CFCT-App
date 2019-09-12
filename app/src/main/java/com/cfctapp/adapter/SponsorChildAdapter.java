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
import com.cfctapp.models.ChildModel;

import java.util.List;
import java.util.Locale;

public class SponsorChildAdapter extends RecyclerView.Adapter<SponsorChildAdapter.MyViewHolder> {

    private List<ChildModel> childModel;
    Context context;
    public SponsorChildAdapterListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView country;
        TextView hobby;
        CardView card;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.age = itemView.findViewById(R.id.age);
            this.country = itemView.findViewById(R.id.country);

            this.hobby = itemView.findViewById(R.id.hobby);

            this.card = itemView.findViewById(R.id.card);


        }
    }

    public SponsorChildAdapter(List<ChildModel> data, Context context, SponsorChildAdapterListener listener) {
        this.childModel = data;
        this.context = context;
        this.onClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sponsor_child_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TextView name = holder.name;
        TextView age = holder.age;
        TextView country = holder.country;
        TextView hobby = holder.hobby;

        CardView card = holder.card;


        age.setText("Age: " + childModel.get(listPosition).getAge());
        name.setText("Name: "+childModel.get(listPosition).getName());
        country.setText("Country: "+childModel.get(listPosition).getCountry());
        hobby.setText("Hobby: "+childModel.get(listPosition).getHobby());



        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.sponsorChild(view, listPosition);
            }
        });


    }

    @Override
    public int getItemCount() {
        return childModel.size();
    }

    public interface SponsorChildAdapterListener {



        void sponsorChild(View v, int position);

    }

}
