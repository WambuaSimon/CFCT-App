package com.cfctapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cfctapp.R;
import com.cfctapp.models.ChildModel;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> implements Filterable {

    private List<ChildModel> childModel;
    private List<ChildModel> filteredChildModel;
    Context context;
    public ChildAdapterListener onClickListener;




    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView country;
        ImageButton remove;
        ImageButton edit;
        TextView hobby;
        TextView sponsor;
        CardView card;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.age = itemView.findViewById(R.id.age);
            this.country = itemView.findViewById(R.id.country);
            this.remove = itemView.findViewById(R.id.remove);
            this.edit = itemView.findViewById(R.id.edit);
            this.hobby = itemView.findViewById(R.id.hobby);
            this.sponsor = itemView.findViewById(R.id.sponsor);

            this.card = itemView.findViewById(R.id.card);


        }
    }

    public ChildAdapter(List<ChildModel> data, Context context, ChildAdapterListener listener) {
        this.childModel = data;
        this.filteredChildModel = data;
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
        TextView hobby = holder.hobby;
        TextView sponsor = holder.sponsor;
        ImageButton remove = holder.remove;
        ImageButton edit = holder.edit;
        CardView card = holder.card;


        age.setText("Age: " + filteredChildModel.get(listPosition).getAge());
        name.setText("Name: " + filteredChildModel.get(listPosition).getName());
        country.setText("Country: " + filteredChildModel.get(listPosition).getCountry());
        hobby.setText("Hobby: " + filteredChildModel.get(listPosition).getHobby());

        if (filteredChildModel.get(listPosition).getSponsor() != null) {
            sponsor.setText("Sponsor: " + filteredChildModel.get(listPosition).getSponsor());
            sponsor.setVisibility(View.VISIBLE);

        } else {
            sponsor.setVisibility(View.INVISIBLE);
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.childOnEdit(v, listPosition);

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onRemoveChild(view, listPosition);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredChildModel.size();
    }

    public interface ChildAdapterListener {

        void childOnEdit(View v, int position);

        void onRemoveChild(View v, int position);

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredChildModel = childModel;
                } else {
                    List<ChildModel> filteredList = new ArrayList<>();
                    for (ChildModel child : childModel) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name
                        if (child.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(child);
                        }
                    }

                    filteredChildModel = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredChildModel;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredChildModel = (ArrayList<ChildModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
