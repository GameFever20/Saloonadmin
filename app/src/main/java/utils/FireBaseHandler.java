package utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by bunny on 11/06/17.
 */

public class FireBaseHandler {
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;


    public FireBaseHandler() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();

    }

    public void uploadOrder(Order order) {
        mDatabaseRef = mFirebaseDatabase.getReference("Orders");

        mDatabaseRef.setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("On upload", "upload completed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("On upload", "upload failed");

            }
        });
    }

    public void downloadOrder() {

    }

    public void uploadOrderList() {

    }

    public void downloadOrderList(int limit, int orderStatus) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders");

        Query myref2 = myRef.orderByChild("orderStatus").equalTo(orderStatus).limitToFirst(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> ordersArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    ordersArrayList.add(order);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public interface DataBaseHandlerOrderListListner {

        public void onOrderList(ArrayList<Order> ordersArrayList);


    }
}
