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

public class OrderDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private Order order;
    private TextView orderTitle;
    private TextView pickUpAddr;
    private TextView customerName;
    private TextView customerAddr;
    private TextView customerPh;
    private Button confirmButton;
    private Button cancelButton;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
                builder.setMessage("Are you sure you want to deliver " + order.getOrderTitle())
                        .setTitle("Confirm Selection")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                order.getOrderStatus().setSelected(true);
                                DatabaseReference ordersDB = FirebaseDatabase.getInstance().getReference("users/vendors/" + order.getVendorID() + "/orders");
                                ordersDB.child(order.getOrderID()).setValue(order);

                                Intent intent = new Intent(OrderDetails.this, DriverMap.class);
                                intent.putExtra("order",order);
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
                startActivity(new Intent(OrderDetails.this, DriverDashboard.class));
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
            startActivity(new Intent(this, User.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
