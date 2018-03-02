package ie.deliverd;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateOrder extends AppCompatActivity {

    Toolbar toolbar;
    EditText orderTitleEditText;
    EditText pickUpAddrEditText;
    EditText customerNameEditText;
    EditText customerAddrEditText;
    EditText customerPhEditText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference ordersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        toolbar = findViewById(R.id.toolbar);
        orderTitleEditText = findViewById(R.id.orderTitleFld);
        pickUpAddrEditText = findViewById(R.id.pickUpAddrFld);
        customerNameEditText = findViewById(R.id.customerNameFld);
        customerAddrEditText = findViewById(R.id.customerAddrFld);
        customerPhEditText = findViewById(R.id.customerPhFld);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        ordersDB = FirebaseDatabase.getInstance().getReference("users/vendors/" + mAuth.getUid() + "/orders");

        findViewById(R.id.submitOrderBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void createOrder() {
        String orderTitle = orderTitleEditText.getText().toString().trim();
        String pickUpAddr = pickUpAddrEditText.getText().toString().trim();
        String customerName = customerNameEditText.getText().toString().trim();
        String customerAddr = customerAddrEditText.getText().toString().trim();
        String customerPhAddr = customerPhEditText.getText().toString().trim();

        if (orderTitle.equals("")) {
            orderTitleEditText.setError("Am order title is required");
            orderTitleEditText.requestFocus();
            return;
        }

        if (pickUpAddr.equals("")) {
            pickUpAddrEditText.setError("A pick up address is required");
            pickUpAddrEditText.requestFocus();
            return;
        }

        if (customerName.equals("")) {
            customerNameEditText.setError("A customer name is required");
            customerNameEditText.requestFocus();
            return;
        }

        if (customerAddr.equals("")) {
            customerAddrEditText.setError("A delivery address is required");
            customerAddrEditText.requestFocus();
            return;
        }

        if (customerPhAddr.equals("")) {
            customerPhEditText.setError("Customer phone number is required");
            customerPhEditText.requestFocus();
            return;
        }

        double[] pickUpLatLong = findLatLong(pickUpAddr);
        double[] customerLatLong = findLatLong(customerAddr);

        String id = ordersDB.push().getKey();
        Order order = new Order(id, orderTitle, mAuth.getUid(), pickUpAddr, customerName, customerAddr, customerPhAddr, pickUpLatLong[0], pickUpLatLong[1], customerLatLong[0], customerLatLong[1]);

        ordersDB.child(id).setValue(order);
        Toast.makeText(this, "Order Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, VendorDashboard.class));
    }

    @NonNull
    private double[] findLatLong(String addr) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        double[] latLong = new double[2];

        try {
            List<Address> addressList = geocoder.getFromLocationName(addr, 1);
            if (addressList != null && addressList.size() > 0){
                latLong[0] = addressList.get(0).getLatitude();
                latLong[1] = addressList.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLong;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}