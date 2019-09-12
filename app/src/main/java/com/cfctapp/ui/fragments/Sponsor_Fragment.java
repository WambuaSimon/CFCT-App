package com.cfctapp.ui.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cfctapp.adapter.ChildAdapter;
import com.cfctapp.R;
import com.cfctapp.adapter.SponsorAdapter;
import com.cfctapp.db.CFCTDatabase;
import com.cfctapp.models.SponsorModel;
import com.cfctapp.models.SponsorModel;
import com.cfctapp.ui.activities.MainActivity;
import com.cfctapp.utils.CountryData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sponsor_Fragment extends Fragment {
    FloatingActionButton fab_add;
    View view;

    RecyclerView recyclerView;
    SponsorAdapter sponsorAdapter;
    TextView empty_view;

    public Sponsor_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sponsor, container, false);
        init();
        return view;
    }

    void init() {

        empty_view = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        getSponsor();

        fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChild();
            }
        });
    }

    void addChild() {
        View dialogView = getLayoutInflater().inflate(R.layout.add_sponsor_modal, null);
        /*get views in the modal*/
        final EditText sponsor_name = dialogView.findViewById(R.id.sponsor_name);
        final EditText contribution = dialogView.findViewById(R.id.sponsor_contribution);
        final AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView);

        final Button add_child = dialogView.findViewById(R.id.add_child);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, CountryData.countryNames);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);


        final BottomSheetDialog fum_dialog = new BottomSheetDialog(getActivity());

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sponsorName = sponsor_name.getText().toString();
                final String contributionTxt = contribution.getText().toString();
                final String countryName = autoCompleteTextView.getText().toString();

                if (sponsorName.isEmpty()) {
                    sponsor_name.setError("Enter Name to proceed");
                } else if (contributionTxt.isEmpty()) {
                    contribution.setError("Enter Monthly Contribution to proceed");
                } else if (countryName.isEmpty()) {
                    Toasty.warning(getActivity(), "Enter Country to proceed", Toasty.LENGTH_SHORT, true).show();

                } else {
                    /*insert to db*/
                    class SaveSponsor extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            SponsorModel sponsorModel = new SponsorModel();
                            sponsorModel.setName(sponsorName);
                            sponsorModel.setCountry(countryName);
                            sponsorModel.setMonthlyAmount("Monthly Contribution:  "+"$" + contributionTxt);

                            CFCTDatabase.getCfctDatabase(getActivity()).sponsorDao().insertSponsor(sponsorModel);


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Toasty.success(getActivity(), "Sponsor Details Saved Successfully", Toasty.LENGTH_SHORT, true).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }

                    SaveSponsor saveSponsor = new SaveSponsor();
                    saveSponsor.execute();
                }


            }
        });
        fum_dialog.setContentView(dialogView);
        BottomSheetBehavior fumBottomSheetBehavior = BottomSheetBehavior.from(((View) dialogView.getParent()));
        fumBottomSheetBehavior.setPeekHeight(800);

        fum_dialog.show();

    }

    void getSponsor() {
        class GetSponsor extends AsyncTask<Void, Void, List<SponsorModel>> {

            @Override
            protected List<SponsorModel> doInBackground(Void... voids) {


                List<SponsorModel> sponsorModels = CFCTDatabase.getCfctDatabase(getActivity()).sponsorDao().getSponsor();

                return sponsorModels;
            }

            @Override
            protected void onPostExecute(List<SponsorModel> sponsorModels) {

                sponsorAdapter = new SponsorAdapter(sponsorModels, getActivity(), new SponsorAdapter.SponsorAdapterListener() {
                    @Override
                    public void sponsorOnClick(View v, int position) {


                    }
                });

                recyclerView.setAdapter(sponsorAdapter);
                super.onPostExecute(sponsorModels);
            }
        }
        GetSponsor gt = new GetSponsor();
        gt.execute();
    }
}