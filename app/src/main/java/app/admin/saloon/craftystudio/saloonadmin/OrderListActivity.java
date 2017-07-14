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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.Order;
import utils.OrderAdapter;
import utils.RecyclerTouchListener;
import utils.Saloon;

public class OrderListActivity extends AppCompatActivity {

    //Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //temporary arraylist for display
    ArrayList<Order> mOrderArraylist;

    ProgressDialog progressDialog;

    boolean isLodingMoreOrder;

    //Declaring order class object
    Order order;
    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    Saloon saloon;

     String saloonUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get Saloon Obbject
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        saloon = (Saloon) bundle.getSerializable("Saloon Class");
        if (saloon == null) {
            saloonUID =getIntent().getStringExtra("saloonUID");
            saloon =new Saloon();
            saloon.setSaloonUID(saloonUID);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Intializing adapter and recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.my_order_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //progress dialog show
        progressDialog = new ProgressDialog(this);

        // instantiating firebasehandler class
        fireBaseHandler = new FireBaseHandler();

        showProgressDialog();
        downloadingOrderList();

    }

    private void downloadingOrderList() {

        //downloading Recent  OrderList from firebase

        //calling download orderlist
        fireBaseHandler.downloadOrderList(saloon.getSaloonUID(), 30, new FireBaseHandler.OnOrderListListner() {

            @Override
            public void onOrderList(ArrayList<Order> ordersArrayList) {

                hideProgressDialog();

                mOrderArraylist = ordersArrayList;
                // specify an adapter (see also next example)
                mAdapter = new OrderAdapter(ordersArrayList, OrderListActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mOrderArraylist);
                mAdapter.notifyDataSetChanged();


                //loading more
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (!recyclerView.canScrollVertically(1)) {
                            //get last order ID
                            onScrolledOrderListToBottom();
                            //  Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


                //handling touch event of item in recycler view
                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(OrderListActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Order order = mOrderArraylist.get(position);


                        //passing data to detail activity
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("Order_Class", order);

                        bundle.putString("SaloonID", order.getSaloonID());
                        bundle.putString("OrderID", order.getOrderID());
                        bundle.putString("UserID", order.getUserID());
                        bundle.putString("ServiceID", order.getServiceID());

                        FullDetailActivity.ORDER = order;

                        Intent intent = new Intent(OrderListActivity.this, FullDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancel() {
                hideProgressDialog();
                Toast.makeText(OrderListActivity.this, "Can't reach ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void saloonMoreOrderFetch() {
        FireBaseHandler fireBaseHandler = new FireBaseHandler();
        fireBaseHandler.downloadOrderList(saloon.getSaloonUID(), 30
                , mOrderArraylist.get(mOrderArraylist.size() - 1).getOrderID()
                , new FireBaseHandler.OnOrderListListner() {
                    @Override
                    public void onOrderList(ArrayList<Order> orderArrayList) {

                        if (orderArrayList.size() > 0) {
                            for (int i = orderArrayList.size() - 1; i >= 0; i--) {
                                OrderListActivity.this.mOrderArraylist.add(orderArrayList.get(i));

                            }
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(OrderListActivity.this, "Fetched", Toast.LENGTH_SHORT).show();
                            isLodingMoreOrder = false;
                        }


                    }

                    @Override
                    public void onCancel() {
                        isLodingMoreOrder = false;


                    }
                });
    }

    private void onScrolledOrderListToBottom() {

        Toast.makeText(OrderListActivity.this, "On Data calling ..", Toast.LENGTH_SHORT).show();
        if (isLodingMoreOrder) {
            Toast.makeText(OrderListActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

        } else {

            isLodingMoreOrder = true;
            saloonMoreOrderFetch();
        }
    }

    public void showProgressDialog() {
        progressDialog.setMessage("Getting Data..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }


    @Override
    public void onBackPressed() {
        if (saloonUID ==null) {
            super.onBackPressed();
        }else{
            Intent intent =new Intent(OrderListActivity.this ,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_saloonprofile) {
            //calling new activity having orders of particular saloon
            Intent intent = new Intent(OrderListActivity.this, PendingSaloonDetailActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("Saloon Class", saloon);
            intent.putExtras(bundle);
            //for not getting accept button in detail activity
            intent.putExtra("Activity", 1);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


}
