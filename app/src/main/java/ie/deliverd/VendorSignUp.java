package ie.deliverd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VendorSignUp extends AppCompatActivity implements View.OnClickListener {

    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText addressEditText;
    ProgressBar progressBar;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference vendors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sign_up);

        usernameEditText = findViewById(R.id.usernameFld);
        emailEditText = findViewById(R.id.emailFld);
        passwordEditText = findViewById(R.id.passwordFld);
        addressEditText = findViewById(R.id.addressFld);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();
        vendors = FirebaseDatabase.getInstance().getReference("users/vendors");

        findViewById(R.id.signUpBtn).setOnClickListener(this);
        findViewById(R.id.driverSignUp).setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void signUpUser() {
        final String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (username.equals("")) {
            usernameEditText.setError("A username is required");
            usernameEditText.requestFocus();
            return;
        }

        if (email.equals("")) {
            emailEditText.setError("An email address is required");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        if (password.equals("")) {
            passwordEditText.setError("A password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password cannot be less than 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        if (address.equals("")) {
            addressEditText.setError("An address is required");
            addressEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    updateUsername(username);
                    startActivity(new Intent(VendorSignUp.this, VendorDashboard.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VendorSignUp.this);
                    builder.setMessage(task.getException().getMessage())
                            .setTitle("Oops..")
                            .setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void updateUsername(String username) {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        addUserInfoToDB();
                    }
                }
            });
        }
    }

    private void addUserInfoToDB() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String id = currentUser.getUid();

        Vendor vendor = new Vendor(id, email, username, address);
        vendors.child(id).setValue(vendor);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpBtn:
                signUpUser();
                break;
            case R.id.driverSignUp:
                startActivity(new Intent(this, DriverSignUp.class));
                finish();
                break;
        }
    }
}
