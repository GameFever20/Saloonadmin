package app.admin.saloon.craftystudio.saloonadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

import utils.FireBaseHandler;
import utils.Saloon;

public class PendingSaloonDetailActivity extends AppCompatActivity {

    TextView mPendingSaloonName, mPendingSaloonPhoneNumber, mPendingSaloonAddress, mPendingSaloonOpenandCloseTime;
    ImageView mPendingSaloonProfile, mPendingSaloonShowcase1, mPendingSaloonShowcase2, mPendingSaloonShowcase3,
            mPendingSaloonShowcase4, mPendingSaloonShowcase5;
    Button mAcceptSaloonButton;

    LinearLayout mAcceptRejectLinearLayout;
    Saloon saloon;

    public  String SaloonUID;

    int min = 10;
    int max = 60;

    Random random;
    int randomvalue;
    String o, c;

    public FireBaseHandler fireBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_saloon_detail);

        //views
        mPendingSaloonName = (TextView) findViewById(R.id.pendingSaloon_saloonName_textview);
        mPendingSaloonPhoneNumber = (TextView) findViewById(R.id.pendingSaloon_saloonphone_no_textview);
        mPendingSaloonAddress = (TextView) findViewById(R.id.pendingSaloon_saloonaddress_textview);
        mPendingSaloonOpenandCloseTime = (TextView) findViewById(R.id.pendingSaloon_saloonTime_textview);

        mPendingSaloonProfile = (ImageView) findViewById(R.id.pendingSaloon_profileImage_collapsing_imageview);
        mPendingSaloonShowcase1 = (ImageView) findViewById(R.id.pendingSaloon_showcase1_imageview);
        mPendingSaloonShowcase2 = (ImageView) findViewById(R.id.pendingSaloon_showcase2_imageview);
        mPendingSaloonShowcase3 = (ImageView) findViewById(R.id.pendingSaloon_showcase3_imageview);
        mPendingSaloonShowcase4 = (ImageView) findViewById(R.id.pendingSaloon_showcase4_imageview);
        mPendingSaloonShowcase5 = (ImageView) findViewById(R.id.pendingSaloon_showcase5_imageview);


        mAcceptSaloonButton = (Button) findViewById(R.id.accept_pendingsaloon_button);

        mAcceptRejectLinearLayout = (LinearLayout) findViewById(R.id.accept_reject_btn_Linearlayout);


        //get Saloon Obbject
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int check = intent.getIntExtra("Activity", 0);

        if (check == 1) {
            mAcceptRejectLinearLayout.setVisibility(View.GONE);
        } else {

        }

        saloon = (Saloon) bundle.getSerializable("Saloon Class");


        if (saloon != null) {
            SaloonUID = saloon.getSaloonUID();

            initializeActivity();

        }else{
            SaloonUID = getIntent().getStringExtra("SaloonUID");

            new FireBaseHandler().downloadSaloon(SaloonUID, new FireBaseHandler.OnSaloonDownload() {
                @Override
                public void onSaloon(Saloon saloon) {
                    if (saloon!= null){

                        PendingSaloonDetailActivity.this.saloon =saloon ;
                        initializeActivity();
                    }else{
                        Toast.makeText(PendingSaloonDetailActivity.this, "Saloon not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSaloonValueUploaded(boolean isSucessful) {

                }
            });
        }

        //intializing firebase
        fireBaseHandler = new FireBaseHandler();

        //getting random no
        random = new Random();


    }

    private void initializeActivity() {
        //check am or pm
        checkAMorPM();

        mPendingSaloonName.setText(saloon.getSaloonName());
        mPendingSaloonPhoneNumber.setText(saloon.getSaloonPhoneNumber());
        mPendingSaloonAddress.setText(saloon.getSaloonAddress());
        mPendingSaloonOpenandCloseTime.setText("Opening Time : " + saloon.getOpeningTimeHour() + ":" + saloon.getOpeningTimeMinute() + o + "\n" + "Closing Time  : " + saloon.getClosingTimeHour() + ":" + saloon.getClosingTimeMinute() + c);


        if (saloon.getSaloonImageList() != null) {

            //firebase storage connection
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "profile_image");


            //Display Profile image

            if (saloon.getSaloonImageList().containsKey("profile_image")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonProfile);
            }

            //Display 1 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_1");

            if (saloon.getSaloonImageList().containsKey("image_1")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonShowcase1);
            }

            //Display 2 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_2");

            if (saloon.getSaloonImageList().containsKey("image_2")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonShowcase2);
            }

            //Display 3 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_3");

            if (saloon.getSaloonImageList().containsKey("image_3")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonShowcase3);
            }

            //Display 4 showcase image

            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_4");

            if (saloon.getSaloonImageList().containsKey("image_4")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonShowcase4);
            }


            //Display 5 showcase image
            storageReference = storageRef.child("saloon_image/" + SaloonUID + "/" + "image_5");

            if (saloon.getSaloonImageList().containsKey("image_5")) {

                Glide.with(this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .thumbnail(0.5f)
                        .override(900, 400)
                        .crossFade(100)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(mPendingSaloonShowcase5);
            }


        }


        // resolveRatingwithStar(saloon.getSaloonRating());

    }

    private void checkAMorPM() {
        int a = saloon.getOpeningTimeHour();
        if (a < 12 && a >= 0) {
            o = "AM";
        } else {
            o = "PM";
        }
        int b = saloon.getClosingTimeHour();
        if (b < 12 && b >= 0) {
            c = "AM";
        } else {
            c = "PM";
        }
    }


    public void pendingSaloonServiceList(View view) {
        Intent intent = new Intent(PendingSaloonDetailActivity.this, ServiceListActivity.class);
        intent.putExtra("saloonUID", saloon.getSaloonUID());
        startActivity(intent);
    }

    public void pendingSaloonDialer(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("156223456"));
        startActivity(intent);
    }

    public void pendingSaloonLocationGPS(View view) {
    }


    public void acceptPendingSaloon(View view) {

        randomvalue = random.nextInt(max - min + 1) + min;
        randomvalue =randomvalue + (saloon.getSaloonCityIndex()*1000000);
        Log.d("Random value : ", randomvalue + "");
        Log.d("Saloon UID", SaloonUID + "");

        fireBaseHandler.uploadSaloonInfo(SaloonUID, "saloonPoint", randomvalue, new FireBaseHandler.OnSaloonDownload() {
            @Override
            public void onSaloon(Saloon msaloon) {
                saloon = msaloon;
            }

            @Override
            public void onSaloonValueUploaded(boolean isSucessful) {

                if (isSucessful) {
                    Toast.makeText(PendingSaloonDetailActivity.this, "Saloon Accepted ", Toast.LENGTH_SHORT).show();
                    openPendingSaloonActivity();

                } else {
                    Toast.makeText(PendingSaloonDetailActivity.this, "Saloon Not Accepted ", Toast.LENGTH_SHORT).show();

                }
            }

        });


    }

    private void openPendingSaloonActivity() {
        Intent intent = new Intent(PendingSaloonDetailActivity.this, PendingSaloonActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // call this to finish the current activity
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

    public void rejectPendingSaloon(View view) {

        fireBaseHandler.uploadSaloonInfo(SaloonUID, "saloonPoint", -1000, new FireBaseHandler.OnSaloonDownload() {
            @Override
            public void onSaloon(Saloon msaloon) {
                saloon = msaloon;
            }

            @Override
            public void onSaloonValueUploaded(boolean isSucessful) {

                if (isSucessful) {
                    openPendingSaloonActivity();
                    Toast.makeText(PendingSaloonDetailActivity.this, "Saloon Rejected ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PendingSaloonDetailActivity.this, "Saloon Not Rejected ", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    public void profileImageDisplay(View view) {

        if (saloon.getSaloonImageList().containsKey("profile_image")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "profile_image");
            bundle.putString("saloonUID", saloon.getSaloonUID());

            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }

    }

    public void showCase1ImageDisplay(View view) {
        if (saloon.getSaloonImageList().containsKey("image_1")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "image_1");
            bundle.putString("saloonUID", saloon.getSaloonUID());
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }

    }

    public void showCase2ImageDisplay(View view) {
        if (saloon.getSaloonImageList().containsKey("image_2")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "image_2");
            bundle.putString("saloonUID", saloon.getSaloonUID());
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCase3ImageDisplay(View view) {
        if (saloon.getSaloonImageList().containsKey("image_3")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "image_3");
            bundle.putString("saloonUID", saloon.getSaloonUID());
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCase4ImageDisplay(View view) {
        if (saloon.getSaloonImageList().containsKey("image_4")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "image_4");
            bundle.putString("saloonUID", saloon.getSaloonUID());
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

    public void showCase5ImageDisplay(View view) {
        if (saloon.getSaloonImageList().containsKey("image_5")) {

            Intent intent = new Intent(PendingSaloonDetailActivity.this, ImageDisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("imageName", "image_5");
            bundle.putString("saloonUID", saloon.getSaloonUID());
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Toast.makeText(this, "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }
}
