package com.cfctapp.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.cfctapp.R;
import com.cfctapp.adapter.ChildAdapter;
import com.cfctapp.adapter.SponsorAdapter;
import com.cfctapp.adapter.SponsorChildAdapter;
import com.cfctapp.db.CFCTDatabase;
import com.cfctapp.models.ChildModel;
import com.cfctapp.utils.CountryData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SponsorChild extends AppCompatActivity {
    RecyclerView recyclerView;
    SponsorChildAdapter sponsorChildAdapter;
    List<ChildModel> childModelList;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_child);

        init();
    }

    void init() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childModelList = new ArrayList<>();

        getChildren();


    }

    void getChildren() {
        class GetChildren extends AsyncTask<Void, Void, List<ChildModel>> {

            @Override
            protected List<ChildModel> doInBackground(Void... voids) {


                List<ChildModel> childModels = CFCTDatabase.getCfctDatabase(getApplicationContext()).childDao().getChildren();

                return childModels;
            }

            @Override
            protected void onPostExecute(final List<ChildModel> childModels) {

                sponsorChildAdapter = new SponsorChildAdapter(childModels, getApplicationContext(), new SponsorChildAdapter.SponsorChildAdapterListener() {
                    @Override
                    public void sponsorChild(View v, final int position) {
                        /*get Sponsor name*/
                        Bundle extras = getIntent().getExtras();
                        if (extras != null) {
                            name = extras.getString("name");

                        }

                        /*update child table by inserting sponsor name*/
                        AlertDialog.Builder builder = new AlertDialog.Builder(SponsorChild.this);
                        builder.setTitle("Confirm Action!")
                                .setMessage("Would you like to proceed with the action?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things

                                        /*update child table by inserting sponsor name*/
                                        updateTask(childModels.get(position));
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);


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


                recyclerView.setAdapter(sponsorChildAdapter);
                super.onPostExecute(childModels);
            }
        }
        GetChildren gt = new GetChildren();
        gt.execute();
    }

    private void updateTask(final ChildModel task) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                task.setSponsor(name);
                CFCTDatabase.getCfctDatabase(getApplicationContext()).childDao().updateChild(task);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toasty.success(getApplicationContext(), "Sponsor added Successfully", Toasty.LENGTH_SHORT, true).show();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }
}
