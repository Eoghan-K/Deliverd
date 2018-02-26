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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverDashboard extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth mAuth;
    TextView username;
    DatabaseReference vendorsDB;
    List<Order> orderList;
    ListView listViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        username.setText(mAuth.getCurrentUser().getDisplayName());
        orderList = new ArrayList<>();
        listViewOrders = findViewById(R.id.listViewOrders);

        vendorsDB = FirebaseDatabase.getInstance().getReference("users/vendors");

        setSupportActionBar(toolbar);

        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Order order = orderList.get(position);

                Intent intent = new Intent(DriverDashboard.this, OrderInformation.class);
                intent.putExtra("order", (Serializable) order);
                startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();

        vendorsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot vendorSnapshot : dataSnapshot.getChildren()){
                    final Vendor vendor = vendorSnapshot.getValue(Vendor.class);

                    if (vendor.getOrders() != null){
                        for(Order order : vendor.getOrders().values()){
                            if (!order.isSelected()){
                                orderList.add(order);
                            }
                        }
                    }
                }

                OrderList adapter = new OrderList(DriverDashboard.this, orderList);
                listViewOrders.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
