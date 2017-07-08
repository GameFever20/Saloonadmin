package app.admin.saloon.craftystudio.saloonadmin;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import utils.FireBaseHandler;
import utils.Saloon;

public class AddSaloonActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //email id ,pw editText declaration
    private EditText mSaloonEmailEditText, mSaloonPasswordEditText, mSaloonNameEditText;
    private Button mSaloonAddSaloonButton;

    //progressdialog declaration
    ProgressDialog progressDialog;

    //Shared Preference declaring
    SharedPreferences pref;

    //Declaring Firebasehandler class object
    FireBaseHandler fireBaseHandler;

    //Declaring saloon's class
    Saloon saloon;
    String uid = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saloon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Views and Buttons
        mSaloonEmailEditText = (EditText) findViewById(R.id.saloon_email_EditText);
        mSaloonPasswordEditText = (EditText) findViewById(R.id.saloon_password_EditText);
        mSaloonNameEditText = (EditText) findViewById(R.id.saloon_name_EditText);
        mSaloonAddSaloonButton = (Button) findViewById(R.id.saloon_addsaloon_button);


        //Instantiating Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Instantiating progress dialog
        progressDialog = new ProgressDialog(this);

        // instantiating firebasehandler class
        fireBaseHandler = new FireBaseHandler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                createSaloonAccount(mSaloonEmailEditText.getText().toString().trim(), mSaloonPasswordEditText.getText().toString().trim());
            }
        });

        //Onclick of Add Saloon Button for creating newsaloon
        mSaloonAddSaloonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mSaloonNameEditText.getText().toString().isEmpty() && !mSaloonPasswordEditText.getText().toString().isEmpty() && ! mSaloonNameEditText.getText().toString().isEmpty()){
                    createSaloonAccount(mSaloonEmailEditText.getText().toString().trim(), mSaloonPasswordEditText.getText().toString().trim());

                }else{
                    Toast.makeText(AddSaloonActivity.this, "Please enter full detail", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void createSaloonAccount(String email, String password) {


        Log.d("In sign in ", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("In Saloon Activity", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("In Saloon Activity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AddSaloonActivity.this, "Authentication failed. Id already signed up",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();

                        // ...
                    }
                });


    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if (user != null) {


            FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseuser != null) {
                // Name, email address, and profile photo Url
                String email = firebaseuser.getEmail();
                // Check if user's email is verified
                boolean emailVerified = firebaseuser.isEmailVerified();
                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getToken() instead.
                uid= firebaseuser.getUid();
            }

                //sign out added saloon
            signOut();

        } else {
            Toast.makeText(this, "Email id or password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {

        //Instantiating Saloon's class with detail getting fromactivty
        saloon = new Saloon();
        saloon.setSaloonName(mSaloonNameEditText.getText().toString().trim());
        saloon.setSaloonPoint(-10);
        saloon.setSaloonUID(uid);

        //upload saloon detail in firebase
        fireBaseHandler.uploadSaloon(uid,saloon);

        mAuth.signOut();

        //get Admin email , pw and sign in admin back
        getAdminData();

        updateUI(null);
    }

    public void getAdminData() {

        //Retrieving value from shared preference
        pref = getApplicationContext().getSharedPreferences("AdminData", 0); // 0 - for private mode
        String e = pref.getString("Email", null);
        String p = pref.getString("Password", null);

        Toast.makeText(this, "Email and pw is" + e + "-" + p, Toast.LENGTH_SHORT).show();

        // sign in back admin
        signInAdmin(e, p);


    }

    public void signInAdmin(String email, String password) {

        Log.d("In sign in ", "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("In sign in ", "signInWithEmail:success");

                            mSaloonPasswordEditText.setText("");
                            mSaloonNameEditText.setText("");
                            mSaloonEmailEditText.setText("");


                            // FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("In sign in", "signInWithAdminEmail:failure", task.getException());
                            Toast.makeText(AddSaloonActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                        hideProgressDialog();
                    }
                });
        // [END sign_in_with_email]


    }

    public void showProgressDialog() {
        progressDialog.setMessage("Creating..");
        progressDialog.show();
    }

    public void hideProgressDialog() {

        progressDialog.cancel();
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mSaloonEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mSaloonEmailEditText.setError("Required.");
            valid = false;
        } else {
            mSaloonEmailEditText.setError(null);
        }

        String password = mSaloonPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mSaloonPasswordEditText.setError("Required.");
            valid = false;
        } else {
            mSaloonPasswordEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }


}
