package ie.deliverd;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VendorProfile extends AppCompatActivity {

    private Button Dashboard_Btn;
    private TextView UserName;
    private Button EditBtn;
    private DatabaseReference orderDB;
    private ImageView Pfp;
    private Button InfoBtn;
    private FirebaseAuth mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Intent intent = getIntent();
        String name = intent.getStringExtra(EditVendorProfile.EXTRA_MESSAGE);

        //CREATE THE TEXT VIEW
        UserName.setText(name);*/
        setContentView(R.layout.activity_vendor_profile);


        Dashboard_Btn = findViewById(R.id.Dashboard_Btn);
        Pfp = (ImageView) findViewById(R.id.Pfp);
        EditBtn = findViewById(R.id.EditBtn);
        InfoBtn = findViewById(R.id.InfoBtn);
        UserName = findViewById(R.id.UserName);
        mRef = FirebaseAuth.getInstance();
        orderDB = FirebaseDatabase.getInstance().getReference("users/vendors/" + mRef.getUid() + "/username");

        UserName.setText(mRef.getCurrentUser().getDisplayName());



        Dashboard_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorProfile.this, VendorDashboard.class));
            }
        });

        InfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorProfile.this, VendorInfo.class));
            }
        });

        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VendorProfile.this, EditVendorProfile.class));
            }
        });




    }

}
