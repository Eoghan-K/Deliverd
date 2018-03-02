package ie.deliverd;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderInformation extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth mAuth;
    Order order;
    TextView orderTitle;
    TextView pickUpAddr;
    TextView customerName;
    TextView customerAddr;
    TextView customerPh;
    Button confirmButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);

        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        order = (Order) getIntent().getSerializableExtra("order");

        orderTitle = findViewById(R.id.orderTitle);
        pickUpAddr = findViewById(R.id.pickUpAddr);
        customerName = findViewById(R.id.customerName);
        customerAddr = findViewById(R.id.customerAddr);
        customerPh = findViewById(R.id.customerPh);
        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);

        orderTitle.setText(order.getOrderTitle());
        pickUpAddr.setText(order.getPickUpAddr());
        customerName.setText(order.getCustomerName());
        customerAddr.setText(order.getCustomerAddr());
        customerPh.setText(order.getCustomerPh());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderInformation.this);
                builder.setMessage("Are you sure you want to deliver " + order.getOrderTitle())
                        .setTitle("Confirm Selection")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                order.setSelected(true);
//                                DatabaseReference ordersDB = FirebaseDatabase.getInstance().getReference("users/vendors/" + order.getVendorID() + "/orders");
//                                ordersDB.child(order.getOrderID()).setValue(order);

                                double[] pLatLong = new double[]{order.getPickUpLatLong().get(0), order.getPickUpLatLong().get(1)};
                                double[] cLatLong = new double[]{order.getCustomerLatLong().get(0), order.getCustomerLatLong().get(1)};
                                Intent intent = new Intent(OrderInformation.this, DriverMap.class);
                                intent.putExtra("pLatLong", pLatLong);
                                intent.putExtra("cLatLong", cLatLong);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderInformation.this, DriverDashboard.class));
            }
        });
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
