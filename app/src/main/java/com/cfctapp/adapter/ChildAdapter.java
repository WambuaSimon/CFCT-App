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

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> {

    private List<ChildModel> childModel;
    Context context;
    public ChildAdapterListener onClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView country;
        ImageButton remove;

        CardView card;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.age = itemView.findViewById(R.id.age);
            this.country = itemView.findViewById(R.id.country);
            this.remove = itemView.findViewById(R.id.remove);

            this.card = itemView.findViewById(R.id.card);


        }
    }

    public ChildAdapter(List<ChildModel> data, Context context, ChildAdapterListener listener) {
        this.childModel = data;
        this.context = context;
        this.onClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final TextView name = holder.name;
        TextView age = holder.age;
        TextView country = holder.country;
        ImageButton remove = holder.remove;
        CardView card = holder.card;


        age.setText("Age: " + childModel.get(listPosition).getAge());
        name.setText(childModel.get(listPosition).getName());
        country.setText(childModel.get(listPosition).getCountry());


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.childOnClick(v, listPosition);

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return childModel.size();
    }

    public interface ChildAdapterListener {

        void childOnClick(View v, int position);
        void onRemoveChild(View v,int position);

    }

}
