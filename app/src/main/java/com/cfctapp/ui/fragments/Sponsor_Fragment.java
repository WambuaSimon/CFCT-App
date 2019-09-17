package com.cfctapp.ui.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.cfctapp.models.SponsorModel;
import com.cfctapp.models.SponsorModel;
import com.cfctapp.ui.activities.MainActivity;
import com.cfctapp.ui.activities.SponsorChild;
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
    SearchView searchView;
    RecyclerView recyclerView;
    SponsorAdapter sponsorAdapter;
    TextView empty_view;
    String nameEdt, contributionEdt, autoCompleteTextViewEdt;

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
        setHasOptionsMenu(true);

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // filter recycler view when query submitted
                sponsorAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {

//                Toast.makeText(ctx, "Stuff changed", Toast.LENGTH_SHORT).show();
                // filter recycler view when text is changed
                sponsorAdapter.getFilter().filter(query);

                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                            sponsorModel.setMonthlyAmount("Monthly Contribution:  " + "$" + contributionTxt);

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
            protected void onPostExecute(final List<SponsorModel> sponsorModels) {

                sponsorAdapter = new SponsorAdapter(sponsorModels, getActivity(), new SponsorAdapter.SponsorAdapterListener() {

                    @Override
                    public void sponsorOnEdit(View v, final int position) {
                        View dialogView = getLayoutInflater().inflate(R.layout.edit_sponsor_modal, null);
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
                                nameEdt = sponsor_name.getText().toString();
                                contributionEdt = contribution.getText().toString();
                                autoCompleteTextViewEdt = autoCompleteTextView.getText().toString();

                                /*update fcn*/
                                updateTask(sponsorModels.get(position));
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        fum_dialog.setContentView(dialogView);
                        BottomSheetBehavior fumBottomSheetBehavior = BottomSheetBehavior.from(((View) dialogView.getParent()));
                        fumBottomSheetBehavior.setPeekHeight(800);

                        fum_dialog.show();
                    }

                    @Override
                    public void assignChild(View v, int position) {
                        String name = sponsorModels.get(position).getName();

                        Intent intent = new Intent(getActivity(), SponsorChild.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("name",name);
                        startActivity(intent);
                    }

                    @Override
                    public void onRemoveSponsor(View v, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete Record!")
                                .setMessage("Would you like to Delete the selected record?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things

                                        deleteSponsor(sponsorModels.get(position));
                                        sponsorModels.remove(position);
                                        sponsorAdapter.notifyDataSetChanged();


                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                recyclerView.setAdapter(sponsorAdapter);
                super.onPostExecute(sponsorModels);
            }
        }
        GetSponsor gt = new GetSponsor();
        gt.execute();
    }

    private void deleteSponsor(final SponsorModel sponsorModel) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                CFCTDatabase.getCfctDatabase(getActivity()).sponsorDao().deleteSponsor(sponsorModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toasty.success(getActivity(), "Record Deleted Successfully", Toasty.LENGTH_SHORT, true).show();

            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void updateTask(final SponsorModel task) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                task.setName(nameEdt);
                task.setCountry(autoCompleteTextViewEdt);
                task.setMonthlyAmount(contributionEdt);


                CFCTDatabase.getCfctDatabase(getActivity()).sponsorDao().updateSponsor(task);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toasty.success(getActivity(), "Record Edited Successfully", Toasty.LENGTH_SHORT, true).show();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
