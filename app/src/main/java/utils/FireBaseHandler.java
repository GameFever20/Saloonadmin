package utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by bunny on 11/06/17.
 */

public class FireBaseHandler {
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;



    public FireBaseHandler() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

    public void uploadOrder(Order order){
        mDatabaseRef= mFirebaseDatabase.getReference("Orders");

        mDatabaseRef.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("On upload","upload completed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("On upload","upload failed");

            }
        });
    }

    public void downloadOrder(){

    }

    public void uploadOrderList(){

    }

    public void downloadOrderList(int limit){

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders");

        Query myref2 = myRef.orderByChild("orderStatus").equalTo(1).limitToFirst(limit);


    }
}
