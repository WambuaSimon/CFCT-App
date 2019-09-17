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
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class Child_Fragment extends Fragment {
    FloatingActionButton fab_add;
    View view;
    List<ChildModel> childModelList;
    RecyclerView recyclerView;
    ChildAdapter childAdapter;
    TextView empty_view;
    MaterialDialog mDialog;
    Context ctx;
    SearchView searchView;
    String childNameEdt, childAgeEdt, countryNameEdt, hobbytxtEdt;

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
        childModelList = new ArrayList<>();

        getChildren();

        fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChild();
            }
        });

        ctx = this.getActivity();
        setHasOptionsMenu(true);
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
                childAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {

//                Toast.makeText(ctx, "Stuff changed", Toast.LENGTH_SHORT).show();
                // filter recycler view when text is changed
                childAdapter.getFilter().filter(query);

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
                    public void childOnEdit(View v, final int position) {

                        View dialogView = getLayoutInflater().inflate(R.layout.edit_child_modal, null);
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
                                childNameEdt = child_name.getText().toString();
                                childAgeEdt = child_age.getText().toString();
                                countryNameEdt = autoCompleteTextView.getText().toString();
                                hobbytxtEdt = hobby.getText().toString();

                                /*update method*/
                                updateTask(childModels.get(position));
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
                    public void onRemoveChild(View v, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete Record!")
                                .setMessage("Would you like to Delete the selected record?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things

                                        deleteChild(childModels.get(position));
                                        childModels.remove(position);
                                        childAdapter.notifyDataSetChanged();


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

    private void updateTask(final ChildModel task) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                task.setName(childNameEdt);
                task.setAge(Integer.parseInt(childAgeEdt));
                task.setCountry(countryNameEdt);
                task.setHobby(hobbytxtEdt);

                CFCTDatabase.getCfctDatabase(getActivity()).childDao().updateChild(task);

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


