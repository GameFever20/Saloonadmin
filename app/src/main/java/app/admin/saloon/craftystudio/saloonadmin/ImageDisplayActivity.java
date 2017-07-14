package app.admin.saloon.craftystudio.saloonadmin;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageDisplayActivity extends AppCompatActivity {

    ImageView imageView;

    String mImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        imageView = (ImageView) findViewById(R.id.imagedisplay_imageview);


        //Getting Image Name
        mImageName = getIntent().getExtras().getString("imageName");
        String saloonUID = getIntent().getExtras().getString("saloonUID");

        //firebase storage connection
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference storageReference = storageRef.child("saloon_image/" + saloonUID + "/" + mImageName);


        //Display image


        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .thumbnail(1f)
                .override(900, 400)
                .crossFade(100)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);


    }
}
