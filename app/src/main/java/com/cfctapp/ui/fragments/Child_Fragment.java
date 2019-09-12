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
import com.cfctapp.db.CFCTDatabase;
import com.cfctapp.models.ChildModel;
import com.cfctapp.ui.activities.MainActivity;
import com.cfctapp.utils.CountryData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Child_Fragment extends Fragment {
    FloatingActionButton fab_add;
    View view;

    RecyclerView recyclerView;
    ChildAdapter childAdapter;
    TextView empty_view;
    MaterialDialog mDialog;

    public Child_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_child, container, false);

        init();

        return view;
    }


    void init() {
        empty_view = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        getChildren();

        fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChild();
            }
        });
    }

    void addChild() {
        View dialogView = getLayoutInflater().inflate(R.layout.add_child_modal, null);
        /*get views in the modal*/
        final EditText child_name = dialogView.findViewById(R.id.child_name);
        final EditText child_age = dialogView.findViewById(R.id.child_age);
        final AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView);
        final EditText hobby = dialogView.findViewById(R.id.hobby);
        final Button add_child = dialogView.findViewById(R.id.add_child);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, CountryData.countryNames);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);


        final BottomSheetDialog fum_dialog = new BottomSheetDialog(getActivity());

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String childName = child_name.getText().toString();
                final String childAge = child_age.getText().toString();
                final String countryName = autoCompleteTextView.getText().toString();
                final String hobbytxt = hobby.getText().toString();

                if (childName.isEmpty()) {
                    child_name.setError("Enter Child Name to proceed");
                } else if (childAge.isEmpty()) {
                    child_age.setError("Enter Child Age to proceed");
                } else if (countryName.isEmpty()) {
                    Toasty.warning(getActivity(), "Enter Country to proceed", Toasty.LENGTH_SHORT, true).show();

                } else if (hobbytxt.isEmpty()) {
                    hobby.setText("Enter Hobby to proceed");
                } else {
                    /*insert to db*/
                    class SaveChild extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            ChildModel childModel = new ChildModel();
                            childModel.setName(childName);
                            childModel.setAge(Integer.parseInt(childAge));
                            childModel.setCountry(countryName);
                            childModel.setHobby(hobbytxt);

                            CFCTDatabase.getCfctDatabase(getActivity()).childDao().insertChild(childModel);


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Toasty.success(getActivity(), "Child Details Saved Successfully", Toasty.LENGTH_SHORT, true).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }

                    SaveChild saveChild = new SaveChild();
                    saveChild.execute();
                }


            }
        });
        fum_dialog.setContentView(dialogView);
        BottomSheetBehavior fumBottomSheetBehavior = BottomSheetBehavior.from(((View) dialogView.getParent()));
        fumBottomSheetBehavior.setPeekHeight(800);

        fum_dialog.show();

    }


    void getChildren() {
        class GetChildren extends AsyncTask<Void, Void, List<ChildModel>> {

            @Override
            protected List<ChildModel> doInBackground(Void... voids) {


                List<ChildModel> childModels = CFCTDatabase.getCfctDatabase(getActivity()).childDao().getChildren();

                return childModels;
            }

            @Override
            protected void onPostExecute(final List<ChildModel> childModels) {

                childAdapter = new ChildAdapter(childModels, getActivity(), new ChildAdapter.ChildAdapterListener() {
                    @Override
                    public void childOnClick(View v, int position) {


                    }

                    @Override
                    public void onRemoveChild(View v, final int position) {
                        mDialog = new MaterialDialog.Builder(getActivity())
                                .setTitle("Remove from cart")

                                .setMessage("You are about to delete this bet, would you like to proceed?")
                                .setCancelable(false)
                                .setPositiveButton("Remove!", new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int position) {
                                        /*delete bet*/
                                        deleteChild(childModels.get(position));


                                        mDialog.dismiss();

//                                getGameBets();

                                        childModels.remove(position);
                                        childAdapter.notifyDataSetChanged();


//
                                    }
                                })
                                .setNegativeButton("Cancel", new MaterialDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .build();

                        // Show Dialog
                        mDialog.show();

                    }
                });

                recyclerView.setAdapter(childAdapter);
                super.onPostExecute(childModels);
            }
        }
        GetChildren gt = new GetChildren();
        gt.execute();
    }


    private void deleteChild(final ChildModel childModel) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                CFCTDatabase.getCfctDatabase(getActivity()).childDao().deleteChild(childModel);
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
}
