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

        mDatabaseRef.push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void downloadOrder(String saloonUID ,String orderUID ,final OnOrderDownload onOrderDownload) {
        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders/" + saloonUID+"/"+orderUID);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String saloonName = dataSnapshot.child("saloonName").getValue(String.class);

                Order order = dataSnapshot.getValue(Order.class);
                if (order!=null) {
                    order.setOrderID(dataSnapshot.getKey());
                }
                onOrderDownload.onOrder(order);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    public void uploadOrderList() {


    }

    public void downloadOrderList(int limit, int orderStatus, final OnOrderListListner onOrderListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders");

        Query myref2 = myRef.orderByChild("orderStatus").equalTo(orderStatus).limitToLast(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> ordersArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    ordersArrayList.add(order);
                }
                onOrderListListner.onOrderList(ordersArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onOrderListListner.onCancel();
            }
        });


    }

    public void downloadOrderList(int limit, String lastOrderID, final OnOrderListListner onOrderListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders");

        //Query myref2 = myRef.limitToLast(limit).endAt(lastOrderID);

        Query myref2 = myRef.orderByKey().endAt(lastOrderID).limitToLast(limit);


        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> ordersArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    ordersArrayList.add(order);
                }
                onOrderListListner.onOrderList(ordersArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onOrderListListner.onCancel();
            }
        });


    }
    public void downloadOrderList(String saloonUID, int limitTo, final OnOrderListListner onOrderListener) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders/" + saloonUID);

        Query myref2 = myRef.limitToLast(limitTo);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Order> orderArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    orderArrayList.add(order);
                }

                onOrderListener.onOrderList(orderArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void downloadOrderList(String saloonUID, int limitTo, String lastOrderId, final OnOrderListListner onOrderListener) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders/" + saloonUID);

        Query myref2 = myRef.orderByKey().endAt(lastOrderId).limitToLast(limitTo);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Order> orderArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    orderArrayList.add(order);
                }

                if(orderArrayList.size()>0) {
                    orderArrayList.remove(orderArrayList.size() - 1);
                }
                onOrderListener.onOrderList(orderArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    public void downloadMoreSaloonList(int limit, int lastSaloonPoint, final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        //Query myref2 = myRef.limitToLast(limit).endAt(lastOrderID);

        Query myref2 = myRef.orderByChild("saloonPoint").endAt(lastSaloonPoint).limitToLast(limit).startAt(1);


        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onSaloonListListner.onCancel();
            }
        });


    }


    public void downloadOrderList(int limit, final OnOrderListListner onOrderListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("Orders");

        Query myref2 = myRef.limitToLast(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> ordersArrayList = new ArrayList<Order>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    order.setOrderID(snapshot.getKey());
                    ordersArrayList.add(order);
                }
                onOrderListListner.onOrderList(ordersArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onOrderListListner.onCancel();
            }
        });


    }

    public void uploadSaloon(String uid, Saloon saloon) {
        mDatabaseRef = mFirebaseDatabase.getReference("saloon/" + uid);

        mDatabaseRef.setValue(saloon).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void downloadSaloon(String saloonUID, final OnSaloonDownload onSaloonDownload) {

        mDatabaseRef = mFirebaseDatabase.getReference().child("saloon/" + saloonUID);


        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String saloonName = dataSnapshot.child("saloonName").getValue(String.class);

                Saloon saloon = dataSnapshot.getValue(Saloon.class);
                onSaloonDownload.onSaloon(saloon);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    public void downloadSaloonList(int limit, final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        Query myref2 = myRef.orderByChild("saloonPoint").limitToLast(limit).startAt(1);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

                onSaloonListListner.onCancel();
            }
        });


    }
    public void downloadServiceList(String saloonUID , int limitTo  , final OnServiceListener onServiceListener){

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("services/");

        Query myref2 = myRef.orderByChild("saloonUID").equalTo(saloonUID).limitToLast(limitTo);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Service> serviceArrayList = new ArrayList<Service>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Service service = snapshot.getValue(Service.class);
                    if(service != null) {
                        service.setServiceUID(snapshot.getKey());
                    }
                    serviceArrayList.add(service);
                }


                onServiceListener.onServiceList(serviceArrayList,true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onServiceListener.onServiceList(null,false);

            }
        });

    }

    public void downloadSaloonList(int limit, int saloonPoint,final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        Query myref2 = myRef.orderByChild("saloonPoint").equalTo(saloonPoint).limitToLast(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

                onSaloonListListner.onCancel();
            }
        });


    }

    public void downloadSaloonList(int limit,boolean hirePhotographer,final OnSaloonListListner onSaloonListListner) {

        DatabaseReference myRef = mFirebaseDatabase.getReference().child("saloon");

        Query myref2 = myRef.orderByChild("saloonHirePhotographer").equalTo(hirePhotographer).limitToLast(limit);

        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Saloon> saloonArrayList = new ArrayList<Saloon>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Saloon saloon = snapshot.getValue(Saloon.class);
                    saloonArrayList.add(saloon);
                }
                onSaloonListListner.onSaloonList(saloonArrayList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

                onSaloonListListner.onCancel();
            }
        });


    }

    public void uploadSaloonInfo(String saloonUID, String saloonKeyValue, String value, final OnSaloonDownload onSaloonDownload) {

        mFirebaseDatabase.getReference().child("saloon/" + saloonUID + "/" + saloonKeyValue).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onSaloonDownload.onSaloonValueUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                onSaloonDownload.onSaloonValueUploaded(false);
            }
        });


    }
    public void uploadSaloonInfo(String saloonUID, String saloonKeyValue, int value, final OnSaloonDownload onSaloonDownload) {

        mFirebaseDatabase.getReference().child("saloon/" + saloonUID + "/" + saloonKeyValue).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onSaloonDownload.onSaloonValueUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                onSaloonDownload.onSaloonValueUploaded(false);
            }
        });


    }
    public void uploadSaloonInfo(String saloonUID, String saloonKeyValue, boolean value, final OnSaloonDownload onSaloonDownload) {

        mFirebaseDatabase.getReference().child("saloon/" + saloonUID + "/" + saloonKeyValue).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                onSaloonDownload.onSaloonValueUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                onSaloonDownload.onSaloonValueUploaded(false);
            }
        });


    }


    // Interface creation

    public interface OnOrderListListner {

        public void onOrderList(ArrayList<Order> ordersArrayList);

        public void onCancel();
    }

    public interface OnSaloonListListner {

        public void onSaloonList(ArrayList<Saloon> saloonArrayList);

        public void onCancel();
    }

    public interface OnSaloonDownload {
        public void onSaloon(Saloon saloon);

        public void onSaloonValueUploaded(boolean isSucessful);


    }

    public interface OnOrderDownload {
        public void onOrder(Order order);


    }
    public interface OnServiceListener {
        public void onSeviceUpload(boolean isSuccesful);

        public void onServiceList(ArrayList<Service> serviceArrayList, boolean isSuccesful);
    }


}
