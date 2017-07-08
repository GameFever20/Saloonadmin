package app.admin.saloon.craftystudio.saloonadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class PendingSaloonActivity extends AppCompatActivity {


    //Recycler View
    private RecyclerView mRecyclerView;
    private SaloonAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    ArrayList<Saloon> mSaloonArraylist;


    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_saloon);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.pending_saloon_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //progress dialog show
        progressDialog = new ProgressDialog(this);

        // instantiating firebasehandler class
        fireBaseHandler = new FireBaseHandler();

        showProgressDialog();
        downloadPendingSaloonList();

    }

    private void downloadPendingSaloonList() {
        fireBaseHandler.downloadSaloonList(10, -100, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                mSaloonArraylist = saloonArrayList;
                // specify an adapter (see also next example)
                mAdapter = new SaloonAdapter(saloonArrayList);
                mRecyclerView.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mSaloonArraylist);
                mAdapter.notifyDataSetChanged();

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(PendingSaloonActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {


                        Saloon saloon = mSaloonArraylist.get(position);

                        //calling new activity having orders of particular saloon
                        Intent intent = new Intent(PendingSaloonActivity.this, PendingSaloonDetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Saloon Class", saloon);

                        intent.putExtras(bundle);
                        //for getting accept button in detail activity
                        intent.putExtra("Activity", 2);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                        //For deleting item

                    }
                }));


                hideProgressDialog();

            }

            @Override
            public void onCancel() {
                hideProgressDialog();

            }
        });
    }

    public void showProgressDialog() {
        progressDialog.setMessage("Fetching Data..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }


}
