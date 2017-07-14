package app.admin.saloon.craftystudio.saloonadmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.RecyclerTouchListener;
import utils.Service;
import utils.ServiceAdapter;

public class ServiceListActivity extends AppCompatActivity {
    //Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter serviceAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    ArrayList<Service> mPendingSaloonServicelist;


    ProgressDialog progressDialog;

    String saloonUID="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
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


        //getting saloonUID
        saloonUID=getIntent().getStringExtra("saloonUID");


        //Intializing adapter and recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.pendingSaloon_servicelistDisplay_recyclerview);

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
        downloadServiceList();

    }

    private void downloadServiceList() {
        fireBaseHandler.downloadServiceList(saloonUID, 30, new FireBaseHandler.OnServiceListener() {
            @Override
            public void onSeviceUpload(boolean isSuccesful) {


            }

            @Override
            public void onServiceList(ArrayList<Service> serviceArrayList, boolean isSuccesful) {

                hideProgressDialog();

                if (isSuccesful) {
                    mPendingSaloonServicelist= serviceArrayList;
                    serviceAdapter = new ServiceAdapter(mPendingSaloonServicelist);
                    initializeRecycler();
                    Toast.makeText(ServiceListActivity.this, "Fecthed service list", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ServiceListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeRecycler() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(serviceAdapter);


        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

              //  openAddSaloonServiceActivity(position);

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                    Toast.makeText(ServiceListActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void onScrolledToBottom() {
    }


    public void showProgressDialog() {
        progressDialog.setMessage("Fetching Data..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }

}
