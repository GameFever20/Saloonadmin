package app.admin.saloon.craftystudio.saloonadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;

import utils.ClickListener;
import utils.FireBaseHandler;
import utils.Order;
import utils.OrderAdapter;
import utils.RecyclerTouchListener;
import utils.Saloon;
import utils.SaloonAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    //Declaring order class object
    Order order;

    //Shared Preference declaring
    SharedPreferences pref;

    //Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //temporary arraylist for display
    ArrayList<Order> mOrderArraylist;

    ArrayList<Saloon> mSaloonArraylist;

    ProgressDialog progressDialog;

    boolean isLodingMoreOrder;
    boolean isLodingMoreSaloon;


    Saloon saloon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseMessaging.getInstance().subscribeToTopic("admin");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSaloonActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Intializing adapter and recycler
        mRecyclerView = (RecyclerView) findViewById(R.id.my_saloon_recycler_view);

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


        //Downloading saloon list from firebase
        showProgressDialog();
        downloadingSaloonList();

        // downloadingOrderList();

        FirebaseMessaging.getInstance().subscribeToTopic("admin");

    }

    private void downloadingOrderList() {

        //downloading Recent  OrderList from firebase

        //calling download orderlist
        showProgressDialog();
        fireBaseHandler.downloadOrderList(10, new FireBaseHandler.OnOrderListListner() {
            @Override
            public void onOrderList(ArrayList<Order> ordersArrayList) {
                mOrderArraylist = ordersArrayList;
                // specify an adapter (see also next example)
                mAdapter = new OrderAdapter(ordersArrayList, MainActivity.this);
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
                            if (!isLodingMoreOrder) {
                                //get last order ID
                                String lastorderId = mOrderArraylist.get(mOrderArraylist.size() - 1).getOrderID();
                                onScrolledOrderListToBottom(lastorderId);
                                //  Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });


                //handling touch event of item in recycler view
                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Order order = mOrderArraylist.get(position);

                        //passing data to detail activity
                        Bundle bundle = new Bundle();
                        bundle.putString("SaloonID", order.getSaloonID());
                        bundle.putString("OrderID", order.getOrderID());
                        bundle.putString("UserID", order.getUserID());
                        bundle.putString("ServiceID", order.getServiceID());

                        Intent intent = new Intent(MainActivity.this, FullDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
                hideProgressDialog();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Can't reach ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void downloadingSaloonList() {

        //calling download saloon list
        fireBaseHandler.downloadSaloonList(30, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                hideProgressDialog();

                mSaloonArraylist = saloonArrayList;
                // specify an adapter (see also next example)
                mAdapter = new SaloonAdapter(saloonArrayList);
                mRecyclerView.setAdapter(mAdapter);

                //Reverse a arraylist
                Collections.reverse(mSaloonArraylist);
                mAdapter.notifyDataSetChanged();

                //loading more
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                                                      @Override
                                                      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                          super.onScrolled(recyclerView, dx, dy);
                                                          if (!recyclerView.canScrollVertically(1)) {
                                                              if (!isLodingMoreSaloon) {

                                                                  //get last Saloon ID
                                                                  long lastSaloonPoint = mSaloonArraylist.get(mSaloonArraylist.size() - 1).getSaloonPoint();
                                                                  onScrolledSaloonListToBottom(lastSaloonPoint);
                                                                  //  Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();

                                                              } else {
                                                                  Toast.makeText(MainActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                                                              }
                                                          }
                                                      }
                                                  }

                );

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        saloon = mSaloonArraylist.get(position);

                        //calling new activity having orders of particular saloon
                        Intent intent = new Intent(MainActivity.this, OrderListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Saloon Class", saloon);
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

            }

        });


    }

    public void onScrolledSaloonListToBottom(long lastSaloonPoint) {

        isLodingMoreSaloon = true;
        Toast.makeText(MainActivity.this, "On Data calling ..", Toast.LENGTH_SHORT).show();

        fireBaseHandler.downloadMoreSaloonList(30, lastSaloonPoint, new FireBaseHandler.OnSaloonListListner() {
            @Override
            public void onSaloonList(ArrayList<Saloon> saloonArrayList) {

                if (saloonArrayList.size() >= 1) {
                    //remove last item of arraylist due to redundancy
                    saloonArrayList.remove(saloonArrayList.size() - 1);

                    if (saloonArrayList != null) {
                        //  String li = saloonArrayList.get(saloonArrayList.size() - 1).getOrderID();

                        //iterate to add orders in main arraylist
                        for (int i = saloonArrayList.size() - 1; i >= 0; i--) {
                            mSaloonArraylist.add(saloonArrayList.get(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "On Data Refreshed", Toast.LENGTH_SHORT).show();
                        isLodingMoreSaloon = false;

                    }

                } else {
                    isLodingMoreSaloon = false;
                    Toast.makeText(MainActivity.this, "Data Already Refreshed", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancel() {

            }
        });


    }

    private void onScrolledOrderListToBottom(String lastorderId) {

        isLodingMoreOrder = true;
        Toast.makeText(MainActivity.this, "On Data calling ..", Toast.LENGTH_SHORT).show();

        fireBaseHandler.downloadOrderList(20, lastorderId, new FireBaseHandler.OnOrderListListner() {
                    @Override
                    public void onOrderList(ArrayList<Order> ordersArrayList) {
                        //remove last item of arraylist due to redundancy
                        ordersArrayList.remove(ordersArrayList.size() - 1);
                        if (ordersArrayList != null) {

                            String li = ordersArrayList.get(ordersArrayList.size() - 1).getOrderID();

                            //iterate to add orders in main arraylist
                            for (int i = ordersArrayList.size() - 1; i >= 0; i--) {
                                mOrderArraylist.add(ordersArrayList.get(i));
                            }
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "On Data Refreshed", Toast.LENGTH_SHORT).show();
                            isLodingMoreOrder = false;
                        } else {
                            isLodingMoreOrder = false;
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this, "Data Already Refreshed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        isLodingMoreOrder = false;
                    }
                }

        );
        //Toast.makeText(this, "On Scrolled reverse" + mOrderArraylist, Toast.LENGTH_SHORT).show();

    }

    public void getDownloadListByStatus(int status) {

        showProgressDialog();
        fireBaseHandler.downloadOrderList(10, status, new FireBaseHandler.OnOrderListListner() {
            @Override
            public void onOrderList(ArrayList<Order> ordersArrayList) {
                mOrderArraylist = ordersArrayList;

                // specify an adapter (see also next example)
                mAdapter = new OrderAdapter(ordersArrayList, MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                hideProgressDialog();
                // Toast.makeText(MainActivity.this, "Order is "+ordersArrayList.get(0).getSaloonName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Can't reach ", Toast.LENGTH_SHORT).show();
            }
        });

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.completed_icon) {
            getDownloadListByStatus(3);
            Toast.makeText(this, "List of completed orders", Toast.LENGTH_SHORT).show();

            // Handle the camera action
        } else if (id == R.id.pending_icon) {
            getDownloadListByStatus(2);
            Toast.makeText(this, "List of pending orders", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.addsaloon_icon) {
            Intent intent = new Intent(MainActivity.this, AddSaloonActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_share) {
            Toast.makeText(this, "Sharing app..", Toast.LENGTH_SHORT).show();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out. Your Admin Application  ";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share..");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
            return true;

        } else if (id == R.id.nav_pendingSaloon) {
            Intent intent = new Intent(MainActivity.this, PendingSaloonActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_hirePhotographerSaloon) {
            Intent intent = new Intent(MainActivity.this, HirePhotographerActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
