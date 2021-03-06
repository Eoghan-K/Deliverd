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

public class User extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userDatabase;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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
                        startActivity(new Intent(User.this, DriverDashboard.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                        .FLAG_ACTIVITY_CLEAR_TASK));
                    } else {
                        startActivity(new Intent(User.this, VendorDashboard.class)
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