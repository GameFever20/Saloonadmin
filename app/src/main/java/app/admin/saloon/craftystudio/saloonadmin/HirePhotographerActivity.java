package app.admin.saloon.craftystudio.saloonadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.RecyclerTouchListener;
import utils.Saloon;
import utils.SaloonAdapter;

public class HirePhotographerActivity extends AppCompatActivity {

    RecyclerView mHirePhotographerrecyclerview;

    private SaloonAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    ArrayList<Saloon> mHirePotographerArraylist;


    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_photographer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //Intializing adapter and recycler
        mHirePhotographerrecyclerview = (RecyclerView) findViewById(R.id.hirePhotographer_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mHirePhotographerrecyclerview.setLayoutManager(mLayoutManager);

        //progress dialog show
        progressDialog = new ProgressDialog(this);

        // instantiating firebasehandler class
        fireBaseHandler = new FireBaseHandler();

        showProgressDialog();
        downloadHirePhotographerSaloonList();


    }

    private void downloadHirePhotographerSaloonList() {

        fireBaseHandler.downloadSaloonList(30, true, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {
                mHirePotographerArraylist = saloonArrayList;

                // specify an adapter (see also next example)
                mAdapter = new SaloonAdapter(saloonArrayList);
                mHirePhotographerrecyclerview.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mHirePotographerArraylist);
                mAdapter.notifyDataSetChanged();

                hideProgressDialog();

                mHirePhotographerrecyclerview.addOnItemTouchListener(new RecyclerTouchListener(HirePhotographerActivity.this, mHirePhotographerrecyclerview, new ClickListener() {
                    @Override
                    public void onClick(View view, final int position) {

                        Saloon saloon = mHirePotographerArraylist.get(position);

                        //calling new activity having orders of particular saloon
                        Intent intent = new Intent(HirePhotographerActivity.this, PendingSaloonDetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Saloon Class", saloon);

                        intent.putExtras(bundle);

                        //for not getting accept button in detail activity
                        intent.putExtra("Activity", 1);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, final int position) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(HirePhotographerActivity.this);
                        builder.setMessage("Hired A Photographer for this Saloon.\n Remove from list ?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                Saloon saloon = mHirePotographerArraylist.get(position);
                                fireBaseHandler.uploadSaloonInfo(saloon.getSaloonUID(), "saloonHirePhotographer", false, new FireBaseHandler.OnSaloonDownload() {
                                    @Override
                                    public void onSaloon(Saloon saloon) {

                                    }

                                    @Override
                                    public void onSaloonValueUploaded(boolean isSucessful) {

                                    }
                                });
                                mHirePotographerArraylist.remove(position);
                                mAdapter.notifyDataSetChanged();

                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });


                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                }));

            }

            @Override
            public void onCancel() {

            }
        });

        hideProgressDialog();
    }

    public void showProgressDialog() {
        progressDialog.setMessage("Fetching Data..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }


}
