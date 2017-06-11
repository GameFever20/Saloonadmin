package utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by bunny on 11/06/17.
 */

public class FireBaseHandler {
    private DatabaseReference mDatabase;


    public FireBaseHandler() {
        //hello he
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void uploadOrder(){

    }

    public void downloadOrder(){

    }

    public void uploadOrderList(){

    }

    public void downloadOrderList(){

    }
}
