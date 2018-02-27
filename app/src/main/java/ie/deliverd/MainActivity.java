package ie.deliverd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

<<<<<<< HEAD
    public void signUp(View view){
        intent = new Intent(this, Signup_driver.class);
        startActivity(intent);
    }

    public void login(View view){
        intent = new Intent(this, Login.class);
        startActivity(intent);
    }
=======
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userDatabase;
    Toolbar toolbar;
>>>>>>> 818987f7d809ecb54f929a7f6bd4faaa8f451ee0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("users");
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (user != null) {
            LogUserIn();
        }
    }

    private void LogUserIn() {
        final String uid = user.getUid();

        userDatabase.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Driver driver = dataSnapshot.child(uid).getValue(Driver.class);
                if (driver != null) {
                    if (driver.getDriverId() != null) {
                        startActivity(new Intent(MainActivity.this, DriverDashboard.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                        .FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        startActivity(new Intent(MainActivity.this, VendorDashboard.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                        .FLAG_ACTIVITY_CLEAR_TASK));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                startActivity(new Intent(this, DriverSignUp.class));
                break;
            case R.id.loginBtn:
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}