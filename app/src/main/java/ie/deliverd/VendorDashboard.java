package ie.deliverd;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendorDashboard extends AppCompatActivity {

    private FloatingActionButton orderActionButton;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private TextView username;
    private DatabaseReference orderDB;
    private List<Order> orderList;
    private ListView listViewOrders;
    private FloatingActionButton orderActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);

        orderActionButton = findViewById(R.id.orderActionButton);
        orderActionButton2 = findViewById(R.id.orderActionButton2);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        orderDB = FirebaseDatabase.getInstance().getReference("users/vendors/" + mAuth.getUid()+ "/orders");
        orderList = new ArrayList<>();
        listViewOrders = findViewById(R.id.listViewOrders);

        username = findViewById(R.id.username);
        username.setText(mAuth.getCurrentUser().getDisplayName());

        setSupportActionBar(toolbar);

        orderActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboard.this, CreateOrder.class));
            }
        });


        orderActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboard.this, VendorProfile.class));
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

    @Override
    protected void onStart() {
        super.onStart();

        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()){
                    Order order = orderSnapshot.getValue(Order.class);
                    orderList.add(0, order);
                }

                OrderList adapter = new OrderList(VendorDashboard.this, orderList);
                listViewOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
