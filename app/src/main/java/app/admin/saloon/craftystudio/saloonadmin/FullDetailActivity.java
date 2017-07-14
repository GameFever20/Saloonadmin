package app.admin.saloon.craftystudio.saloonadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import utils.FireBaseHandler;
import utils.Order;

public class FullDetailActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //All id's
    public  String saloonUID, orderID;

    //Imageview for showcasig saloon's image
    ImageView imageView;

    static Order ORDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get all id's

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();




        if (ORDER == null){

            saloonUID = getIntent().getStringExtra("SaloonUID");
            orderID = getIntent().getStringExtra("OrderID");


            new FireBaseHandler().downloadOrder(saloonUID, orderID, new FireBaseHandler.OnOrderDownload() {
                @Override
                public void onOrder(Order order) {
                    if (order !=null){
                        ORDER=order;
                        initializeActivity();
                    }else{
                        Toast.makeText(FullDetailActivity.this, "No order found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else
        {
            initializeActivity();
        }




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void initializeActivity() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //intializing imageview
        imageView = (ImageView) findViewById(R.id.full_detail_showcase_imageview);
        //change when saloon's image is ready
        // imageView.setImageResource(R.drawable.parlourimageshowcase);

    }


    @Override
    public void onBackPressed() {
        if (orderID ==null) {
            super.onBackPressed();
        }else{
            Intent intent =new Intent(FullDetailActivity.this ,OrderListActivity.class);
            intent.putExtra("saloonUID" ,saloonUID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_detail, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    TabbedSaloon tabbedSaloon = new TabbedSaloon();
                    return tabbedSaloon;
                case 1:
                    TabbedOrder tabbedOrder = new TabbedOrder();
                    return tabbedOrder;
                case 2:
                    TabbedService tabbedService = new TabbedService();
                    return tabbedService;
                case 3:
                    TabbedUser tabbedUser = new TabbedUser();
                    return tabbedUser;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Saloon";
                case 1:
                    return "Order";
                case 2:
                    return "Service";
                case 3:
                    return "User";
            }
            return null;
        }
    }
}
